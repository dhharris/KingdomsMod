package net.kingdomsmod.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.kingdomsmod.common.Kingdom;
import net.kingdomsmod.common.KingdomsMod;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;


public class CommandStatus {
    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("status")
                .executes(ctx -> execute(ctx.getSource())
                );
    }

    private static int execute(CommandSource source) throws CommandException {
        Kingdom[] allKingdoms = KingdomsMod.getKingdoms();
        BlockPos pos = new BlockPos(source.getPos());
        StringTextComponent msg = new StringTextComponent("You are in ");
        for (Kingdom i : allKingdoms) {
            if (i.isWithinBorders(source.getPos())) {
                source.sendFeedback(msg.appendText(i.getRuler()).appendText("'s kingdom."), true);
                return 0;
            }
        }
        source.sendFeedback(msg.appendText("the wilderness."), true);

        return 0;
    }
}