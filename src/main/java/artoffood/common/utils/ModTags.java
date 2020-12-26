package artoffood.common.utils;

import artoffood.ArtOfFood;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class ModTags {

    public static class Blocks {

        private static void init() { }

        private static IOptionalNamedTag<Block> tag(String name) {
            return BlockTags.createOptional(new ResourceLocation(ArtOfFood.MOD_ID, name));
        }

        //public static final Tags.IOptionalNamedTag<Block> CHESTS = tag("chests");
    }

    public static class Items {

        private static void init() { }

        private static IOptionalNamedTag<Item> tag(String name) {
            return ItemTags.createOptional(new ResourceLocation(ArtOfFood.MOD_ID, name));
        }

        public static final IOptionalNamedTag<Item> COOKING_TOOL = tag("cooking_tool");
    }
}
