package es.nullbyte.charmiscmods.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class AVIDtsrail extends Block {
    // Define properties for the block here.
    // For example, you could use the BlockStateProperties class to define properties for the block's direction, like so:
    public static final VoxelShape HITBOX_NS = Block.box(4.15, 0.0, 0.0, 12.0, 9.0, 16.0);
    public static final VoxelShape HITBOX_EW = Block.box(0.0, 0.0, 4.15, 16.0, 9.0, 12.0);

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


    // Override the getShape method to define the shape of the hitbox.

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {        // Define the shape of the hitbox here.
        Direction direction = state.getValue(FACING);
        switch (direction) {
            case NORTH:
                return HITBOX_NS;
            case SOUTH:
                return HITBOX_NS;
            case EAST:
                return HITBOX_EW;
            case WEST:
                return HITBOX_EW;
            default:
                return HITBOX_NS;
        }
    }
}
