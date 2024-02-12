package es.nullbyte.relativedimensions.worldgen.aberrant.biomes;

import es.nullbyte.relativedimensions.worldgen.aberrant.biomes.custom.ModOverworldRegion;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class TerraBlenderInterface{
    public static void registerRegions(){
        Regions.register(new ModOverworldRegion(new ResourceLocation(MOD_ID, "overworld"), 1));
    }
}
