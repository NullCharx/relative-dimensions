package es.nullbyte.relativedimensions.worldgen.biomes.custom;

import com.mojang.datafixers.util.Pair;
import es.nullbyte.relativedimensions.worldgen.biomes.ModBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ModOverworldRegion extends Region {

    public ModOverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint,
                    ResourceKey<Biome>>> mapper) {

        //A certain percentage of the specified biomes will be overriden by the region's biomes
        this.addModifiedVanillaOverworldBiomes(mapper, modifiedVanillaOverworldBuilder -> {
            modifiedVanillaOverworldBuilder.replaceBiome(Biomes.MUSHROOM_FIELDS, ModBiomes.ABERRANT_FOREST);
            modifiedVanillaOverworldBuilder.replaceBiome(Biomes.STONY_SHORE, ModBiomes.ABERRANT_PLAINS);
        });
    }
}
