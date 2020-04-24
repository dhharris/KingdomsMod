package net.kingdomsmod.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.kingdomsmod.common.Kingdom;
import net.kingdomsmod.common.KingdomsMod;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandStatus {
    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("status")
                .executes(ctx -> execute(ctx.getSource())
                );
    }

    private static int execute(CommandSource source) throws CommandException {
        Kingdom[] allKingdoms = KingdomsMod.getKingdoms();
        for (Kingdom i : allKingdoms) {
            if (i.isWithinBorders(source.getPos())) {
                source.sendFeedback(new StringTextComponent(String.format("%s's Kingdom", i.getRuler())).applyTextStyle(TextFormatting.AQUA), true);
                source.sendFeedback(new StringTextComponent(i.getInfo()), true);
                return 0;
            }
        }
        // Player is not inside anyone's kingdom
        source.sendFeedback(new StringTextComponent("You are in the wilderness."), true);

        return 0;
    }
}