package es.nullbyte.relativedimensions.worldgen.biomes;

import es.nullbyte.relativedimensions.worldgen.ModPlacedFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModBiomes {
    public static  final ResourceKey<Biome> ABERRANT_FOREST = register("aberrant_forest");
    public static  final ResourceKey<Biome> ABERRANT_PLAINS = register("aberrant_plains");


    public static void bootstap (BootstapContext<Biome> context) {
        // This is a placeholder for the future
        context.register(ABERRANT_FOREST, aberrantForest(context));
        context.register(ABERRANT_PLAINS, aberrantPlains(context));
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder); //Add default carvers and lakes
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);  //Add default crystal formations
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder); //Add default monster room
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder); //Add default underground variety
        BiomeDefaultFeatures.addDefaultSprings(builder); //Add default springs
        BiomeDefaultFeatures.addSurfaceFreezing(builder); //Add surface freezing
    }

    //Aberrant forest biome: Aberrant trees an ores (For now) and probably aberrant dirt and grass parches
    //TODO NOW: Make aberrant grass, dirt.
    //Make config and placed features for aberrant grass and dirt and the aberrant mineraloid (the three only naturally found on this biome)
    public static Biome aberrantForest(BootstapContext<Biome> context) {
        //Spawn settings(Category, Spawnsettings(Entity, Weight, minGroupSize, maxGroupSize))
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        //Custom entity spawn
        //spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities .RHINO.get(), 2, 3, 5));
        //Spawn wolf
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));


        //Default biome featurettes
        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        //add a monster room ("OG Dungeon") (done in the globalOverworldGeneration method)
        //BiomeDefaultFeatures.addDefaultMonsterRoom(biomeBuilder);
        //Add common spawns:
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        //Add common features:
        globalOverworldGeneration(biomeBuilder);
        //ADd mossy stone
        BiomeDefaultFeatures.addMossyStoneBlock(biomeBuilder);

        //Default shrooms, lush caves, and extra vegetation
        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        BiomeDefaultFeatures.addLushCavesSpecialOres(biomeBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);

        //Add custom features for aberrant ores and trees
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.ABERRANT_ORE_PLACED_VANILLA_BIOMES);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.ABERRANT_TREE_PLACED_ABERRANT_BIOMES);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true) //Can rain
                .downfall(0.8f) //Rain level
                .temperature(0.7f) //From 0F(Snow) to 2F(Desert)
                .generationSettings(biomeBuilder.build())  //Pass the biome builder
                .mobSpawnSettings(spawnBuilder.build()) //pass the spawn builder
                .specialEffects((new BiomeSpecialEffects.Builder()) //Pretty self-explanatory names
                        .waterColor(0xd0d0f5)
                        .waterFogColor(0xbf1b26)
                        .skyColor(0x4e32a8)
                        .grassColorOverride(0x32a855)
                        .foliageColorOverride(0xa89232)
                        .fogColor(0x2222e6)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.END).build())
                        //Modded music
                        //.backgroundMusic(Musics.createGameMusic(ModSounds.BAR_BRAWL.getHolder().get())).build())
                .build();
    }

    //Aberrant plains biome: Aberrant ores (For now) abrrant grass all over
    public static Biome aberrantPlains(BootstapContext<Biome> context) {
        //Spawn settings(Category, Spawnsettings(Entity, Weight, minGroupSize, maxGroupSize))
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        //Custom entity spawn
        //spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities .RHINO.get(), 2, 3, 5));
        //Spawn wolf
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));


        //Default biome featurettes
        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        //add a monster room ("OG Dungeon") (done in the globalOverworldGeneration method)
        //BiomeDefaultFeatures.addDefaultMonsterRoom(biomeBuilder);
        //Add common spawns:
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        //Add common features:
        globalOverworldGeneration(biomeBuilder);
        //ADd mossy stone
        BiomeDefaultFeatures.addMossyStoneBlock(biomeBuilder);

        //Default shrooms, lush caves, and extra vegetation
        BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        BiomeDefaultFeatures.addLushCavesSpecialOres(biomeBuilder);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);

        //Add custom features for aberrant ores and trees
        biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.ABERRANT_ORE_PLACED_VANILLA_BIOMES);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(true) //Can rain
                .downfall(0.8f) //Rain level
                .temperature(0.7f) //From 0F(Snow) to 2F(Desert)
                .generationSettings(biomeBuilder.build())  //Pass the biome builder
                .mobSpawnSettings(spawnBuilder.build()) //pass the spawn builder
                .specialEffects((new BiomeSpecialEffects.Builder()) //Pretty self-explanatory names
                        .waterColor(0xd0d0f5)
                        .waterFogColor(0xbf1b26)
                        .skyColor(0x4e32a8)
                        .grassColorOverride(0x32a855)
                        .foliageColorOverride(0xa89232)
                        .fogColor(0x2222e6)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.END).build())
                //Modded music
                //.backgroundMusic(Musics.createGameMusic(ModSounds.BAR_BRAWL.getHolder().get())).build())
                .build();
    }

    public static final ResourceKey<Biome> register(String id) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, id));
    }
}
