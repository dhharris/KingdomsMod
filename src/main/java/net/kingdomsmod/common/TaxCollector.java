package net.kingdomsmod.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Hashtable;

public class TaxCollector {
    private static final Logger LOGGER = LogManager.getLogger();


    // Maps players to their taxable income
    // TODO: Need to store this so it doesn't reset when a player logs out
    private Hashtable<PlayerEntity, ItemCounter> taxableIncome = new Hashtable<PlayerEntity, ItemCounter>();

    boolean isTaxable(Item item) {
        return true;
    }

    public void addIncome(PlayerEntity player, Item item) {
        if (isTaxable(item)) {
            if (!taxableIncome.containsKey(player)) {
                LOGGER.info("Adding player " + player.getName().getString() + " to TaxCollector");
                taxableIncome.put(player, new ItemCounter());
            }
            ItemCounter playerIncome = taxableIncome.get(player);
            playerIncome.add(Item.getIdFromItem(item));
            LOGGER.info(player.getName().getString() + "'s income now at " + playerIncome.get(item) + " for item " + item);
        }
    }
}
