package net.niceduckdev.coordinatenotes.client;

import com.mojang.bridge.launcher.SessionEventListener;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.WorldSavePath;
import net.niceduckdev.coordinatenotes.CoordinateNotes;
import net.niceduckdev.coordinatenotes.commands.AmogusCommand;
import net.niceduckdev.coordinatenotes.commands.DeleteCoordinatesCommand;
import net.niceduckdev.coordinatenotes.commands.GetCoordinatesCommand;
import net.niceduckdev.coordinatenotes.commands.SaveCoordinatesCommand;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class CoordinateNotesClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        registerCommands(ClientCommandManager.DISPATCHER);
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        SaveCoordinatesCommand.register(dispatcher);
        GetCoordinatesCommand.register(dispatcher);
        DeleteCoordinatesCommand.register(dispatcher);
        //AmogusCommand.register(dispatcher);
    }
}