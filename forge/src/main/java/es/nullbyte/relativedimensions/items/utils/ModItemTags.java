package es.nullbyte.relativedimensions.items.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

/**
 * This class is used to create custom item tags: Items shall be added on datagen/ModelItemTagGenerator.java
 */
public class ModItemTags {

    public static final TagKey<Item> ABERRANT_ITEM = tag("aberrant_item");

    private  static TagKey<Item> tag(String id) {
        return ItemTags.create(new ResourceLocation(MOD_ID, id));
    }
}

