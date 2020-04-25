package net.kingdomsmod.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Kingdom implements INBTSerializable<CompoundNBT> {
    private static final Logger LOGGER = LogManager.getLogger();

    private Border border;
    private String ruler;
    private TaxCollector taxes = new TaxCollector();

    public Kingdom(Border border, String ruler) {
        this.border = border;
        this.ruler = ruler;
    }

    public Kingdom(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    public Border getBorder() {
        return border;
    }

    public TaxCollector getTaxes() {
        return taxes;
    }

    public String getRuler() {
        return ruler;
    }

    public String getInfo() {
        return String.format("Ruler: %s\nTax rate: %2.0f%%\nBorders: %s", ruler, taxes.getTaxRate() * 100, border.toString());
    }

    public boolean isWithinBorders(Vec3d pos) {
        return border.isWithinBorder(new BlockPos(pos));
    }

    public boolean equals(Kingdom other) {
        return ruler.equals(other.ruler) && border.equals(other.border) && taxes.equals(other.taxes);
    }

    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event) {
        // Handles block breaking , but only in the Kingdom
        BlockPos pos = event.getPos();
        if (border.isWithinBorder(pos)) {
            PlayerEntity player = event.getPlayer();
            // TODO: Quantity is slightly different, could lead to off by 1 errors.
            List<ItemStack> items = Block.getDrops(event.getState().getBlockState(), KingdomsMod.world, pos, null);
            for (ItemStack item : items) {
                taxes.addIncome(player, item);
                LOGGER.info(String.format("Player %s harvested %s", player.getName().getString(), item.toString()));
            }
        }
    }

    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();

        CompoundNBT borderNBT = border.serializeNBT();
        CompoundNBT taxesNBT = taxes.serializeNBT();

        nbt.put("border", borderNBT);
        nbt.put("taxes", taxesNBT);
        nbt.putString("ruler", ruler);

        return nbt;
    }

    // For reading from NBT data structure and populating the
    public void deserializeNBT(CompoundNBT nbt)
    {
        CompoundNBT borderNBT = nbt.getCompound("border");
        CompoundNBT taxesNBT = nbt.getCompound("taxes");

        ruler = nbt.getString("ruler");
        border = new Border(borderNBT);
        taxes = new TaxCollector(taxesNBT);
    }
}

