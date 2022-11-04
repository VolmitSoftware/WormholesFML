package com.volmit.wormholes;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WormholesMod.MOD_ID)
public class WormholesMod {
    public static final String MOD_ID = "wormholes";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WormholesMod() {
        MinecraftForge.EVENT_BUS.register(this);
        Content.Blocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        Content.Items.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
