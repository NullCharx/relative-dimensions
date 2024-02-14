package es.nullbyte.relativedimensions.worldgen.dimensions;

import com.mojang.datafixers.util.Pair;
import es.nullbyte.relativedimensions.worldgen.biomes.ModBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.List;
import java.util.OptionalLong;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;
import static net.minecraft.world.level.biome.Climate.Parameter.span;

//misode.github.io/dimension
public class ModDimensions {

    public static final ResourceKey<LevelStem> ABERRANT_OVERWORLD_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(MOD_ID, "aberrant_overworld")); // The key for the dimension
    public static final ResourceKey<Level> ABERRANT_OVERWORLD_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(MOD_ID, "aberrant_overworld"));
    public static final ResourceKey<DimensionType> ABERRANT_OVERWORLD_DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(MOD_ID, "aberrant_overworld_type"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        //Copy of regular overworld. Remember that since 1.18, the overworld has new height limits. and y limits.
        context.register(ABERRANT_OVERWORLD_DIMENSION_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                true, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                true, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                -64, // minY
                384, // height
                384, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0,7), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        //MultiNoiseBiomeSource.createFromPreset(reference), holder1);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.ABERRANT_FOREST)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        utilityClass.customOverworldDefaults(biomeRegistry)
                ),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.ABERRANT_OVERWORLD_DIMENSION_TYPE), noiseBasedChunkGenerator);

        context.register(ABERRANT_OVERWORLD_KEY, stem);
    }



}
