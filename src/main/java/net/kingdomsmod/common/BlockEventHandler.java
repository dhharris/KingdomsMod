package net.kingdomsmod.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockEventHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onBreakEvent(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        String playerName = player.getName().getString();
        BlockPos pos = event.getPos();
        LOGGER.info("Block mined by player " + playerName + " at position " + pos.toString());
    }

//    @Override
//    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
//
//    }
}

