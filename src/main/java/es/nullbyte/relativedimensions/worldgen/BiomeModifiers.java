package es.nullbyte.relativedimensions.worldgen;

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

public class BiomeModifiers {
    //Needed by forge to apply generation rules to biomes.

    public static final ResourceKey<BiomeModifier> ADD_OW_ABERRANT_ORE = registerKey("add_ow_aberrant_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETH_ABERRANT_ORE = registerKey("add_neth_aberrant_ore");
    public static final ResourceKey<BiomeModifier> ADD_END_ABERRANT_ORE = registerKey("add_end_aberrant_ore");

    public static void bootstap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        //Add features to biomes
        //Biomes.isOverworld is a tag that contains all biomes that are in the overworld
        context.register(ADD_OW_ABERRANT_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatures.OW_ABERRANT_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_NETH_ABERRANT_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatures.NETH_ABERRANT_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_END_ABERRANT_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatures.OW_ABERRANT_ORE_PLACED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));


    }
    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS , new ResourceLocation(MOD_ID, name));
    }
}
