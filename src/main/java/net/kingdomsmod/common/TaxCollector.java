package net.kingdomsmod.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.UUID;

public class TaxCollector implements INBTSerializable<CompoundNBT> {
    private static final Logger LOGGER = LogManager.getLogger();
    // TODO: Display taxable items in CommandStatus
    // TODO: Add multiplier for ores which drop multiple items
    private static final HashSet<String> taxableItems = new HashSet<>(Arrays.asList(
            "Gold Ore",
            "Iron Ore",
            "Diamond Ore",
            "Redstone Ore",
            "Lapis Lazuli Ore"
    )); // Note: using HashSet provides better perf with O(1) contains()

    // Maps players' UUID to their taxable income
    private Hashtable<UUID, ItemCounter> taxableIncome = new Hashtable<>();
    private double taxRate = 0.1; // TODO: Add command to set this

    public TaxCollector() {}

    public TaxCollector(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    boolean isTaxable(Item item) {
        // TODO: Make sure this works
        return taxableItems.contains(item.getName().getString());
    }

    public void addIncome(PlayerEntity player, Item item) {
        if (isTaxable(item)) {
            addIncomeImpl(player.getUniqueID(), Item.getIdFromItem(item));
        }
    }

    // Add income directly, bypassing isTaxable
    public void addIncomeImpl(UUID playerUUID, int itemId) {
        if (!taxableIncome.containsKey(playerUUID)) {
            taxableIncome.put(playerUUID, new ItemCounter());
        }
        ItemCounter playerIncome = taxableIncome.get(playerUUID);
        playerIncome.add(itemId);
    }

    public ItemCounter getIncome(UUID playerUUID) {
        return taxableIncome.get(playerUUID);
    }

    public ItemCounter getTaxesOwed(UUID playerUUID) {
        ItemCounter income = getIncome(playerUUID);
        if (income == null) {
            return null;
        }
        ItemCounter taxes = new ItemCounter();

        for (int itemID : income.keySet()) {
            // Taxes are always rounded up to the nearest integer value
            taxes.add(itemID, (int) Math.ceil(income.get(itemID) * taxRate));
        }

        return taxes;
    }

    public String getTaxesOwedAsString(UUID playerUUID) {
       ItemCounter taxes = getTaxesOwed(playerUUID);
       if (taxes == null) {
           return String.format("%s does not owe any taxes\n", KingdomsModUtils.getPlayerName(playerUUID));
       }
       StringBuilder sb = new StringBuilder();
       sb.append(String.format("%s owes:\n", KingdomsModUtils.getPlayerName(playerUUID)));

       for (int itemId : taxes.keySet()) {
           Item item = Item.getItemById(itemId);
           sb.append(String.format("%s: %d\n", item.getName().getString(), taxes.get(itemId)));
       }
       return sb.toString();
    }

    public void markTaxesAsPaid(UUID playerUUID) {
        taxableIncome.remove(playerUUID);
    }


    public boolean equals(TaxCollector other) {
        if (!taxableIncome.keySet().equals(other.taxableIncome.keySet())) {
            return false;
        }
        for (UUID player : taxableIncome.keySet()) {
            if (!taxableIncome.get(player).equals(other.taxableIncome.get(player))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        for (UUID key : taxableIncome.keySet()) {
            nbt.put(key.toString(), taxableIncome.get(key).serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        taxableIncome = new Hashtable<>();
        for (String key : nbt.keySet()) {
            ItemCounter counter = new ItemCounter();
            counter.deserializeNBT(nbt.getCompound(key));
            taxableIncome.put(UUID.fromString(key), counter);
        }
    }
}
