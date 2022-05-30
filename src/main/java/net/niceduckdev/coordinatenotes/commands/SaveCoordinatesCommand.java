package net.niceduckdev.coordinatenotes.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.WorldSavePath;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class SaveCoordinatesCommand
{
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(literal("coordinates")
                .then(literal("save")
                        .then(argument("name", StringArgumentType.string())
                                .executes(ctx -> run(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
        );
    }

    private static int run(FabricClientCommandSource source, String name) throws CommandSyntaxException
    {
        double x = Math.round(source.getPlayer().getPos().x * 100.0) / 100.0;
        double y = Math.round(source.getPlayer().getPos().y * 100.0) / 100.0;
        double z = Math.round(source.getPlayer().getPos().z * 100.0) / 100.0;

        if (name.contains("#"))
        {
            return -1;
        }

        try {
            SaveToFile("#e" + name + " #f| x: " + x + " y: " + y + " z: " + z);
        } catch (IOException e) {
            return 0;
        }

        source.sendFeedback(new LiteralText("§fSaved x: " + x + " y: " + y + " z: " + z + " as " + "" + "§e" + name));

        return 1;
    }

    public static void SaveToFile(String save) throws IOException
    {
        try
        {
            FileWriter myWriter = new FileWriter(ClientToName(MinecraftClient.getInstance()) + "-coords.txt", true);
            myWriter.append(save + "\n");
            myWriter.close();
        }
        catch (IOException e)
        {
            return;
        }
    }

    public static String ClientToName(MinecraftClient client)
    {
        if (client.isInSingleplayer())
        {
            return Objects.requireNonNull(client.getServer()).getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
        }
        else if (client.getCurrentServerEntry() != null)
        {
            return client.getCurrentServerEntry().address;
        }
        else
        {
            return "global";
        }
    }
}