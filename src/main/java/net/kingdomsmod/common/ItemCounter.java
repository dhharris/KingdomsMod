package net.kingdomsmod.common;

import net.minecraft.item.Item;

import java.util.Hashtable;

/**
 * Simple class that holds items associated with a count
 */
public class ItemCounter {
    // Mapping from item ID to its count
    private Hashtable<Integer, Integer> countMap = new Hashtable<Integer, Integer>();

    public void add(int itemId) {
        if (!countMap.containsKey(itemId)) {
            countMap.put(itemId, 1);
        } else {
            // Add 1 of the item
            countMap.replace(itemId, countMap.get(itemId) + 1);
        }
    }

    public int get(int itemId) {
        if (!countMap.containsKey(itemId)) {
            return 0;
        } else {
            return countMap.get(itemId);
        }
    }
    public int get(Item item) {
        int itemId = Item.getIdFromItem(item);
        if (!countMap.containsKey(itemId)) {
            return 0;
        } else {
            return countMap.get(itemId);
        }
    }

}
