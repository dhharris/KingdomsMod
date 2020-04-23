package net.kingdomsmod.common;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;


// Ex: https://github.com/MinecraftForge/MinecraftForge/blob/1.14.x/src/main/java/net/minecraftforge/common/util/WorldCapabilityData.java
// Stores all saved data for the mod
// KingdomsMod class has an instance of this class and controls access
@MethodsReturnNonnullByDefault
public class KingdomsModWorldSavedData extends WorldSavedData {
    private static final String DATA_NAME = KingdomsMod.MOD_ID + "_Data";

    private ArrayList<Kingdom> kingdoms = new ArrayList<>();

    public KingdomsModWorldSavedData() {
        super(DATA_NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
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
            // putInt("index", i); if we need kingdoms to know their index
            kingdomsList.add(i.serializeNBT());
        }
        nbt.put("kingdoms", kingdomsList);

        return nbt;
    }

    public Kingdom[] getKingdoms() {
        Kingdom[] ret = new Kingdom[kingdoms.size()];
        ret = kingdoms.toArray(ret);
        return ret;
    }

    public void addKingdom(Kingdom kingdom) {
        addKingdomImpl(kingdom);
        // Mark data as dirty, since the request came from public method
        markDirty();
    }

    private void addKingdomImpl(Kingdom kingdom) {
        kingdoms.add(kingdom);
        MinecraftForge.EVENT_BUS.register(kingdom);
    }

}
