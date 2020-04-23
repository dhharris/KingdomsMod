package net.kingdomsmod.common;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class Border implements INBTSerializable<CompoundNBT>  {
    static final int WORLD_HEIGHT = 255;
    private BlockPos start, end;

    public Border(BlockPos start, BlockPos end) {
        this.start = start;
        this.end = end;
    }

    public Border(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    public boolean isWithinBorder(BlockPos pos) {
        return pos.getX() >= start.getX() && pos.getZ() >= start.getZ() && pos.getX() <= end.getX() && pos.getZ() <= end.getZ();
    }

    // Equality for borders only looks at X and Z values
    public boolean equals(Border other) {
        return start.getX() == other.start.getX() && start.getZ() == other.start.getZ() && end.getX() == other.end.getX() && end.getZ() == other.end.getZ();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        CompoundNBT startNBT = new CompoundNBT();
        startNBT.putInt("x", start.getX());
        startNBT.putInt("z", start.getZ());

        CompoundNBT endNBT = new CompoundNBT();
        endNBT.putInt("x", end.getX());
        endNBT.putInt("z", end.getZ());

        nbt.put("start", startNBT);
        nbt.put("end", endNBT);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CompoundNBT startNBT = nbt.getCompound("start");
        CompoundNBT endNBT = nbt.getCompound("end");

        start = new BlockPos(startNBT.getInt("x"), 0, startNBT.getInt("z"));
        end = new BlockPos(endNBT.getInt("x"), 0, endNBT.getInt("z"));
    }
}
