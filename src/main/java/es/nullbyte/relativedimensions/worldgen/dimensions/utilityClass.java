package es.nullbyte.relativedimensions.worldgen.dimensions;

import com.mojang.datafixers.util.Pair;
import es.nullbyte.relativedimensions.worldgen.biomes.ModBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.List;
import com.mojang.datafixers.util.Pair;


public class utilityClass {

    public static Climate.ParameterList<Holder<Biome>> customOverworldDefaults(HolderGetter<Biome> biomeRegistry) {
        return new Climate.ParameterList<>(List.of(
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15F, 0.2F), Climate.Parameter.span(0.3F, 1F), Climate.Parameter.span(-0.11F, 1F), Climate.Parameter.span(-1F, -0.7799F), Climate.Parameter.span(-1F, -0.9333F), Climate.Parameter.point(0F), 0F), biomeRegistry.getOrThrow(Biomes.GROVE)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15F, 0.2F), Climate.Parameter.span(0.3F, 1F), Climate.Parameter.span(-0.11F, 1F), Climate.Parameter.span(-1F, -0.7799F), Climate.Parameter.span(-1F, -0.9333F), Climate.Parameter.point(1F), 0F), biomeRegistry.getOrThrow(Biomes.GROVE)),
                Pair.of(Climate.parameters(Climate.Parameter.span(-0.15F, 0.2F), Climate.Parameter.span(0.3F, 1F), Climate.Parameter.span(-0.11F, 1F), Climate.Parameter.span(-1F, -0.7799F), Climate.Parameter.span(-1F, -0.9333F), Climate.Parameter.point(1F), 0F), biomeRegistry.getOrThrow(ModBiomes.ABERRANT_FOREST)
                )));
    }
}
