package net.minecraft.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class PressurePlateBlock extends BasePressurePlateBlock {
    public static final MapCodec<PressurePlateBlock> CODEC = RecordCodecBuilder.mapCodec(
        p_310452_ -> p_310452_.group(BlockSetType.CODEC.fieldOf("block_set_type").forGetter(p_313030_ -> p_313030_.type), propertiesCodec())
                .apply(p_310452_, PressurePlateBlock::new)
    );
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    @Override
    public MapCodec<PressurePlateBlock> codec() {
        return CODEC;
    }

    public PressurePlateBlock(BlockSetType p_273284_, BlockBehaviour.Properties p_273571_) {
        super(p_273571_, p_273284_);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
    }

    @Override
    protected int getSignalForState(BlockState pState) {
        return pState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected BlockState setSignalForState(BlockState pState, int pStrength) {
        return pState.setValue(POWERED, Boolean.valueOf(pStrength > 0));
    }

    @Override
    protected int getSignalStrength(Level pLevel, BlockPos pPos) {
        Class<? extends Entity> oclass = switch (this.type.pressurePlateSensitivity()) {
            case EVERYTHING -> Entity.class;
            case MOBS -> LivingEntity.class;
        };
        return getEntityCount(pLevel, TOUCH_AABB.move(pPos), oclass) > 0 ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }
}