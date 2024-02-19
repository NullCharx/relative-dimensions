package es.nullbyte.relativedimensions.blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

/*
 * This class is used to create custom block tags: Blocks shall be added on datagen/ModBlockTagGenerator.java
 */
public class ModBlockTags {

        public static final TagKey<Block> NEED_ABERRANT_TOOL = tag("needs_aberrant_tool");
        public static final TagKey<Block> ABERRANT_BLOCK = tag("aberrant_block");

        private  static TagKey<Block> tag(String id) {

            return BlockTags.create(new ResourceLocation(MOD_ID, id));

        }
    }
