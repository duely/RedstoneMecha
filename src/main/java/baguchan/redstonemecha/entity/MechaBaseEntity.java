package baguchan.redstonemecha.entity;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.container.MechaContainer;
import baguchan.redstonemecha.network.MechaPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class MechaBaseEntity extends MobEntity{
    private final Inventory inventory = new Inventory(3);
    private int cooldown;

    protected MechaBaseEntity(EntityType<? extends MechaBaseEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerData() {
        super.registerData();
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        ListNBT listnbt = new ListNBT();

        for(int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.write(new CompoundNBT()));
            }
        }

        compound.put("Inventory", listnbt);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        ListNBT listnbt = compound.getList("Inventory", 10);

        for(int i = 0; i < listnbt.size(); ++i) {
            ItemStack itemstack = ItemStack.read(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.inventory.addItem(itemstack);
            }
        }
    }



    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.cooldown > 0){
            --this.cooldown;
        }

        if (world.isRemote) {
            this.updateClientControls();
        }
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if (super.processInteract(player, hand)) {
            return true;
        } else if (player.isSneaking()) {
            if (player instanceof ServerPlayerEntity && !(player instanceof FakePlayer)) {
                if (!player.world.isRemote) {
                    ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity) player;
                    NetworkHooks.openGui(entityPlayerMP, new INamedContainerProvider() {
                        @Override
                        public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
                            return new MechaContainer(windowId, inventory, (MechaBaseEntity) player.world.getEntityByID(getEntityId()));
                        }

                        @Override
                        public ITextComponent getDisplayName() {
                            return new TranslationTextComponent("container.redstonemecha.mecha_inventory");
                        }
                    }, buf -> {
                        buf.writeInt(this.getEntityId());
                    });
                }
            }

            this.playSound(SoundEvents.BLOCK_IRON_DOOR_OPEN, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            return true;
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

    @OnlyIn(Dist.CLIENT)
    protected void updateClientControls() {
        Minecraft mc = Minecraft.getInstance();

        if (this.isRidingPlayer(mc.player)) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                MechaPacketHandler.pushSpaceStart(this);
            }

            if (RedstoneMechaCore.instance.keyBindAction0.isKeyDown()) {
                MechaPacketHandler.pushActionStart(this, 0);
            }

            if (RedstoneMechaCore.instance.keyBindAction1.isKeyDown()) {
                MechaPacketHandler.pushActionStart(this, 1);
            }

            if (RedstoneMechaCore.instance.keyBindAction2.isKeyDown()) {
                MechaPacketHandler.pushActionStart(this, 2);
            }
        }
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return null;
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval() {
        return 120;
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

}
