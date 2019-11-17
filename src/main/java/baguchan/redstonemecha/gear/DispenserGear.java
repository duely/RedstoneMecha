package baguchan.redstonemecha.gear;

import baguchan.redstonemecha.entity.MechaBaseEntity;
import baguchan.redstonemecha.utils.InventoryUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class DispenserGear extends MechaGearType {
    public DispenserGear() {
        super();
    }

    @Override
    public void onPushActionKey(MechaBaseEntity mechaEntity) {
        if (mechaEntity.getCooldown() <= 3) {
            if (mechaEntity.getControllingPassenger() instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity) mechaEntity.getControllingPassenger();
                World worldIn = mechaEntity.world;
                boolean flag = playerentity.abilities.isCreativeMode;
                ItemStack itemstack = InventoryUtils.findItemStack(mechaEntity.getInventory());

                if (!itemstack.isEmpty() && itemstack.getItem() instanceof ArrowItem || flag) {
                    if (itemstack.isEmpty()) {
                        itemstack = new ItemStack(Items.ARROW);
                    }

                    float f = 0.85F;

                    boolean flag1 = playerentity.abilities.isCreativeMode;
                    if (!worldIn.isRemote) {
                        if (mechaEntity.getInventory().getStackInSlot(1).getItem() == Item.getItemFromBlock(Blocks.DISPENSER) && EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, mechaEntity.getInventory().getStackInSlot(1)) > 0) {
                            for (int i = 0; i < 2; i++) {
                                int angle = 0;

                                if (i % 2 == 1) {
                                    angle = 5 + 5 * i;
                                } else {
                                    angle = -5 - 5 * i;
                                }

                                Vec3d vec3d1 = mechaEntity.func_213286_i(1.0F);
                                Quaternion quaternion = new Quaternion(new Vector3f(vec3d1), angle, true);
                                Vec3d vec3d = mechaEntity.getLook(1.0F);
                                Vector3f vector3f = new Vector3f(vec3d);
                                vector3f.func_214905_a(quaternion);

                                ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                                AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, mechaEntity);
                                abstractarrowentity = customeArrow(abstractarrowentity);
                                abstractarrowentity.shoot((double) vector3f.getX(), (double) vector3f.getY(), (double) vector3f.getZ(), f * 3.0F, 1.0F);
                                if (f == 1.0F) {
                                    abstractarrowentity.setIsCritical(true);
                                }

                                if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                                    abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                                } else {
                                    abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
                                }


                                worldIn.addEntity(abstractarrowentity);
                            }
                        } else {
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
                    }

                    worldIn.playSound((PlayerEntity) null, playerentity.posX, playerentity.posY, playerentity.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.MUSIC, 1.0F, 1.0F / (worldIn.rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !playerentity.abilities.isCreativeMode) {
                        mechaEntity.getInventory().getStackInSlot(0).shrink(1);
                    }
                }
            }
            mechaEntity.setCooldown(10);
        }
    }

    public AbstractArrowEntity customeArrow(AbstractArrowEntity arrow) {
        return arrow;
    }

    public ItemStack findAmmo(PlayerEntity playerEntity, ItemStack shootable) {
        if (!(shootable.getItem() instanceof ShootableItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ShootableItem) shootable.getItem()).getAmmoPredicate();
            ItemStack itemstack = ShootableItem.getHeldAmmo(playerEntity, predicate);
            if (!itemstack.isEmpty()) {
                return itemstack;
            } else {
                predicate = ((ShootableItem) shootable.getItem()).getInventoryAmmoPredicate();

                for (int i = 0; i < playerEntity.inventory.getSizeInventory(); ++i) {
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
