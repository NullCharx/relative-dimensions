package es.nullbyte.relativedimensions.worldgen.aberrant.biomes.surface;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.worldgen.aberrant.biomes.ModBiomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.NotNull;


//More info on surface riles at https://github.com/TheForsakenFurby/Surface-Rules-Guide-Minecraft-JE-1.18/blob/main/Guide.md
public class ModSurfaceRules {

    private static final SurfaceRules.RuleSource DIRT = makeStateRule(BlockInit.ABERRANT_DIRT.get());
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(BlockInit.ABERRANT_GRASS.get());
    private static final SurfaceRules.RuleSource STONE = makeStateRule(BlockInit.ABERRANT_MINERALOID.get());

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);

        SurfaceRules.RuleSource grassSurface = SurfaceRules.sequence(SurfaceRules.ifTrue(isAtOrAboveWaterLevel, GRASS_BLOCK), DIRT);

        return SurfaceRules.sequence(
                SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.ABERRANT_FOREST),
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, DIRT)), //REPLACE WITH ABERRANT DIRT at floor level
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE)), //REPLACE WITH ABERRANT MINERALOID at cave ceiling level

                SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomes.ABERRANT_PLAINS),
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, DIRT)), //REPLACE WITH ABERRANT DIRT at floor level
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE)), //REPLACE WITH ABERRANT MINERALOID at cave ceiling level


                // Default to a grass and dirt surface
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grassSurface)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(@NotNull Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
