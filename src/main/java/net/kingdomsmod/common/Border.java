package net.kingdomsmod.common;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Border {
    static final int WORLD_HEIGHT = 255;
    private BlockPos start, end;

    public Border(BlockPos start, BlockPos end) {
        this.start = start;
        this.end = end;
    }

    public boolean isWithinBorder(BlockPos pos) {
        return pos.getX() >= start.getX() && pos.getZ() >= start.getZ() && pos.getX() <= end.getX() && pos.getZ() <= end.getZ();
    }
}
