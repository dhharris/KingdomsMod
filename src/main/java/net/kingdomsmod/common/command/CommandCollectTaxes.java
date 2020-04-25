package net.kingdomsmod.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.kingdomsmod.common.Kingdom;
import net.kingdomsmod.common.KingdomsMod;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandCollectTaxes {
    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("collect_taxes")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> execute(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))
                );
    }

    private static int execute(CommandSource source, ServerPlayerEntity player) throws CommandException {
        Kingdom[] allKingdoms = KingdomsMod.getKingdoms();
        for (Kingdom i : allKingdoms) {
            if (i.isWithinBorders(source.getPos())) {
                if (!source.getEntity().getName().getString().equals(i.getRuler())) {
                    throw new CommandException(new StringTextComponent(String.format("Only %s can modify taxes for this kingdom", i.getRuler())));
                }
                KingdomsMod.collectTaxes(i, player.getUniqueID());
                source.sendFeedback(new StringTextComponent(String.format("Cleared taxes for player: %s", player.getName().getString())).applyTextStyle(TextFormatting.AQUA), true);
                return 0;
            }
        }
        // Player is not inside anyone's kingdom
        throw new CommandException(new StringTextComponent("This action can only be performed inside a kingdom"));
    }
}