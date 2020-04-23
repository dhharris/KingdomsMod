package net.kingdomsmod.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
    private static final HashSet<String> taxableItems = new HashSet<>(Arrays.asList(
            "gold_ore",
            "iron_ore",
            "diamond_ore",
            "redstone_ore",
            "lapis_ore"
    )); // Note: using HashSet provides better perf with O(1) contains()

    // Maps players' UUID to their taxable income
    private Hashtable<UUID, ItemCounter> taxableIncome = new Hashtable<>();

    public TaxCollector() {}

    public TaxCollector(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    boolean isTaxable(Item item) {
        // TODO: Make sure this works
        return taxableItems.contains(item.getName().getString());
    }

    public void addIncome(PlayerEntity player, Item item) {
        addIncomeImpl(player.getUniqueID(), Item.getIdFromItem(item));
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
