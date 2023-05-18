package com.possible_triangle.flightlib.fabric;

import com.possible_triangle.flightlib.init.CommonClass;
import com.possible_triangle.flightlib.logic.ControlManager;
import com.possible_triangle.flightlib.logic.ControlSender;
import com.possible_triangle.flightlib.logic.JetpackLogic;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CommonClass.INSTANCE.clientInit();

        ControlManager.INSTANCE.registerKeybinds(KeyBindingHelper::registerKeyBinding);

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> {
            var player = minecraft.player;
            if (player == null) return;
            ControlSender.INSTANCE.onTick(player);
            JetpackLogic.INSTANCE.onTick(player);
        });

        ClientTickEvents.END_CLIENT_TICK.register($ -> ControlSender.INSTANCE.checkKeys());
    }

}
