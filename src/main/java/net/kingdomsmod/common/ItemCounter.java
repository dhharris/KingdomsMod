package net.kingdomsmod.common;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Hashtable;
import java.util.Set;

/**
 * Simple class that holds items associated with a count
 */
public class ItemCounter implements INBTSerializable<CompoundNBT> {
    // Mapping from item ID to its count
    private Hashtable<Integer, Integer> countMap = new Hashtable<>();

    public void add(int itemId) {
        add(itemId, 1);
    }

    public void add(int itemId, int amount) {
        if (!countMap.containsKey(itemId)) {
            countMap.put(itemId, amount);
        } else {
            // Add 1 of the item
            countMap.replace(itemId, countMap.get(itemId) + amount);
        }
    }

    public int get(int itemId) {
        return countMap.getOrDefault(itemId, 0);
    }

    public Set<Integer> keySet() {
        return countMap.keySet();
    }

    public boolean equals(ItemCounter other) {
        return countMap.equals(other.countMap);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        for (int key : countMap.keySet()) {
            nbt.putInt(Integer.toString(key), countMap.get(key));
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        countMap = new Hashtable<>();
        for (String key : nbt.keySet()) {
            countMap.put(Integer.parseInt(key), nbt.getInt(key));
        }
    }
}
