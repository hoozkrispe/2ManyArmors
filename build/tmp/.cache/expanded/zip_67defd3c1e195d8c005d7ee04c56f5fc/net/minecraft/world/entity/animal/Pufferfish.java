package net.minecraft.world.entity.animal;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Pufferfish extends AbstractFish {
    private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(Pufferfish.class, EntityDataSerializers.INT);
    int inflateCounter;
    int deflateTimer;
    private static final Predicate<LivingEntity> SCARY_MOB = p_341416_ -> {
        if (p_341416_ instanceof Player player && player.isCreative()) {
            return false;
        }

        return !p_341416_.getType().is(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH);
    };
    static final TargetingConditions targetingConditions = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().selector(SCARY_MOB);
    public static final int STATE_SMALL = 0;
    public static final int STATE_MID = 1;
    public static final int STATE_FULL = 2;

    public Pufferfish(EntityType<? extends Pufferfish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(PUFF_STATE, 0);
    }

    public int getPuffState() {
        return this.entityData.get(PUFF_STATE);
    }

    public void setPuffState(int pPuffState) {
        this.entityData.set(PUFF_STATE, pPuffState);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (PUFF_STATE.equals(pKey)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(pKey);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("PuffState", this.getPuffState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setPuffState(Math.min(pCompound.getInt("PuffState"), 2));
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.PUFFERFISH_BUCKET);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new Pufferfish.PufferfishPuffGoal(this));
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide && this.isAlive() && this.isEffectiveAi()) {
            if (this.inflateCounter > 0) {
                if (this.getPuffState() == 0) {
                    this.makeSound(SoundEvents.PUFFER_FISH_BLOW_UP);
                    this.setPuffState(1);
                } else if (this.inflateCounter > 40 && this.getPuffState() == 1) {
                    this.makeSound(SoundEvents.PUFFER_FISH_BLOW_UP);
                    this.setPuffState(2);
                }

                this.inflateCounter++;
            } else if (this.getPuffState() != 0) {
                if (this.deflateTimer > 60 && this.getPuffState() == 2) {
                    this.makeSound(SoundEvents.PUFFER_FISH_BLOW_OUT);
                    this.setPuffState(1);
                } else if (this.deflateTimer > 100 && this.getPuffState() == 1) {
                    this.makeSound(SoundEvents.PUFFER_FISH_BLOW_OUT);
                    this.setPuffState(0);
                }

                this.deflateTimer++;
            }
        }

        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.getPuffState() > 0) {
            for (Mob mob : this.level().getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(0.3), p_149013_ -> targetingConditions.test(this, p_149013_))) {
                if (mob.isAlive()) {
                    this.touch(mob);
                }
            }
        }
    }

    private void touch(Mob pMob) {
        int i = this.getPuffState();
        if (pMob.hurt(this.damageSources().mobAttack(this), (float)(1 + i))) {
            pMob.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), this);
            this.playSound(SoundEvents.PUFFER_FISH_STING, 1.0F, 1.0F);
        }
    }

    @Override
    public void playerTouch(Player pEntity) {
        int i = this.getPuffState();
        if (pEntity instanceof ServerPlayer && i > 0 && pEntity.hurt(this.damageSources().mobAttack(this), (float)(1 + i))) {
            if (!this.isSilent()) {
                ((ServerPlayer)pEntity).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PUFFER_FISH_STING, 0.0F));
            }

            pEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * i, 0), this);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PUFFER_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PUFFER_FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.PUFFER_FISH_FLOP;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pPose) {
        return super.getDefaultDimensions(pPose).scale(getScale(this.getPuffState()));
    }

    private static float getScale(int pPuffState) {
        switch (pPuffState) {
            case 0:
                return 0.5F;
            case 1:
                return 0.7F;
            default:
                return 1.0F;
        }
    }

    static class PufferfishPuffGoal extends Goal {
        private final Pufferfish fish;

        public PufferfishPuffGoal(Pufferfish pFish) {
            this.fish = pFish;
        }

        @Override
        public boolean canUse() {
            List<LivingEntity> list = this.fish
                .level()
                .getEntitiesOfClass(LivingEntity.class, this.fish.getBoundingBox().inflate(2.0), p_149015_ -> Pufferfish.targetingConditions.test(this.fish, p_149015_));
            return !list.isEmpty();
        }

        @Override
        public void start() {
            this.fish.inflateCounter = 1;
            this.fish.deflateTimer = 0;
        }

        @Override
        public void stop() {
            this.fish.inflateCounter = 0;
        }
    }
}