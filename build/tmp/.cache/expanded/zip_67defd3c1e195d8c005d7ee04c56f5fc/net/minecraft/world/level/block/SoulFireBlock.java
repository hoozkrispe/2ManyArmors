package net.minecraft.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SoulFireBlock extends BaseFireBlock {
    public static final MapCodec<SoulFireBlock> CODEC = simpleCodec(SoulFireBlock::new);

    @Override
    public MapCodec<SoulFireBlock> codec() {
        return CODEC;
    }

    public SoulFireBlock(BlockBehaviour.Properties p_56653_) {
        super(p_56653_, 2.0F);
    }

    @Override
    protected BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return this.canSurvive(pState, pLevel, pCurrentPos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canSurviveOnBlock(pLevel.getBlockState(pPos.below()));
    }

    public static boolean canSurviveOnBlock(BlockState pState) {
        return pState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS);
    }

    @Override
    protected boolean canBurn(BlockState pState) {
        return true;
    }
}