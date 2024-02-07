package es.nullbyte.relativedimensions.datagen;

import es.nullbyte.relativedimensions.worldgen.BiomeModifiers;
import es.nullbyte.relativedimensions.worldgen.ConfiguredFeatures;
import es.nullbyte.relativedimensions.worldgen.PlacedFeatures;
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
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatures::bootstap)
            .add(Registries.PLACED_FEATURE, PlacedFeatures::bootstap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstap);
    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(MOD_ID));
    }
}
