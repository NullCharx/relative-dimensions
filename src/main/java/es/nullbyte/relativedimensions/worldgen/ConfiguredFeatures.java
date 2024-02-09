package es.nullbyte.relativedimensions.worldgen;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ConfiguredFeatures {
    //Features are the actual generation rules for the world. They are used to generate structures, ores, trees, etc.
    public static final ResourceKey<ConfiguredFeature<?,?>> OVERWORLD_ABERRANT_ORE = registerKey("ow_aberrant_ore");
    public static final ResourceKey<ConfiguredFeature<?,?>> NETHER_ABERRANT_ORE = registerKey("nt_aberrant_ore");
    public static final ResourceKey<ConfiguredFeature<?,?>> END_ABERRANT_ORE = registerKey("end_aberrant_ore");

    public static void bootstap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        //List of replacement rules. In this case, the ore can replace stone and deepslate (overworld), netherrack (nether) and endstone (end)
        RuleTest stoneReplacement = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES); //Tag which indicates ores that can replace stone
        RuleTest deepslateReplacement = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES); //Tag which indicates ores that can replace deepslate
        RuleTest netherrackReplacement = new BlockMatchTest(Blocks.NETHERRACK); //Tag which indicates blocks that can replace netherrack
        RuleTest endstoneReplacement = new BlockMatchTest(Blocks.END_STONE); //Tag which indicates blocks that can replace endstone

        //In case that more than one ruletest apply for a given dimension, use a list as below:
        List<OreConfiguration.TargetBlockState> overworldAberrantOres = List.of(OreConfiguration.target(stoneReplacement,
                BlockInit.ABERRANT_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplacement, BlockInit.ABERRANT_ORE.get().defaultBlockState()));

        //Register the generation configuration for the overworld, nether and end. The number at the end indicates vein size.
        //Since this block represent an "aberrant" ore, there is no need to make distinct ore textures for different
        //Types of soils (i.e aberrant ore in stone, aberrant ore in deepslate, netherrack, endstone etc)
        register(context, OVERWORLD_ABERRANT_ORE, Feature.ORE, new OreConfiguration(overworldAberrantOres, 5));
        register(context, NETHER_ABERRANT_ORE, Feature.ORE, new OreConfiguration(netherrackReplacement, BlockInit.ABERRANT_ORE.get().defaultBlockState(), 8));
        register(context, END_ABERRANT_ORE, Feature.ORE, new OreConfiguration(endstoneReplacement, BlockInit.ABERRANT_ORE.get().defaultBlockState(), 8));
    }

    //Creating resource keys for the configured features
    public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(MOD_ID, name));
    }

    //Registering with bootstrap context
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
