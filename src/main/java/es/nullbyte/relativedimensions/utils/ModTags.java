package es.nullbyte.relativedimensions.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEED_ABERRANT_TOOL = tag("needs_aberrant_tool");

        private  static TagKey<Block> tag(String id) {
            return BlockTags.create(new ResourceLocation(MOD_ID, id));
        }
    }

    public static class Items {

        private  static TagKey<Block> tag(String id) {
            return BlockTags.create(new ResourceLocation(MOD_ID, id));
        }
    }
}
