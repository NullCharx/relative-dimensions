package es.nullbyte.relativedimensions.worldgen.aberrant;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

/**
 * Biome modifiers are used to modify biomes. For example, adding features to biomes, or changing the temperature of a biome.
 * This class is used to register the biome modifiers that will be used in the mod.
 */
public class ModBiomeModifier {

    public static final ResourceKey<BiomeModifier> ADD_OW_ABERRANT_TREE = registerKey("add_ow_aberrant_tree");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_ABERRANT_TREE = registerKey("add_nether_aberrant_tree");
    public static final ResourceKey<BiomeModifier> ADD_END_ABERRANT_TREE = registerKey("add_end_aberrant_tree");


    public static final ResourceKey<BiomeModifier> ADD_OW_ABERRANT_ORE = registerKey("add_ow_aberrant_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_ABERRANT_ORE = registerKey("add_nether_aberrant_ore");
    public static final ResourceKey<BiomeModifier> ADD_END_ABERRANT_ORE = registerKey("add_end_aberrant_ore");

    public static void bootstap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        //Add features to biomes
        //for example Biomes.isOverworld is a tag that contains all biomes that are in the overworld
        //--ABERRANT TREE GENERATION--Since we want the same tree gen config with the same chance on all dimensions,
        // we can use the same placed and config feature for all dimensions
        context.register(ADD_OW_ABERRANT_TREE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ABERRANT_TREE_PLACED_VANILLA_BIOMES)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_NETHER_ABERRANT_TREE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ABERRANT_TREE_PLACED_VANILLA_BIOMES)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_END_ABERRANT_TREE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ABERRANT_TREE_PLACED_VANILLA_BIOMES)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        //--ABERRANT ORE GENERATION-- Since we want the same ore replacables with the same chance on all dimensions,
        // we can use the same placed and config feature for all dimensions
        context.register(ADD_OW_ABERRANT_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ABERRANT_ORE_PLACED_VANILLA_BIOMES)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_NETHER_ABERRANT_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ABERRANT_ORE_PLACED_VANILLA_BIOMES)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_END_ABERRANT_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ABERRANT_ORE_PLACED_VANILLA_BIOMES)),
                GenerationStep.Decoration.UNDERGROUND_ORES));


    }
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        ResourceKey<BiomeModifier> key = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS , new ResourceLocation(MOD_ID, name));
        return key;
    }
}
