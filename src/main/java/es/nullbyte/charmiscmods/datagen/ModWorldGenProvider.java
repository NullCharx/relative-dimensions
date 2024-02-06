package es.nullbyte.charmiscmods.datagen;

import es.nullbyte.charmiscmods.worldgen.BiomeModifiers;
import es.nullbyte.charmiscmods.worldgen.ConfiguredFeatures;
import es.nullbyte.charmiscmods.worldgen.PlacedFeatures;
import net.minecraft.core.HolderLookup;
import es.nullbyte.charmiscmods.datagen.tempaux.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import es.nullbyte.charmiscmods.datagen.tempaux.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatures::bootstap)
            .add(Registries.PLACED_FEATURE, PlacedFeatures::bootstap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiers::bootstap);
    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(MOD_ID));
    }
}
