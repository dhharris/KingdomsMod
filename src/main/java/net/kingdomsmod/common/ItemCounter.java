package net.kingdomsmod.common;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Hashtable;

/**
 * Simple class that holds items associated with a count
 */
public class ItemCounter implements INBTSerializable<CompoundNBT> {
    // Mapping from item ID to its count
    private Hashtable<Integer, Integer> countMap = new Hashtable<>();

    public void add(int itemId) {
        if (!countMap.containsKey(itemId)) {
            countMap.put(itemId, 1);
        } else {
            // Add 1 of the item
            countMap.replace(itemId, countMap.get(itemId) + 1);
        }
    }

    public int get(int itemId) {
        return countMap.getOrDefault(itemId, 0);
    }
    public int get(Item item) {
        int itemId = Item.getIdFromItem(item);
        return get(itemId);
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
