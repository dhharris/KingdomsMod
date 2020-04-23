package net.kingdomsmod.common;

import net.minecraft.nbt.CompoundNBT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaxCollectorTest {
    TaxCollector collector = new TaxCollector();

    private final int testItemId = 1337;
    private final UUID testPlayer = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        collector.addIncomeImpl(testPlayer, testItemId);
    }

    @Test
    void TestEqualsShouldBeTrue() {
        TaxCollector other = new TaxCollector();
        other.addIncomeImpl(testPlayer, testItemId);
        assertTrue(collector.equals(other));
    }

    @Test
    void TestEqualsWithDisjointPlayerSetShouldBeFalse() {
        TaxCollector other = new TaxCollector();
        other.addIncomeImpl(UUID.randomUUID(), testItemId);
        assertFalse(collector.equals(other));
    }

    @Test
    void TestEqualsShouldBeFalse() {
        TaxCollector other = new TaxCollector();
        other.addIncomeImpl(testPlayer, testItemId);
        other.addIncomeImpl(testPlayer, testItemId);
        assertFalse(collector.equals(other));
    }

    @Test
    void TestAddIncomeImpl() {
        collector.addIncomeImpl(testPlayer, 42069);
        ItemCounter income = collector.getIncome(testPlayer);
        assertEquals(1, income.get(42069));
        assertEquals(1, income.get(testItemId));
    }

    @Test
    void TestAddIncomeImplWithNewPlayer() {
        UUID newPlayer = UUID.randomUUID();
        collector.addIncomeImpl(newPlayer, testItemId);
        assertEquals(1, collector.getIncome(newPlayer).get(testItemId));
        assertEquals(1, collector.getIncome(testPlayer).get(testItemId));
    }

    @Test
    void TestNBT() {
        TaxCollector other = new TaxCollector();
        CompoundNBT nbt = collector.serializeNBT();
        other.deserializeNBT(nbt);
        assertTrue(collector.equals(other));
    }
}