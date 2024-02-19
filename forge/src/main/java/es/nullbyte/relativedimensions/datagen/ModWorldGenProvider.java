package es.nullbyte.relativedimensions.datagen;

import es.nullbyte.relativedimensions.worldgen.ModBiomeModifier;
import es.nullbyte.relativedimensions.worldgen.ModConfiguredFeatures;
import es.nullbyte.relativedimensions.worldgen.ModPlacedFeatures;
import es.nullbyte.relativedimensions.worldgen.biomes.ModBiomes;
import es.nullbyte.relativedimensions.worldgen.dimensions.ModDimensions;
import net.minecraft.core.HolderLookup;
import es.nullbyte.relativedimensions.datagen.tempaux.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import es.nullbyte.relativedimensions.datagen.tempaux.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstap) //Register configured features
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstap) //Register placed features
            .add(Registries.BIOME, ModBiomes::bootstap) //Register biomes
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem) //Register dimensions
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType)//Register dimension types
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifier::bootstap); //Register biome modifiers

    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(MOD_ID));
    }
}
