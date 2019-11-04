package baguchan.redstonemecha.init;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.gear.DispencerGear;
import baguchan.redstonemecha.gear.MechaGearType;
import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

import java.util.Map;

public class GearTypeRegister {
    public static Map<ResourceLocation,MechaGearType> gearType = Maps.newHashMap();
    public static final MechaGearType NONE = new MechaGearType(ItemStack.EMPTY);
    public static final MechaGearType DISPENCER = new DispencerGear(new ItemStack(Blocks.DISPENSER));

    public static void registerGearType(){
        gearType.put(new ResourceLocation(RedstoneMechaCore.MODID,"none"),NONE);
        gearType.put(new ResourceLocation(RedstoneMechaCore.MODID,"dispencer"),DISPENCER);
    }
}
