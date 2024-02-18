package es.nullbyte.relativedimensions.worldgen.biomes.surface;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.worldgen.biomes.ModBiomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public class ModSurfaceRules {
    // Existing custom block rules
    private static final SurfaceRules.RuleSource ABERRANT_GRASS_BLOCK = makeStateRule(BlockInit.ABERRANT_GRASS.get());
    //if specifying an specific blocks, you can use the following method
    //SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
    //Offset, add_surace_depth,surface type
    private static final SurfaceRules.RuleSource ABERRANT_DIRT = makeStateRule(BlockInit.ABERRANT_DIRT.get());

    private static final SurfaceRules.RuleSource ABERRANT_STONE = makeStateRule(BlockInit.ABERRANT_MINERALOID.get()); // Assuming you have ABERRANT_STONE
    private static final SurfaceRules.RuleSource ABERRANT_SNOWY_GRASS = makeStateRule(BlockInit.ABERRANT_SNOWY_GRASS.get()); // For snow coverage

    public static SurfaceRules.RuleSource customMakeRules() {

        return SurfaceRules.sequence(
            SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.ABERRANT_FOREST), SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.temperature(), ABERRANT_SNOWY_GRASS),
                SurfaceRules.ifTrue(ON_FLOOR, ABERRANT_GRASS_BLOCK),
                SurfaceRules.ifTrue(ON_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(0,2),ABERRANT_DIRT)),
                SurfaceRules.ifTrue(UNDER_FLOOR, ABERRANT_DIRT),
                SurfaceRules.ifTrue(yBlockCheck(VerticalAnchor.absolute(18), 2),ABERRANT_STONE))
            ),

            SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.ABERRANT_PLAINS), SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.temperature(), ABERRANT_SNOWY_GRASS),
                SurfaceRules.ifTrue(ON_FLOOR, ABERRANT_GRASS_BLOCK),
                SurfaceRules.ifTrue(ON_FLOOR, SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(0,2),ABERRANT_DIRT)),
                SurfaceRules.ifTrue(UNDER_FLOOR, ABERRANT_DIRT),
                SurfaceRules.ifTrue(yBlockCheck(VerticalAnchor.absolute(18), 2),ABERRANT_STONE))
            )
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}


