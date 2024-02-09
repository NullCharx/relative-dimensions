package es.nullbyte.relativedimensions.blocks.flammables;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;

public class ModFlammableRotatedPlank extends RotatedPillarBlock {
    public ModFlammableRotatedPlank(Properties p_55926_) {
        super(p_55926_);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true; //From fireblock class
    }

    @Override
    public  int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 20; //From fireblock class (Flammability)
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 20; //From fireblock class (encouragement)
    }

}
