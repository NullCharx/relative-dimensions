package es.nullbyte.charmiscmods.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;


public class AVIDtsrail extends Block {
    // Define properties for the block here.
    // For example, you could use the BlockStateProperties class to define properties for the block's direction, like so:
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AVIDtsrail(Properties properties) {
        super(properties);
        // Set the default state of the block here.
        // For example, you could set the default facing direction to be north:
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
