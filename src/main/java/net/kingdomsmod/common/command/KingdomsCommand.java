package net.kingdomsmod.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class KingdomsCommand {
    public KingdomsCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal("kingdoms")
                        .then(CommandStatus.register())
                        .then(CommandAdd.register())
                        .then(CommandGetTaxesOwed.register())

        );
    }
}
