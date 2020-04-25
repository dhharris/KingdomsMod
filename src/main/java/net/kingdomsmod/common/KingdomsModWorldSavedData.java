package net.kingdomsmod.common;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;


// Ex: https://github.com/MinecraftForge/MinecraftForge/blob/1.14.x/src/main/java/net/minecraftforge/common/util/WorldCapabilityData.java
// Stores all saved data for the mod
// KingdomsMod class has an instance of this class and controls access
@MethodsReturnNonnullByDefault
public class KingdomsModWorldSavedData extends WorldSavedData {
    private static final String DATA_NAME = KingdomsMod.MOD_ID + "_data";
    private static final Logger LOGGER = LogManager.getLogger();


    private ArrayList<Kingdom> kingdoms = new ArrayList<>();

    public KingdomsModWorldSavedData() {
        super(DATA_NAME);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void read(CompoundNBT nbt) {
        LOGGER.info("Loading KingdomsMod data");
        kingdoms = new ArrayList<>();
        ListNBT kingdomsList = nbt.getList("kingdoms", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < kingdomsList.size(); i++) {
            // calls private addKingdomImpl method so data is not marked as dirty
            addKingdomImpl(new Kingdom(kingdomsList.getCompound(i)));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public CompoundNBT write(CompoundNBT nbt) {
        // See https://github.com/MinecraftForge/MinecraftForge/blob/1.14.x/src/main/java/net/minecraftforge/items/ItemStackHandler.java
        // for more examples of ListNBT usage
        ListNBT kingdomsList = new ListNBT();
        for (Kingdom i : kingdoms) {
            kingdomsList.add(i.serializeNBT());
        }
        nbt.put("kingdoms", kingdomsList);

        return nbt;
    }

    @Override
    public void save(File fileIn) {
        LOGGER.info(String.format("Saving KingdomsMod data as %s", fileIn.getName()));
        super.save(fileIn);
    }

    public Kingdom[] getKingdoms() {
        Kingdom[] ret = new Kingdom[kingdoms.size()];
        ret = kingdoms.toArray(ret);
        return ret;
    }

    /* Write Operations Below this line. Make sure to call markDirty() to save data
     */

    public void addKingdom(Kingdom kingdom) {
        addKingdomImpl(kingdom);
        markDirty();
    }

    public void collectTaxes(Kingdom kingdom, UUID playerUUID) {
        kingdom.getTaxes().markTaxesAsPaid(playerUUID);
        markDirty();
    }

    private void addKingdomImpl(Kingdom kingdom) {
        kingdoms.add(kingdom);
        MinecraftForge.EVENT_BUS.register(kingdom);
    }

}
