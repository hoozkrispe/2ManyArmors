package net.minecraft.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MangrovePropaguleBlock extends SaplingBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<MangrovePropaguleBlock> CODEC = RecordCodecBuilder.mapCodec(
        p_310142_ -> p_310142_.group(TreeGrower.CODEC.fieldOf("tree").forGetter(p_310695_ -> p_310695_.treeGrower), propertiesCodec())
                .apply(p_310142_, MangrovePropaguleBlock::new)
    );
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    public static final int MAX_AGE = 4;
    private static final VoxelShape[] SHAPE_PER_AGE = new VoxelShape[]{
        Block.box(7.0, 13.0, 7.0, 9.0, 16.0, 9.0),
        Block.box(7.0, 10.0, 7.0, 9.0, 16.0, 9.0),
        Block.box(7.0, 7.0, 7.0, 9.0, 16.0, 9.0),
        Block.box(7.0, 3.0, 7.0, 9.0, 16.0, 9.0),
        Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0)
    };
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    @Override
    public MapCodec<MangrovePropaguleBlock> codec() {
        return CODEC;
    }

    public MangrovePropaguleBlock(TreeGrower p_312632_, BlockBehaviour.Properties p_221449_) {
        super(p_312632_, p_221449_);
        this.registerDefaultState(
            this.stateDefinition
                .any()
                .setValue(STAGE, Integer.valueOf(0))
                .setValue(AGE, Integer.valueOf(0))
                .setValue(WATERLOGGED, Boolean.valueOf(false))
                .setValue(HANGING, Boolean.valueOf(false))
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STAGE).add(AGE).add(WATERLOGGED).add(HANGING);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(Blocks.CLAY);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return super.getStateForPlacement(pContext).setValue(WATERLOGGED, Boolean.valueOf(flag)).setValue(AGE, Integer.valueOf(4));
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Vec3 vec3 = pState.getOffset(pLevel, pPos);
        VoxelShape voxelshape;
        if (!pState.getValue(HANGING)) {
            voxelshape = SHAPE_PER_AGE[4];
        } else {
            voxelshape = SHAPE_PER_AGE[pState.getValue(AGE)];
        }

        return voxelshape.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return isHanging(pState) ? pLevel.getBlockState(pPos.above()).is(Blocks.MANGROVE_LEAVES) : super.canSurvive(pState, pLevel, pPos);
    }

    @Override
    protected BlockState updateShape(
        BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos
    ) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return pFacing == Direction.UP && !pState.canSurvive(pLevel, pCurrentPos)
            ? Blocks.AIR.defaultBlockState()
            : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!isHanging(pState)) {
            if (pRandom.nextInt(7) == 0) {
                this.advanceTree(pLevel, pPos, pState, pRandom);
            }
        } else {
            if (!isFullyGrown(pState)) {
                pLevel.setBlock(pPos, pState.cycle(AGE), 2);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        return !isHanging(pState) || !isFullyGrown(pState);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return isHanging(pState) ? !isFullyGrown(pState) : super.isBonemealSuccess(pLevel, pRandom, pPos, pState);
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        if (isHanging(pState) && !isFullyGrown(pState)) {
            pLevel.setBlock(pPos, pState.cycle(AGE), 2);
        } else {
            super.performBonemeal(pLevel, pRandom, pPos, pState);
        }
    }

    private static boolean isHanging(BlockState pState) {
        return pState.getValue(HANGING);
    }

    private static boolean isFullyGrown(BlockState pState) {
        return pState.getValue(AGE) == 4;
    }

    public static BlockState createNewHangingPropagule() {
        return createNewHangingPropagule(0);
    }

    public static BlockState createNewHangingPropagule(int pAge) {
        return Blocks.MANGROVE_PROPAGULE.defaultBlockState().setValue(HANGING, Boolean.valueOf(true)).setValue(AGE, Integer.valueOf(pAge));
    }
}