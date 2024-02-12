package es.nullbyte.relativedimensions.worldgen.aberrant.treegen;

import es.nullbyte.relativedimensions.worldgen.aberrant.ModConfiguredFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public class AberrantTreeGrower {

    //Before the class was abstract, so the way to create a custom tree grower was to extend this class and override the getConfiguredFeature method and
    //couldnt be instantiated.
    //Now the class is the complete opposite: a final class that can be instantitated but cant be extended or inherited.
    //The way to creat a custom tree grower is a new instance of the class (the way its made on TreeGrower.java)
    //And make a getter to said instance.
    private static final TreeGrower GROWER_OF_ABERRANT_TREE = new TreeGrower("aberranttreegrower", 1F, Optional.empty(), Optional.empty(), Optional.of(ModConfiguredFeatures.ABERRANT_TREE_CONFIG),Optional.empty(),Optional.empty(),Optional.empty());
    public AberrantTreeGrower() {
        super();
    }

    protected ResourceKey<ConfiguredFeature<?,?>> getConfiguredFeature(RandomSource pRandom, boolean pHasflower) {
        return ModConfiguredFeatures.ABERRANT_TREE_CONFIG;
    }

    public TreeGrower getGrowerOfAberrantTree() {
        return GROWER_OF_ABERRANT_TREE;
    }
}
