package baguchan.redstonemecha.gear;

import baguchan.redstonemecha.api.IGearType;
import baguchan.redstonemecha.entity.MechaBaseEntity;
import net.minecraft.item.ItemStack;


public class MechaGearType implements IGearType {
    private final ItemStack gearItemStack;
    public MechaGearType(ItemStack itemstack) {
        this.gearItemStack = itemstack;
    }

    public ItemStack getGearItemStack() {
        return this.gearItemStack;
    }



    @Override
    public void onPushSpaceKey(MechaBaseEntity mechaEntity) {

    }
}
