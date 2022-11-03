package com.volmit.wormholes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import qouteall.imm_ptl.core.portal.Mirror;

import java.util.UUID;

public class Content {
    public static final CreativeModeTab TAB = new CreativeModeTab("wormholes") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.WAND.get());
        }
    };

    public static class Blocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WormholesMod.MOD_ID);

        public static final RegistryObject<Block> FRAME = BLOCKS.register("frame",
            () -> new FrameBlock(Block.Properties.of(Material.STONE)
                .strength(4f, 1200f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.DEEPSLATE_TILES)
                .emissiveRendering((__, ___, ____) -> true)));
    }

    public static class Items {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WormholesMod.MOD_ID);

        public static final RegistryObject<Item> WAND = ITEMS.register("wand",
            () -> new ItemWand(new Item.Properties().tab(Content.TAB)));

        public static final RegistryObject<Item> FRAME = ITEMS.register("frame",
            () -> new BlockItem(Blocks.FRAME.get(), new Item.Properties().tab(Content.TAB)));
    }
}
