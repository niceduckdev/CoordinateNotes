package net.niceduckdev.coordinatenotes.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class GetCoordinatesCommand
{
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(literal("coordinates")
                .then(literal("list").executes(context -> run(context.getSource()))));
    }

    private static int run(FabricClientCommandSource source) throws CommandSyntaxException
    {
        List<String> coordList = ReadFileInList(ClientToName(MinecraftClient.getInstance()) + "-coords.txt");

        for (int i = 0; i < coordList.size(); i++) {
            String currentCoords = coordList.get(i);
            currentCoords = currentCoords.replace("#", "§");
            Text feedback = new LiteralText("§f" + currentCoords);
            source.sendFeedback(feedback);
        }

        return 1;
    }

    public static List<String> ReadFileInList(String fileName)
    {
        List<String> lines = Collections.emptyList();
        try
        {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
        return lines;
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