package net.minecraft.world.entity.animal.horse;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class SkeletonHorse extends AbstractHorse {
    private final SkeletonTrapGoal skeletonTrapGoal = new SkeletonTrapGoal(this);
    private static final int TRAP_MAX_LIFE = 18000;
    private static final EntityDimensions BABY_DIMENSIONS = EntityType.SKELETON_HORSE
        .getDimensions()
        .withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, 0.0F, EntityType.SKELETON_HORSE.getHeight() - 0.03125F, 0.0F))
        .scale(0.5F);
    private boolean isTrap;
    private int trapTime;

    public SkeletonHorse(EntityType<? extends SkeletonHorse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    public static boolean checkSkeletonHorseSpawnRules(
        EntityType<? extends Animal> pAnimal, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom
    ) {
        return !MobSpawnType.isSpawner(pSpawnType)
            ? Animal.checkAnimalSpawnRules(pAnimal, pLevel, pSpawnType, pPos, pRandom)
            : MobSpawnType.ignoresLightRequirements(pSpawnType) || isBrightEnoughToSpawn(pLevel, pPos);
    }

    @Override
    protected void randomizeAttributes(RandomSource pRandom) {
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateJumpStrength(pRandom::nextDouble));
    }

    @Override
    protected void addBehaviourGoals() {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isEyeInFluid(FluidTags.WATER) ? SoundEvents.SKELETON_HORSE_AMBIENT_WATER : SoundEvents.SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SKELETON_HORSE_HURT;
    }

    @Override
    protected SoundEvent getSwimSound() {
        if (this.onGround()) {
            if (!this.isVehicle()) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }

            this.gallopSoundCounter++;
            if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                return SoundEvents.SKELETON_HORSE_GALLOP_WATER;
            }

            if (this.gallopSoundCounter <= 5) {
                return SoundEvents.SKELETON_HORSE_STEP_WATER;
            }
        }

        return SoundEvents.SKELETON_HORSE_SWIM;
    }

    @Override
    protected void playSwimSound(float pVolume) {
        if (this.onGround()) {
            super.playSwimSound(0.3F);
        } else {
            super.playSwimSound(Math.min(0.1F, pVolume * 25.0F));
        }
    }

    @Override
    protected void playJumpSound() {
        if (this.isInWater()) {
            this.playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
        } else {
            super.playJumpSound();
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pPose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pPose);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isTrap() && this.trapTime++ >= 18000) {
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("SkeletonTrap", this.isTrap());
        pCompound.putInt("SkeletonTrapTime", this.trapTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setTrap(pCompound.getBoolean("SkeletonTrap"));
        this.trapTime = pCompound.getInt("SkeletonTrapTime");
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.96F;
    }

    public boolean isTrap() {
        return this.isTrap;
    }

    public void setTrap(boolean pIsTrap) {
        if (pIsTrap != this.isTrap) {
            this.isTrap = pIsTrap;
            if (pIsTrap) {
                this.goalSelector.addGoal(1, this.skeletonTrapGoal);
            } else {
                this.goalSelector.removeGoal(this.skeletonTrapGoal);
            }
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return EntityType.SKELETON_HORSE.create(pLevel);
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        return !this.isTamed() ? InteractionResult.PASS : super.mobInteract(pPlayer, pHand);
    }
}