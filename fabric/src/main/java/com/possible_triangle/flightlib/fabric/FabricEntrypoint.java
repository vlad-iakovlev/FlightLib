package com.possible_triangle.flightlib.fabric;

import com.possible_triangle.flightlib.fabric.compat.TrinketsCompat;
import com.possible_triangle.flightlib.fabric.services.FabricNetwork;
import com.possible_triangle.flightlib.init.CommonClass;
import com.possible_triangle.flightlib.logic.ControlManager;
import com.possible_triangle.flightlib.logic.JetpackLogic;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.INSTANCE.init();

        FabricNetwork.Companion.register();
        FabricSources.INSTANCE.register();
        TrinketsCompat.INSTANCE.register();

        ServerTickEvents.START_SERVER_TICK.register(server ->
                server.getPlayerList().getPlayers().forEach(JetpackLogic.INSTANCE::onTick)
        );

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> ControlManager.INSTANCE.reset(handler.player));
    }

}
