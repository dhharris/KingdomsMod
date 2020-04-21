package net.kingdomsmod.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

public class TaxCollector {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final HashSet<Item> taxableItems = new HashSet<>(Arrays.asList(
            Items.GOLD_ORE,
            Items.IRON_ORE,
            Items.DIAMOND_ORE,
            Items.REDSTONE_ORE,
            Items.LAPIS_ORE
    )); // Note: using HashSet provides better perf with O(1) contains()


    // Maps players to their taxable income
    // TODO: Need to store this so it doesn't reset when a player logs out
    private Hashtable<PlayerEntity, ItemCounter> taxableIncome = new Hashtable<>();

    boolean isTaxable(Item item) {
        return taxableItems.contains(item);
    }

    public void addIncome(PlayerEntity player, Item item) {
        if (isTaxable(item)) {
            if (!taxableIncome.containsKey(player)) {
                LOGGER.info("Adding player " + player.getName().getString() + " to TaxCollector");
                taxableIncome.put(player, new ItemCounter());
            }
            ItemCounter playerIncome = taxableIncome.get(player);
            playerIncome.add(Item.getIdFromItem(item));
        }
    }
}
