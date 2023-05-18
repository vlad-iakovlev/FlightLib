package com.possible_triangle.flightlib.fabric;

import com.possible_triangle.flightlib.fabric.services.FabricNetwork;
import com.possible_triangle.flightlib.init.CommonClass;
import com.possible_triangle.flightlib.logic.ControlManager;
import com.possible_triangle.flightlib.logic.JetpackLogic;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class FabricEntrypoint implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.INSTANCE.init();

        FabricNetwork.Companion.register();

        ServerTickEvents.START_SERVER_TICK.register(server ->
                server.getPlayerList().getPlayers().forEach(JetpackLogic.INSTANCE::onTick)
        );

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> ControlManager.INSTANCE.reset(handler.player));
    }

    @Override
    public void onInitializeClient() {
        CommonClass.INSTANCE.clientInit();

        ControlManager.INSTANCE.registerKeybinds(KeyBindingHelper::registerKeyBinding);

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> {
            var player = minecraft.player;
            if (player == null) return;
            ControlManager.INSTANCE.onTick(player);
            JetpackLogic.INSTANCE.onTick(player);
        });

        ClientTickEvents.END_CLIENT_TICK.register($ -> ControlManager.INSTANCE.checkKeys());
    }

}
