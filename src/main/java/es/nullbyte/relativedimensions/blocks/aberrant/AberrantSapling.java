package es.nullbyte.relativedimensions.blocks.aberrant;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;

public class AberrantSapling extends SaplingBlock {
    public AberrantSapling(TreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties.emissiveRendering((state, world, pos) -> true)
                .lightLevel((light) -> 1));
    }
}
