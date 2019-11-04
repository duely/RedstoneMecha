package baguchan.redstonemecha.container;

import baguchan.redstonemecha.entity.MechaBaseEntity;
import baguchan.redstonemecha.init.MechaContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

public class MechaContainer extends Container {

    protected final Inventory mechaInv;
    protected final MechaBaseEntity mecha;

    public MechaContainer(int id, PlayerInventory playerInventory, MechaBaseEntity mecha) {
        super(MechaContainers.MECHA_INVENTORY, id);
        this.mecha = mecha;
        this.mechaInv = mecha.getInventory();

        for (int i = 0; i < 3; ++i) {
            this.addSlot(new Slot(this.mechaInv, i, 20, 15 + i * 18));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.mecha.isAlive() && this.mecha.getDistance(playerIn) < 8.0F;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            if (index < mechaInv.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, mechaInv.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, mechaInv.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public MechaBaseEntity getMechaEntity() {
        return mecha;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
    }

}