package es.nullbyte.charmiscmods.blocks;

import es.nullbyte.charmiscmods.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class MobSlayerBlock extends Block implements EntityBlock {
    public MobSlayerBlock(AbstractGlassBlock.Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.MOB_SLAYER.get().create(pos, state);
    }
}