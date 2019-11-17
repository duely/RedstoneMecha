package baguchan.redstonemecha.utils;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtils {

    public static boolean findItem(Inventory inventory, Item item) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i).getItem() == item) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack findItemStack(Inventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return inventory.getStackInSlot(i);
            }
        }
        return null;
    }
}
