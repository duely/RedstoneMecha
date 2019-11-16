package baguchan.redstonemecha.init;

import baguchan.redstonemecha.api.IGearType;
import baguchan.redstonemecha.gear.DispenserGear;
import baguchan.redstonemecha.gear.MechaGearType;
import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class GearTypeRegister {
    public static Map<Item, IGearType> gearType = Maps.newHashMap();
    public static final MechaGearType DISPENCER = new DispenserGear();

    public static void registerGearType(){
        gearType.put(Item.getItemFromBlock(Blocks.DISPENSER), DISPENCER);
    }

    public static IGearType getGear(ItemStack stack) {
        IGearType gear = gearType.get(stack.getItem());

        if (gear == null) {
            return null;
        } else {
            return gear;
        }
    }
}
