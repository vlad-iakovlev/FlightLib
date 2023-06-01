package com.possible_triangle.flightlib.forge;

import com.possible_triangle.flightlib.Constants;
import com.possible_triangle.flightlib.forge.compat.CuriosCompat;
import com.possible_triangle.flightlib.forge.services.ForgeNetwork;
import com.possible_triangle.flightlib.forge.services.ForgeRegistries;
import com.possible_triangle.flightlib.init.CommonClass;
import com.possible_triangle.flightlib.logic.ControlManager;
import com.possible_triangle.flightlib.logic.ControlSender;
import com.possible_triangle.flightlib.logic.JetpackLogic;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {

    public ForgeEntrypoint() {
        CommonClass.INSTANCE.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        var forgeBus = MinecraftForge.EVENT_BUS;

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> clientInit(modBus, forgeBus));

        ForgeNetwork.Companion.init();
        ForgeRegistries.Companion.register(modBus);
        ForgeSources.INSTANCE.register();
        CuriosCompat.INSTANCE.register(modBus);

        forgeBus.addListener((PlayerTickEvent event) -> JetpackLogic.INSTANCE.onTick(event.player));

        forgeBus.addListener((PlayerChangedDimensionEvent event) -> ControlManager.INSTANCE.reset(event.getPlayer()));
        forgeBus.addListener((PlayerLoggedOutEvent event) -> ControlManager.INSTANCE.reset(event.getPlayer()));
    }

    private void clientInit(IEventBus modBus, IEventBus forgeBus) {
        CommonClass.INSTANCE.clientInit();

        modBus.addListener((FMLClientSetupEvent event) -> ControlManager.INSTANCE.registerKeybinds(ClientRegistry::registerKeyBinding));
        forgeBus.addListener((InputEvent.KeyInputEvent event) -> ControlSender.INSTANCE.checkKeys());
        forgeBus.addListener((PlayerTickEvent event) -> {
            if(event.player instanceof LocalPlayer player) ControlSender.INSTANCE.onTick(player);
        });
    }

}
