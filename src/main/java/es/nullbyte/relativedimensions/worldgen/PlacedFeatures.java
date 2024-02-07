package es.nullbyte.relativedimensions.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class PlacedFeatures {

    public static final ResourceKey<PlacedFeature> OW_ABERRANT_ORE_PLACED = registerKey("ow_aberrant_ore_placed");
    public static final ResourceKey<PlacedFeature> NETH_ABERRANT_ORE_PLACED = registerKey("neth_aberrant_ore_placed");
    public static final ResourceKey<PlacedFeature> END_ABERRANT_ORE_PLACED = registerKey("end_aberrant_ore_placed");

    public static void bootstap(BootstapContext<PlacedFeature> context){
        //There is no direct way to reference the configured features from the placed features, so we need to use a HolderGetter
        //That uses their resource keys to get them.
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        //first number is avg veins per chunk, second number is the vein size
        //it will uniformly spawn the veins between the vertical anchors height (HeightRangePlacement.uniform)
        //if using HeightRangePlacement.triangle, it will become likely ti spawn veins in the middle heightm being the extremes less likely
        //if using
        register(context, OW_ABERRANT_ORE_PLACED, configuredFeatures.getOrThrow(ConfiguredFeatures.OVERWORLD_ABERRANT_ORE),
                OrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(100))));
        register(context, NETH_ABERRANT_ORE_PLACED, configuredFeatures.getOrThrow(ConfiguredFeatures.NETHER_ABERRANT_ORE),
                OrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(100))));
        register(context, END_ABERRANT_ORE_PLACED, configuredFeatures.getOrThrow(ConfiguredFeatures.END_ABERRANT_ORE),
                OrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(100))));

    }



    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(MOD_ID, name));
    }

    public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> config,
                                List<PlacementModifier> modifierList){
        context.register(key, new PlacedFeature(config, List.copyOf(modifierList)));
    }
}
