package com.volmit.wormholes;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.stream.Collectors;

@Mod(WormholesMod.MOD_ID)
public class WormholesMod
{
    public static final String MOD_ID = "wormholes";
    private static final Logger LOGGER = LogUtils.getLogger();

    public WormholesMod()
    {
        MinecraftForge.EVENT_BUS.register(this);
        Content.Blocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        Content.Items.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }
}
