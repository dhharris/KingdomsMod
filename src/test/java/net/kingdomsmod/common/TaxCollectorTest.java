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
        collector.addIncomeImpl(testPlayer, testItemId, 5);
    }

    @Test
    void TestEqualsShouldBeTrue() {
        TaxCollector other = new TaxCollector();
        other.addIncomeImpl(testPlayer, testItemId, 5);
        assertTrue(collector.equals(other));
    }

    @Test
    void TestEqualsWithDisjointPlayerSetShouldBeFalse() {
        TaxCollector other = new TaxCollector();
        other.addIncomeImpl(UUID.randomUUID(), testItemId, 5);
        assertFalse(collector.equals(other));
    }

    @Test
    void TestEqualsShouldBeFalse() {
        TaxCollector other = new TaxCollector();
        other.addIncomeImpl(testPlayer, testItemId, 2);
        assertFalse(collector.equals(other));
    }

    @Test
    void TestAddIncomeImpl() {
        collector.addIncomeImpl(testPlayer, 42069, 5);
        ItemCounter income = collector.getIncome(testPlayer);
        assertEquals(5, income.get(42069));
        assertEquals(5, income.get(testItemId));
    }

    @Test
    void TestAddIncomeImplWithNewPlayer() {
        UUID newPlayer = UUID.randomUUID();
        collector.addIncomeImpl(newPlayer, testItemId, 5);
        assertEquals(5, collector.getIncome(newPlayer).get(testItemId));
        assertEquals(5, collector.getIncome(testPlayer).get(testItemId));
    }

    @Test
    void TestGetTaxesOwed() {
        UUID newPlayer = UUID.randomUUID();
        collector.addIncomeImpl(newPlayer, testItemId, 11);

        ItemCounter taxes = collector.getTaxesOwed(newPlayer);

        // Owes 10% of 11 rounded up = 2
        assertEquals(2, taxes.get(testItemId));
    }

    @Test
    void TestMarkTaxesAsPaid() {
        collector.markTaxesAsPaid(testPlayer);
        assertNull(collector.getIncome(testPlayer));
    }

    @Test
    void TestNBT() {
        TaxCollector other = new TaxCollector();
        CompoundNBT nbt = collector.serializeNBT();
        other.deserializeNBT(nbt);
        assertTrue(collector.equals(other));
    }
}