package net.kingdomsmod.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.kingdomsmod.common.Border;
import net.kingdomsmod.common.Kingdom;
import net.kingdomsmod.common.KingdomsMod;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;


public class CommandAdd {
    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("add")
                .then(Commands.argument("start", BlockPosArgument.blockPos())
                        .then(Commands.argument("end", BlockPosArgument.blockPos())
                                .executes(ctx -> execute(ctx.getSource(), BlockPosArgument.getBlockPos(ctx, "start"), BlockPosArgument.getBlockPos(ctx, "end"))
                                )));
    }

    private static int execute(CommandSource source, BlockPos start, BlockPos end) throws CommandException {
        // Allow pass-through for single player servers
        if (source.getServer().isSinglePlayer() || source.hasPermissionLevel(1)) {
            Kingdom kingdom = new Kingdom(new Border(start, end), source.getName());
            KingdomsMod.addKingdom(kingdom);
            source.sendFeedback(new StringTextComponent("Kingdom successfully created."), true);
            return 0;
        }
        throw new CommandException(new StringTextComponent("You do not have permission to perform this command."));
    }
}
