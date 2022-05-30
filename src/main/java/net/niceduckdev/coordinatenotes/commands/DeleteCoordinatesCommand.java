package net.niceduckdev.coordinatenotes.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class DeleteCoordinatesCommand
{
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(literal("coordinates")
                .then(literal("delete")
                        .then(argument("name", StringArgumentType.string())
                                .executes(ctx -> run(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
        );
    }

    private static int run(FabricClientCommandSource source, String name) throws CommandSyntaxException
    {
        List<String> coordList = ReadFilesInList(ClientToName(MinecraftClient.getInstance()) + "-coords.txt");
        Boolean found = false;

        for (int i = 0; i < coordList.size(); i++)
        {
            String currentCoords = coordList.get(i);
            if (currentCoords.startsWith("#e" + name + " #f| x: "))
            {
                found = true;
                source.sendFeedback(new LiteralText("§e" + name + " §fwas succesfully deleted!"));

                RemoveLineFromFile(ClientToName(MinecraftClient.getInstance()) + "-coords.txt", currentCoords);
            }
            else
            {
                found = false;
            }
        }

        if (!found)
        {
            source.sendFeedback(new LiteralText("§e" + name + " §fwasn't found in your saved coords!"));
        }

        return 1;
    }

    public static void RemoveLineFromFile(String file, String lineToRemove)
    {
        try
        {

            File inFile = new File(file);

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {

                if (!line.trim().equals(lineToRemove)) {

                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");

        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<String> ReadFilesInList(String fileName)
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