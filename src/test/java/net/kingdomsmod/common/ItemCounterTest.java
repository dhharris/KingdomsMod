package net.kingdomsmod.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCounterTest {
    private final int testItemId = 1337;

    @Test
    void TestEquals() {
        ItemCounter counter = new ItemCounter();
        counter.add(testItemId);

        ItemCounter other = new ItemCounter();
        other.add(testItemId);

        assertTrue(counter.equals(other));
    }

    @Test
    void TestAdd() {
        ItemCounter counter = new ItemCounter();

        assertEquals(counter.get(testItemId), 0);

        counter.add(testItemId);

        assertEquals(counter.get(testItemId), 1);

        counter.add(testItemId, 2);

        assertEquals(counter.get(testItemId), 3);
    }

    @Test
    void TestNBT() {
        ItemCounter counter = new ItemCounter();
        counter.add(testItemId);
        counter.add(testItemId);

        CompoundNBT nbt = counter.serializeNBT();
        ItemCounter other = new ItemCounter();
        other.deserializeNBT(nbt);

        assertEquals(other.get(testItemId), counter.get(testItemId));

    }

}