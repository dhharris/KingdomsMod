package net.kingdomsmod.common;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BorderTest {

    @Test
    void TestIsWithinBorderShouldBeTrue() {
        Border border = new Border(new BlockPos(0, 0, 0), new BlockPos(100,0,100));
        BlockPos myBlock = new BlockPos(50, 100, 50);

        assertTrue(border.isWithinBorder(myBlock));
    }

    @Test
    void TestIsWithinBorderShouldBeFalse() {
        Border border = new Border(new BlockPos(0, 0, 0), new BlockPos(100,0,100));
        BlockPos myBlock = new BlockPos(-50, 0, 50);

        assertFalse(border.isWithinBorder(myBlock));
    }
}