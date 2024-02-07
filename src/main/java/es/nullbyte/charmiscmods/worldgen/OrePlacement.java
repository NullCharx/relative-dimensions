package es.nullbyte.charmiscmods.worldgen;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrePlacement {

    //Helper class copied from vanilla OrePlacements, in which they are private
    public static List<PlacementModifier> orePlacement(PlacementModifier pmod1, PlacementModifier pmod2) {
        return List.of(pmod1, InSquarePlacement.spread(), pmod2, BiomeFilter.biome());
    }

    public static List <PlacementModifier> commonOrePlacement(int count, PlacementModifier pHeighRange) {
        return orePlacement(CountPlacement.of(count), pHeighRange);
    }

    public static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier pHeighRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), pHeighRange);
    }
}
