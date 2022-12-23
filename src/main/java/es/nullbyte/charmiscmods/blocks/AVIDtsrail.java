package es.nullbyte.charmiscmods.blocks;

import es.nullbyte.charmiscmods.blocks.tsrailaux.RailDirection;
import es.nullbyte.charmiscmods.blocks.tsrailaux.tsRailBase;
import es.nullbyte.charmiscmods.blocks.tsrailaux.tsRailState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class AVIDtsrail extends tsRailBase {
    //Rail direction property, custom rail direction enum found in ./railaux/RailDirection.java--------
    public static final EnumProperty<RailDirection> RAIL_DIRECTION = EnumProperty.create("direction", RailDirection.class);
    //-----------------------------------------------------------------------------------------------

    public AVIDtsrail(Properties properties, Boolean isStraight) {
        super(properties, isStraight);
        //Call the super constructor and register the default state of the block. In this case, looking north-south and not waterlogged
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(RAIL_DIRECTION, RailDirection.NORTH_SOUTH)
                .setValue(WATERLOGGED, false));
    }

    protected void updateState(BlockState state, Level world, BlockPos position, Block block) {
        if (block.defaultBlockState().isSignalSource() && (new tsRailState(world, position, state)).countPotentialConnections() == 3) {
            this.updateDir(world, position, state, false);
        }

    }

    @Override
    public Property<RailDirection> getShapeProperty() {
        return RAIL_DIRECTION;
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180:
                switch ((RailDirection)state.getValue(RAIL_DIRECTION)) {
                    case ASCENDING_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_WEST);
                    case NORTH_SOUTH: //Forge fix: MC-196102
                    case EAST_WEST:
                        return state;
                }
            case COUNTERCLOCKWISE_90:
                switch ((RailDirection)state.getValue(RAIL_DIRECTION)) {
                    case ASCENDING_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_WEST);
                    case NORTH_SOUTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch ((RailDirection)state.getValue(RAIL_DIRECTION)) {
                    case ASCENDING_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.EAST_WEST);
                    case EAST_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_SOUTH);
                }
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        RailDirection railshape = state.getValue(RAIL_DIRECTION);
        switch (mirror) {
            case LEFT_RIGHT:
                switch (railshape) {
                    case ASCENDING_NORTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_EAST);
                    case SOUTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_WEST);
                    case NORTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_WEST);
                    case NORTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_EAST);
                    default:
                        return super.mirror(state, mirror);
                }
            case FRONT_BACK:
                switch (railshape) {
                    case ASCENDING_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_WEST);
                    case SOUTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.SOUTH_EAST);
                    case NORTH_WEST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_EAST);
                    case NORTH_EAST:
                        return state.setValue(RAIL_DIRECTION, RailDirection.NORTH_WEST);
                }
        }

        return super.mirror(state, mirror);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        //The blocksate defintion detects both direction and waterlog
        builder.add(RAIL_DIRECTION, WATERLOGGED);
    }



}
