package net.kingdomsmod.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kingdom {
    private static final Logger LOGGER = LogManager.getLogger();
    private Border border;
    private PlayerEntity ruler;
    private TaxCollector taxes = new TaxCollector();

    public Kingdom(Border border) {
        this.border = border;
        // TODO: Add player argument
//        this.ruler = ruler;
    }

    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event) {
        // Handles block breaking , but only in the Kingdom
        BlockPos pos = event.getPos();
        if (border.isWithinBorder(pos)) {
            PlayerEntity player = event.getPlayer();
            Block block = event.getState().getBlock();
            Item item = block.asItem();
            taxes.addIncome(player, item);
            LOGGER.info(block + " mined by player " + player.getName().getString() + " at position " + pos);
        }
    }

//    @Override
//    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
//
//    }
}

