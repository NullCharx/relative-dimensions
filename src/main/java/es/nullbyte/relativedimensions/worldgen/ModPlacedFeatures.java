package es.nullbyte.relativedimensions.worldgen;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.worldgen.oregen.OrePlacement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

/**
 * Placed features, on the other hand, control how the configured features are placed in the world. I.e, in which biomes,
 * how many, and how often.
 */
public class ModPlacedFeatures {

    //Placed feature for trees on vanilla biomes for the vanilla dimensions. Since the placed feature is the same for all dimensions, we can use the same placed feature for all dimensions
    public static final ResourceKey<PlacedFeature> ABERRANT_TREE_PLACED_VANILLA_BIOMES = registerKey("aberrant_tree_placed_vanilla_biomes");

    //Placed feature for ores on vanilla biomes for the vanilla dimensions. Since the placed feature is the same for all dimensions, we can use the same placed feature for all dimensions
    public static final ResourceKey<PlacedFeature> ABERRANT_ORE_PLACED_VANILLA_BIOMES = registerKey("aberrant_ore_placed_vanilla_biomes");

    //Aberrant tree placement for aberrant biomes
    public static final ResourceKey<PlacedFeature>  ABERRANT_TREE_PLACED_ABERRANT_BIOMES = registerKey("aberrant_tree_placed_aberrant_biomes");
    public static final ResourceKey<PlacedFeature>  ABERRANT_ORE_PLACED_ABERRANT_BIOMES = registerKey("aberrant_ore_placed_aberrant_biomes");

    public static void bootstap(BootstapContext<PlacedFeature> context){
        //There is no direct way to reference the configured features from the placed features, so we need to use a HolderGetter
        //That uses their resource keys to get them.
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        //ABERRANT TREE PLACEMENT CONFIGURATION FOR VANILLA BIOMES IN VANILLA DIMENSIONS ------
        //first number is avg trees per chunk, second number is the chance of extra trees
        // (Must be an integer if divided by 1( for example 1/0.1 = 10)) 0.3 for example is not valid.
        //This number cannot diverge much from a whole integer, or it will not work properly
        //The third number is the number of extra placements that will happen if the second number is met
        register(context, ABERRANT_TREE_PLACED_VANILLA_BIOMES, configuredFeatures.getOrThrow(ModConfiguredFeatures.ABERRANT_TREE_CONFIG),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.0008F, 1),
                        BlockInit.ABERRANT_SAPLING.get()));
        //ABERRANT ORE PLACEMENT CONFIGURATION FOR VANILLA BIOMES IN VANILLA DIMENSIONS ------
        //first number is avg veins per chunk, second number is the vein size
        //it will uniformly spawn the veins between the vertical anchors height (HeightRangePlacement.uniform)
        //if using HeightRangePlacement.triangle, it will become likely ti spawn veins in the middle heightm being the extremes less likely
        //if using
        register(context, ABERRANT_ORE_PLACED_VANILLA_BIOMES, configuredFeatures.getOrThrow(ModConfiguredFeatures.ABERRANT_ORE_CONFIG),
                OrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(100))));

        //ABERRANT TREE PLACEMENT CONFIGURATION FOR ABERRANT BIOMES ------ (More likely to spawn than on vanilla biomes)
        register(context, ABERRANT_TREE_PLACED_ABERRANT_BIOMES, configuredFeatures.getOrThrow(ModConfiguredFeatures.ABERRANT_TREE_CONFIG),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05F, 1),
                        BlockInit.ABERRANT_SAPLING.get()));
        //ABERRANT ORE PLACEMENT CONFIGURATION FOR ABERRANT BIOMES ------ (More likely to spawn than on vanilla biomes)
        register(context, ABERRANT_ORE_PLACED_ABERRANT_BIOMES, configuredFeatures.getOrThrow(ModConfiguredFeatures.ABERRANT_ORE_CONFIG),
                OrePlacement.commonOrePlacement(128, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(64))));
    }



    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MOD_ID, name));
    }
//
    public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> config,
                                List<PlacementModifier> modifierList){
        context.register(key, new PlacedFeature(config, List.copyOf(modifierList)));
    }
}
