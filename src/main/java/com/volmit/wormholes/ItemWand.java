package com.volmit.wormholes;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;

public class ItemWand extends Item {
    Cuboid frame;

    public ItemWand(Properties properties) {
        super(properties.stacksTo(1).fireResistant()
            .durability(255).rarity(Rarity.RARE));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(pContext.getLevel().isClientSide()) {
            return super.useOn(pContext);
        }

        BlockPos pos = pContext.getClickedPos();

        if(pContext.getLevel().getBlockState(pos).getBlock().equals(Content.Blocks.FRAME.get())) {
            Cuboid c = new Framer(pContext.getLevel(), pos).tick(512).validate();

            if(c != null)
            {
                pContext.getPlayer().sendMessage(new TextComponent("Frame: " + c.getLowerNE() + " -> " + c.getUpperSW()), pContext.getPlayer().getUUID());
                frame = c;
            }

            else {
                pContext.getPlayer().sendMessage(new TextComponent("Invalid Frame"), pContext.getPlayer().getUUID());
            }
        }

        return super.useOn(pContext);
    }
}
