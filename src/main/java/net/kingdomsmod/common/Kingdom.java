package net.kingdomsmod.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kingdom {
    private static final Logger LOGGER = LogManager.getLogger();
    private Border border;
    private String ruler;
    private TaxCollector taxes = new TaxCollector();

    public Kingdom(Border border, String ruler) {
        this.border = border;
        this.ruler = ruler;
    }

    public boolean isWithinBorders(Vec3d pos) {
        return border.isWithinBorder(new BlockPos(pos));
    }

    public String getRuler() {
        return ruler;
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

