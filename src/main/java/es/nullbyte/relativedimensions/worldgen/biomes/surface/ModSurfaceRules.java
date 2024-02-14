package es.nullbyte.relativedimensions.worldgen.biomes.surface;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.worldgen.biomes.ModBiomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource ABERRANT_DIRT = makeStateRule(BlockInit.ABERRANT_DIRT.get());
    private static final SurfaceRules.RuleSource ABERRANT_GRASS_BLOCK = makeStateRule(BlockInit.ABERRANT_GRASS.get());
    private static final SurfaceRules.RuleSource ABERRANT_MINERALOID = makeStateRule(BlockInit.ABERRANT_MINERALOID.get());
    private static final SurfaceRules.RuleSource ABERRANT_ORE = makeStateRule(BlockInit.ABERRANT_ORE.get());

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);

        SurfaceRules.RuleSource grassSurface = SurfaceRules.sequence(SurfaceRules.ifTrue(isAtOrAboveWaterLevel, ABERRANT_GRASS_BLOCK), ABERRANT_DIRT);

        SurfaceRules.RuleSource stoneRule = SurfaceRules.state(Blocks.STONE.defaultBlockState());

        return SurfaceRules.sequence(
                SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.ABERRANT_FOREST),
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, ABERRANT_MINERALOID)),
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, ABERRANT_ORE)),

                SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.ABERRANT_PLAINS),
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, ABERRANT_MINERALOID)),
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, ABERRANT_ORE)),


                // Default to a grass and dirt surface
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grassSurface),

                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, GRASS_BLOCK)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}