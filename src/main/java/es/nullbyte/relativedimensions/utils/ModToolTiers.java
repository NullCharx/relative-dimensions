package es.nullbyte.relativedimensions.utils;

import es.nullbyte.relativedimensions.items.ItemInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModToolTiers {
    public static final Tier ABERRANT = TierSortingRegistry.registerTier(
            //Level: 1 Copper, 2 Iron, 3 Diamond, 4 Netherite
            //Uses (durability): 131, 250, 1561, 2031
            //Speed is swing speed, attack damage is damage per hit
            //Aberrant tools are at the same level as diamond, but with a less durability and more speed. The last line
            //Places aberrant items before diamond items in the tier list
            new ForgeTier(3, 300, 6.8F, 3.2F, 20,
                    ModTags.Blocks.NEED_ABERRANT_TOOL, ()-> Ingredient.of(ItemInit.ABERRANT_INGOT.get())),
            new ResourceLocation(MOD_ID, "aberrant"), List.of(Tiers.DIAMOND), List.of());

}
