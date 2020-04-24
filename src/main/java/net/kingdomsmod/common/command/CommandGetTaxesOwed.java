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

public class CommandGetTaxesOwed {
    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("get_taxes_owed")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(ctx -> execute(ctx.getSource(), EntityArgument.getPlayer(ctx, "player")))
                )
                .executes(ctx -> execute(ctx.getSource(), null)
                );
    }

    private static int execute(CommandSource source, ServerPlayerEntity player) throws CommandException {
        Kingdom[] allKingdoms = KingdomsMod.getKingdoms();
        for (Kingdom i : allKingdoms) {
            if (i.isWithinBorders(source.getPos())) {
                if (player != null) {
                    if (player.getName().getString().equals(i.getRuler())) {
                        throw new CommandException(new StringTextComponent("The Kingdom's ruler is not taxed"));
                    }
                    source.sendFeedback(new StringTextComponent(i.getTaxes().getTaxesOwedAsString(player.getUniqueID())), true);
                }
                else {
                    // Get all taxes for active players
                    source.sendFeedback(new StringTextComponent(String.format("Taxes for %s's Kingdom", i.getRuler())).applyTextStyle(TextFormatting.AQUA), true);
                    StringTextComponent allTaxes = new StringTextComponent("");
                    for (ServerPlayerEntity j : KingdomsMod.server.getPlayerList().getPlayers()) {
                        // Removed if statement for testing
//                        if (!j.getName().getString().equals(i.getRuler())) {
                            allTaxes.appendText(i.getTaxes().getTaxesOwedAsString(j.getUniqueID()));
//                        }
                    }
                    if (allTaxes.toString().isEmpty()) {
                        allTaxes.appendText("None");
                    }
                    source.sendFeedback(allTaxes, true);
                }
                return 0;
            }
        }
        // Player is not inside anyone's kingdom
        throw new CommandException(new StringTextComponent("This action can only be performed inside a kingdom"));
    }
}