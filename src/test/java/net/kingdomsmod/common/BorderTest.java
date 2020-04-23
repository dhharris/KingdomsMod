package net.kingdomsmod.common;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BorderTest {
    Border border = new Border(new BlockPos(0, 0, 0), new BlockPos(100,0,100));

    @Test
    void TestIsWithinBorderShouldBeTrue() {
        BlockPos myBlock = new BlockPos(50, 100, 50);
        assertTrue(border.isWithinBorder(myBlock));
    }

    @Test
    void TestIsWithinBorderShouldBeFalse() {
        BlockPos myBlock = new BlockPos(-50, 0, 50);
        assertFalse(border.isWithinBorder(myBlock));
    }

    @Test
    void TestEqualsShouldBeTrue() {
        Border other = new Border(new BlockPos(0, 0, 0), new BlockPos(100,0,100));
        assertTrue(border.equals(other));
    }

    @Test
    void TestEqualsWithDifferentYValuesShouldBeTrue() {
        Border other = new Border(new BlockPos(0, 1, 0), new BlockPos(100,1,100));
        assertTrue(border.equals(other));
    }

    @Test
    void TestEqualsShouldBeFalse() {
        Border other = new Border(new BlockPos(1, 0, 0), new BlockPos(100,0,100));
        assertFalse(border.equals(other));
    }

    @Test
    void TestNBT() {
        CompoundNBT nbt = border.serializeNBT();
        Border other = new Border(new BlockPos(1, 1, 1), new BlockPos(100,0,100));
        other.deserializeNBT(nbt);
        assertTrue(border.equals(other));
    }
}