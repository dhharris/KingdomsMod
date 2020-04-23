package net.kingdomsmod.common;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingdomTest {

    @Test
    void TestEqualsShouldBeTrue() {
        Kingdom kingdom = new Kingdom(new Border(new BlockPos(0, 0, 0), new BlockPos(100, 0, 100)), "Alfred");
        Kingdom other = new Kingdom(new Border(new BlockPos(0, 0, 0), new BlockPos(100, 0, 100)), "Alfred");
        assertTrue(kingdom.equals(other));
    }

    @Test
    void TestEqualsWithDifferentRulerShouldBeFalse() {
        Kingdom kingdom = new Kingdom(new Border(new BlockPos(0, 0, 0), new BlockPos(100, 0, 100)), "Alfred");
        Kingdom other = new Kingdom(new Border(new BlockPos(0, 0, 0), new BlockPos(100, 0, 100)), "Ecbert");
        assertFalse(kingdom.equals(other));
    }

    @Test
    void TestEqualsWithDifferentBordersShouldBeFalse() {
        Kingdom kingdom = new Kingdom(new Border(new BlockPos(0, 0, 0), new BlockPos(100, 0, 100)), "Alfred");
        Kingdom other = new Kingdom(new Border(new BlockPos(0, 0, 0), new BlockPos(1000, 0, 1000)), "Alfred");
        assertFalse(kingdom.equals(other));
    }

    @Test
    void TestNBT() {
        Kingdom kingdom = new Kingdom(new Border(new BlockPos(0, 0, 0), new BlockPos(100, 0, 100)), "Alfred");
        CompoundNBT nbt = kingdom.serializeNBT();
        Kingdom other = new Kingdom(nbt);
        assertTrue(kingdom.equals(other));
    }
}