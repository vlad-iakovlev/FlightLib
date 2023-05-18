package com.possible_triangle.flightlib.forge;

import com.possible_triangle.flightlib.Constants;
import com.possible_triangle.flightlib.forge.compat.CuriosCompat;
import com.possible_triangle.flightlib.forge.services.ForgeNetwork;
import com.possible_triangle.flightlib.forge.services.ForgeRegistries;
import com.possible_triangle.flightlib.init.CommonClass;
import com.possible_triangle.flightlib.logic.ControlManager;
import com.possible_triangle.flightlib.logic.JetpackLogic;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        CommonClass.INSTANCE.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        var forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener((FMLClientSetupEvent event) -> CommonClass.INSTANCE.clientInit());

        ForgeNetwork.Companion.init();
        ForgeRegistries.Companion.register(modBus);
        ForgeSources.INSTANCE.register();
        CuriosCompat.INSTANCE.register(modBus);

        modBus.addListener((RegisterKeyMappingsEvent event) -> ControlManager.INSTANCE.registerKeybinds(event::register));

        forgeBus.addListener((PlayerTickEvent event) -> {
            if (event.player instanceof LocalPlayer player) ControlManager.INSTANCE.onTick(player);
            JetpackLogic.INSTANCE.onTick(event.player);
        });

        forgeBus.addListener((InputEvent.Key event) -> ControlManager.INSTANCE.checkKeys());

        forgeBus.addListener((PlayerChangedDimensionEvent event) -> ControlManager.INSTANCE.reset(event.getEntity()));
        forgeBus.addListener((PlayerLoggedOutEvent event) -> ControlManager.INSTANCE.reset(event.getEntity()));
    }

}
