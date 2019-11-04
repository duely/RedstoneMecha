package baguchan.redstonemecha.gear;

import baguchan.redstonemecha.entity.MechaBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class DispencerGear extends MechaGearType {
    public DispencerGear(ItemStack itemstack) {
        super(itemstack);
    }

    @Override
    public void onPushSpaceKey(MechaBaseEntity mechaEntity) {

        if(mechaEntity.getCooldown() == 0) {
            if (mechaEntity.getControllingPassenger() instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity) mechaEntity.getControllingPassenger();
                World worldIn = mechaEntity.world;
                boolean flag = playerentity.abilities.isCreativeMode;
                ItemStack itemstack = playerentity.findAmmo(playerentity.getHeldItemMainhand());

                if (!itemstack.isEmpty() || flag) {
                    if (itemstack.isEmpty()) {
                        itemstack = new ItemStack(Items.ARROW);
                    }

                    float f = 0.8F;

                    boolean flag1 = playerentity.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem);
                    if (!worldIn.isRemote) {
                        ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, mechaEntity);
                        abstractarrowentity = customeArrow(abstractarrowentity);
                        abstractarrowentity.shoot(mechaEntity, mechaEntity.rotationPitch, mechaEntity.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            abstractarrowentity.setIsCritical(true);
                        }

                        if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.addEntity(abstractarrowentity);
                    }

                    worldIn.playSound((PlayerEntity) null, playerentity.posX, playerentity.posY, playerentity.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.MUSIC, 1.0F, 1.0F / (worldIn.rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !playerentity.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.deleteStack(itemstack);
                        }
                    }
                }
            }
            mechaEntity.setCooldown(10);
        }
    }

    public AbstractArrowEntity customeArrow(AbstractArrowEntity arrow) {
        return arrow;
    }

    public ItemStack findAmmo(PlayerEntity playerEntity,ItemStack shootable) {
        if (!(shootable.getItem() instanceof ShootableItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getAmmoPredicate();
            ItemStack itemstack = ShootableItem.getHeldAmmo(playerEntity, predicate);
            if (!itemstack.isEmpty()) {
                return itemstack;
            } else {
                predicate = ((ShootableItem)shootable.getItem()).getInventoryAmmoPredicate();

                for(int i = 0; i < playerEntity.inventory.getSizeInventory(); ++i) {
                    ItemStack itemstack1 = playerEntity.inventory.getStackInSlot(i);
                    if (predicate.test(itemstack1)) {
                        return itemstack1;
                    }
                }

                return playerEntity.isCreative() ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }
}
