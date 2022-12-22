package es.nullbyte.charmiscmods.blocks;

import es.nullbyte.charmiscmods.blocks.tsrailaux.RailDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class AVIDtsrail extends Block {

    //Hitboxes depending on the direction of the rail---------------------------------------------------
    public static final VoxelShape HITBOX_NS = Block.box(4.15, 0.0, 0.0, 12.0, 9.0, 16.0);
    public static final VoxelShape HITBOX_EW = Block.box(0.0, 0.0, 4.15, 16.0, 9.0, 12.0);
    //-----------------------------------------------------------------------------------------------

    //Rail direction property, custom rail direction enum found in ./railaux/RailDirection.java--------
    public static final EnumProperty<RailDirection> RAIL_DIRECTION = EnumProperty.create("direction", RailDirection.class);
    //-----------------------------------------------------------------------------------------------

    //Waterlogged property, vanilla property, changes wether the blocm is placed on water or not-----
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    //-----------------------------------------------------------------------------------------------

    public AVIDtsrail(Properties properties) {
        super(properties);
        //Call the super constructor and register the default state of the block. In this case, looking north-south and not waterlogged
        this.registerDefaultState(this.stateDefinition.any().setValue(RAIL_DIRECTION, RailDirection.NORTH_SOUTH));
        this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        //The blocksate defintion detects both direction and waterlog
        builder.add(RAIL_DIRECTION, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        //Get the direction, as well as the blockstate of the block the player is looking at, to check if it is water.
        BlockState state = this.defaultBlockState().setValue(RAIL_DIRECTION, context.getHorizontalDirection().getAxis()
                == Direction.Axis.X ? RailDirection.EAST_WEST : RailDirection.NORTH_SOUTH);
        return context.getLevel().getBlockState(context.getClickedPos()).getMaterial() == Material.WATER ? state.setValue(WATERLOGGED, true) : state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {        // Define the shape of the hitbox here.
        //return a hitbox depending on the direction of the block
        RailDirection railDirection = state.getValue(RAIL_DIRECTION);
        if (railDirection == RailDirection.NORTH_SOUTH) {
            return HITBOX_NS;
        } else if (railDirection == RailDirection.EAST_WEST) {
            return HITBOX_EW;
        } else {
            return HITBOX_NS;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
       //Return water if the block is waterlogged, otherwise return empty fluid
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
