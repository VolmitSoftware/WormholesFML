package com.volmit.wormholes.content;

import com.mojang.datafixers.util.Either;
import com.volmit.Wormholes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Wormholes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemPortkey extends Item {
    public ItemPortkey(Properties properties) {
        super(properties.stacksTo(8).fireResistant()
                .rarity(Rarity.EPIC));
    }

    @SubscribeEvent
    public static void onTooltipGather(RenderTooltipEvent.GatherComponents e) {
        if (e.getItemStack().getItem() instanceof ItemPortkey) {
            e.getTooltipElements().add(Either.left(new TextComponent("§7Construct a frame of §dLogs§7 or §dPlanks§7, or §dWool")));
            e.getTooltipElements().add(Either.left(new TextComponent("§7Toss a Key in, Link to a §5§l§nVoid Dimension")));
            e.getTooltipElements().add(Either.left(new TextComponent("§7§lDimensional Scales Below: ")));
            e.getTooltipElements().add(Either.left(new TextComponent("§7Wool: 1 : 10 | Logs: 500 : 1 | Planks: 1 : 1000")));
            e.getTooltipElements().add(Either.left(new TextComponent("§7§oThe Overworld/Nether is  8 : 1")));
        }
    }


}
