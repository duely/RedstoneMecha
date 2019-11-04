package baguchan.redstonemecha.entity;

import baguchan.redstonemecha.init.GearTypeRegister;
import baguchan.redstonemecha.network.MechaPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class RedWorkerEntity extends MechaBaseEntity {
    public RedWorkerEntity(EntityType<? extends RedWorkerEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;
    }


    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0d);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.285d);
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
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if (super.processInteract(player, hand)) {
            return true;
        } else if (player.isSneaking()) {
            return false;
        } else if (this.isBeingRidden()) {
            return true;
        } else {
            if (!this.world.isRemote) {
                this.mountTo(player);
            }
            return true;
        }
    }

    protected void mountTo(PlayerEntity player) {
        if (!this.world.isRemote) {
            player.rotationYaw = this.rotationYaw;
            player.rotationPitch = this.rotationPitch;
            player.startRiding(this);
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);

        if (passenger instanceof MobEntity) {
            MobEntity mobentity = (MobEntity) passenger;
            this.renderYawOffset = mobentity.renderYawOffset;
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public boolean canBePushed() {
        return !this.isBeingRidden();
    }

    @Override
    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof PlayerEntity;
    }

    public boolean isRidingPlayer(PlayerEntity player) {
        return this.getControllingPassenger() != null && this.getControllingPassenger() instanceof PlayerEntity && this.getControllingPassenger().getUniqueID().equals(player.getUniqueID());
    }

    @Override
    public void tick() {
        super.tick();

        if (world.isRemote) {
            this.updateClientControls();
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getInstance();

        if (this.isRidingPlayer(mc.player)) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                MechaPacketHandler.jumpStart(this);
            }
        }
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
