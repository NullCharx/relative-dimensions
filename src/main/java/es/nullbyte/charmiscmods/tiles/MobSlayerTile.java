package es.nullbyte.charmiscmods.tiles;

import es.nullbyte.charmiscmods.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MobSlayerTile extends BlockEntity {
    public MobSlayerTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.MOB_SLAYER.get(), pos, state);
    }
}