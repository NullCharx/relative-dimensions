package es.nullbyte.relativedimensions.blocks.flammables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ModFlammableRotatedLeave extends LeavesBlock {
    public ModFlammableRotatedLeave(Properties p_55926_) {
        super(p_55926_);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true; //From fireblock class
    }

    @Override
    public  int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60; //From fireblock class (Flammability)
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30; //From fireblock class (encouragement)
    }

}
