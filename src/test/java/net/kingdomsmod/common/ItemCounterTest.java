package net.kingdomsmod.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCounterTest {
    int testItemId = 1337;
    @Test
    void TestAdd() {
        ItemCounter counter = new ItemCounter();

        assertEquals(counter.get(testItemId), 0);

        counter.add(testItemId);

        assertEquals(counter.get(testItemId), 1);

        counter.add(testItemId);

        assertEquals(counter.get(testItemId), 2);
    }

}