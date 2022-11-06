package com.volmit.wormholes;

import com.volmit.WormholesMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.openjdk.nashorn.internal.objects.annotations.Getter;

import java.util.List;

import static com.volmit.wormholes.ContentRegistry.Blocks.*;

public class ContentRegistry {
    public static final CreativeModeTab TAB = new CreativeModeTab("wormholes") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.WAND.get());
        }
    };

    @Getter
    public static List<Block> blockList() {
        return List.of(FRAME.get(), FRAME_BLACK.get(), FRAME_BLUE.get(), FRAME_BROWN.get(), FRAME_FUCHSIA.get(), FRAME_LIME.get());
    }

    public static class Blocks {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WormholesMod.MOD_ID);

        public static final RegistryObject<Block> FRAME = BLOCKS.register("frame",
                () -> new FrameBlock(Block.Properties.of(Material.STONE)
                        .strength(4f, 1200f)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.DEEPSLATE_TILES)
                        .emissiveRendering((__, ___, ____) -> true)));

        public static final RegistryObject<Block> FRAME_BLACK = BLOCKS.register("frame_black",
                () -> new FrameBlock(Block.Properties.of(Material.STONE)
                        .strength(4f, 1200f)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.DEEPSLATE_TILES)
                        .emissiveRendering((__, ___, ____) -> true)));
        public static final RegistryObject<Block> FRAME_BLUE = BLOCKS.register("frame_blue",
                () -> new FrameBlock(Block.Properties.of(Material.STONE)
                        .strength(4f, 1200f)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.DEEPSLATE_TILES)
                        .emissiveRendering((__, ___, ____) -> true)));
        public static final RegistryObject<Block> FRAME_BROWN = BLOCKS.register("frame_brown",
                () -> new FrameBlock(Block.Properties.of(Material.STONE)
                        .strength(4f, 1200f)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.DEEPSLATE_TILES)
                        .emissiveRendering((__, ___, ____) -> true)));
        public static final RegistryObject<Block> FRAME_FUCHSIA = BLOCKS.register("frame_fuchsia",
                () -> new FrameBlock(Block.Properties.of(Material.STONE)
                        .strength(4f, 1200f)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.DEEPSLATE_TILES)
                        .emissiveRendering((__, ___, ____) -> true)));
        public static final RegistryObject<Block> FRAME_LIME = BLOCKS.register("frame_lime",
                () -> new FrameBlock(Block.Properties.of(Material.STONE)
                        .strength(4f, 1200f)
                        .requiresCorrectToolForDrops()
                        .sound(SoundType.DEEPSLATE_TILES)
                        .emissiveRendering((__, ___, ____) -> true)));
    }

    public static class Items {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WormholesMod.MOD_ID);
        //WANDS
        public static final RegistryObject<Item> WAND = ITEMS.register("wand",
                () -> new ItemWand(new Item.Properties().tab(ContentRegistry.TAB)));


        //FRAMES
        public static final RegistryObject<Item> FRAME = ITEMS.register("frame",
                () -> new BlockItem(Blocks.FRAME.get(), new Item.Properties().tab(ContentRegistry.TAB)));
        public static final RegistryObject<Item> FRAME_BLACK = ITEMS.register("frame_black",
                () -> new BlockItem(Blocks.FRAME_BLACK.get(), new Item.Properties().tab(ContentRegistry.TAB)));
        public static final RegistryObject<Item> FRAME_BLUE = ITEMS.register("frame_blue",
                () -> new BlockItem(Blocks.FRAME_BLUE.get(), new Item.Properties().tab(ContentRegistry.TAB)));
        public static final RegistryObject<Item> FRAME_BROWN = ITEMS.register("frame_brown",
                () -> new BlockItem(Blocks.FRAME_BROWN.get(), new Item.Properties().tab(ContentRegistry.TAB)));
        public static final RegistryObject<Item> FRAME_FUCHSIA = ITEMS.register("frame_fuchsia",
                () -> new BlockItem(Blocks.FRAME_FUCHSIA.get(), new Item.Properties().tab(ContentRegistry.TAB)));
        public static final RegistryObject<Item> FRAME_LIME = ITEMS.register("frame_lime",
                () -> new BlockItem(Blocks.FRAME_LIME.get(), new Item.Properties().tab(ContentRegistry.TAB)));
    }
}
