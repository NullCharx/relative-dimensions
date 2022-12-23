package es.nullbyte.charmiscmods.blocks.tsrailaux;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import es.nullbyte.charmiscmods.blocks.tsrailaux.tsRailState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBaseRailBlock;

public abstract class tsRailBase extends Block implements ICustomRail {
    //Hitboxes depending on the direction of the rail---------------------------------------------------
    public static final VoxelShape HITBOX_NS = Block.box(4.15, 0.0, 0.0, 12.0, 9.0, 16.0);
    public static final VoxelShape HITBOX_EW = Block.box(0.0, 0.0, 4.15, 16.0, 9.0, 12.0);
    protected static final VoxelShape HALF_BLOCK_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    //-----------------------------------------------------------------------------------------------
    //Waterlogged property, vanilla property, changes wether the blocm is placed on water or not-----
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    //-----------------------------------------------------------------------------------------------
    private final boolean isStraight;

    public static boolean isRail(Level world, BlockPos position) {
        return isRail(world.getBlockState(position));
    }

    public static boolean isRail(BlockState state) {
        //TODO El codigo original mira tags. Se añade medienate un json en la carpeta data.minecraft.tags.blocks/rails
        return state.getBlock() instanceof BaseRailBlock;
    }

    public boolean isStraight() {
        return this.isStraight;
    }

    public tsRailBase(Block.Properties properties, Boolean isStraight) {
        super(properties);
        this.isStraight = isStraight;
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGET, BlockPos posotion, CollisionContext context) {
        RailDirection railshape = state.is(this) ? state.getValue(this.getShapeProperty()) : null;
        RailDirection railShape2 = state.is(this) ? getRailDirection(state, blockGET, posotion, null) : null;
        return HALF_BLOCK_AABB; //Todo custom hitbox

    }

    public boolean canSurvive(BlockState state, LevelReader p_49396_, BlockPos position) {
        return true;
    }

    public void onPlace(BlockState state, Level world, BlockPos position, BlockState state2, boolean p_49412_) {
        if (!state2.is(state.getBlock())) {
            this.updateState(state, world, position, p_49412_);
        }
    }

    protected BlockState updateState(BlockState state, Level world, BlockPos position, boolean p_49393_) {
        state = this.updateDir(world, position, state, true);
        if (this.isStraight) {
            world.neighborChanged(state, position, this, position, p_49393_);
        }
        return state;
    }

    public void neighborChanged(BlockState state, Level world, BlockPos position, Block block2, BlockPos position2, boolean p_49382_) {
        if (!world.isClientSide && world.getBlockState(position).is(this)) {
            RailDirection railshape = getRailDirection(state, world, position, null);
            if (shouldBeRemoved(position, world, railshape)) {
                dropResources(state, world, position);
                world.removeBlock(position, p_49382_);
            } else {
                this.updateState(state, world, position, block2);
            }

        }
    }

    private static boolean shouldBeRemoved(BlockPos position, Level world, RailDirection shape) {
        //If the block below cant support rigid block and is not air, remove the rail.
        //This allows the rial to obey regular rail rules while still being able to be placed on top of air (hanging)
        if (!canSupportRigidBlock(world, position.below())&& !world.isEmptyBlock(position.below())) {
            return true;
        } else {
            switch (shape) {
                case ASCENDING_EAST:
                    return !canSupportRigidBlock(world, position.east());
                case ASCENDING_WEST:
                    return !canSupportRigidBlock(world, position.west());
                case ASCENDING_NORTH:
                    return !canSupportRigidBlock(world, position.north());
                case ASCENDING_SOUTH:
                    return !canSupportRigidBlock(world, position.south());
                default:
                    return false;
            }
        }
    }

    protected void updateState(BlockState state, Level world, BlockPos position, Block block) {
    }

    protected BlockState updateDir(Level world, BlockPos position, BlockState state, boolean p_49371_) {
        if (world.isClientSide) {
            return state;
        } else {
            RailDirection railshape = state.getValue(this.getShapeProperty());
            return (new tsRailState(world, position, state)).place(world.hasNeighborSignal(position), p_49371_, railshape).getState();
        }
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    public void onRemove(BlockState state, Level world, BlockPos position, BlockState state2, boolean p_49388_) {
        if (!p_49388_) {
            super.onRemove(state, world, position, state2, p_49388_);
            if (getRailDirection(state, world, position, null).isAscending()) {
                world.updateNeighborsAt(position.above(), this);
            }

            if (this.isStraight) {
                world.updateNeighborsAt(position, this);
                world.updateNeighborsAt(position.below(), this);
            }

        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        BlockState blockstate = super.defaultBlockState();
        Direction direction = context.getHorizontalDirection();
        boolean flag1 = direction == Direction.EAST || direction == Direction.WEST;
        return blockstate.setValue(this.getShapeProperty(), flag1 ? RailDirection.EAST_WEST : RailDirection.NORTH_SOUTH).setValue(WATERLOGGED, Boolean.valueOf(flag));
    }

    @Deprecated //Forge: Use getRailDirection(IBlockAccess, BlockPos, IBlockState, EntityMinecart) for enhanced ability
    public abstract Property<RailDirection> getShapeProperty();

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor worldAccess, BlockPos position, BlockPos position2) {
        if (state.getValue(WATERLOGGED)) {
            worldAccess.scheduleTick(position, Fluids.WATER, Fluids.WATER.getTickDelay(worldAccess));
        }

        return super.updateShape(state, direction, state2, worldAccess, position, position2);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }


    /* ======================================== FORGE START =====================================*/
    public boolean isFlexibleRail(BlockState state, BlockGetter world, BlockPos pos)
    {
        return  !this.isStraight;
    }

    @Override
    public RailDirection getRailDirection(BlockState state, BlockGetter world, BlockPos pos, @org.jetbrains.annotations.Nullable net.minecraft.world.entity.vehicle.AbstractMinecart cart) {
        return state.getValue(getShapeProperty());
    }

    /* ========================================= FORGE END ======================================*/
}



