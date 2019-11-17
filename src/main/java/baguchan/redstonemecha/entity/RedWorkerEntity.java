package baguchan.redstonemecha.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RedWorkerEntity extends MechaBaseEntity {
    public RedWorkerEntity(EntityType<? extends RedWorkerEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;
    }


    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0d);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.295d);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.0D);
    }

    protected float getJumpUpwardsMotion() {
        return 0.5F;
    }

    public double getMountedYOffset() {
        return this.getHeight() * 0.5;
    }

    @Override
    public void travel(Vec3d vector) {
        if (this.isAlive()) {

            if (this.isBeingRidden() && this.canBeSteered()) {
                LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
                this.rotationYaw = livingentity.rotationYaw;
                this.prevRotationYaw = this.rotationYaw;
                this.rotationPitch = livingentity.rotationPitch * 0.5F;
                this.setRotation(this.rotationYaw, this.rotationPitch);
                this.renderYawOffset = this.rotationYaw;
                this.rotationYawHead = this.renderYawOffset;
                float strafe = livingentity.moveStrafing * 0.5F;
                float forward = livingentity.moveForward;
                if (forward <= 0.0F) {
                    forward *= 0.25F;
                }

                if (this.onGround) {
                    strafe = livingentity.moveStrafing / 3F;

                    forward = livingentity.moveForward / 2F;
                }

                this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;
                if (this.canPassengerSteer()) {
                    this.setAIMoveSpeed((float) this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() * 1.0F);

                    super.travel(new Vec3d((double) strafe, vector.y, getAIMoveSpeed() * forward * 2.0F));
                } else if (livingentity instanceof PlayerEntity) {
                    this.setMotion(Vec3d.ZERO);
                }

            } else {
                this.jumpMovementFactor = 0.02F;
                super.travel(vector);
            }
        }
    }
}
