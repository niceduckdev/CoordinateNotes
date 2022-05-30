package net.niceduckdev.coordinatenotes.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TextColor;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class AmogusCommand
{
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(literal("amogus").executes(context -> run(context.getSource())));
    }

    private static int run(FabricClientCommandSource source) throws CommandSyntaxException
    {
        source.sendFeedback(new LiteralText("§4When the imposter is sus!"));
        return 1;
    }
}