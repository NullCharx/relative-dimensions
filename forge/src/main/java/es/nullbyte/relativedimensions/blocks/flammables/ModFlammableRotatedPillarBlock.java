package es.nullbyte.relativedimensions.blocks.flammables;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;

public class ModFlammableRotatedPillarBlock extends RotatedPillarBlock {
    public ModFlammableRotatedPillarBlock(Properties p_55926_) {
        super(p_55926_);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true; //From fireblock class
    }

    @Override
    public  int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5; //From fireblock class (Flammability)
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5; //From fireblock class (encouragement)
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate) {
        //Axe stripping
        if (context.getItemInHand().getItem() instanceof AxeItem) {
            if(state.is(BlockInit.ABERRANT_LOG.get())) {
                return BlockInit.STRIPPED_ABERRANT_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
            }
        }
        return super.getToolModifiedState(state, context, action, simulate);
    }
}
