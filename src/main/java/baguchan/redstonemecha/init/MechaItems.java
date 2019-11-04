package baguchan.redstonemecha.init;

import baguchan.redstonemecha.RedstoneMechaCore;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MechaItems {
    public static final Item BASIS = new Item(new Item.Properties().group(ItemGroup.MISC));
    public static final Item REDSTONEBASIS = new Item(new Item.Properties().group(ItemGroup.MISC));

    public static void register(RegistryEvent.Register<Item> registry, Item item, String id) {
        if (item instanceof BlockItem){
            Item.BLOCK_TO_ITEM.put(((BlockItem) item).getBlock(), item);
        }

        item.setRegistryName(new ResourceLocation(RedstoneMechaCore.MODID, id));

        registry.getRegistry().register(item);
    }

    public static void register(RegistryEvent.Register<Item> registry, Item item) {

        if (item instanceof BlockItem && item.getRegistryName() == null) {
            item.setRegistryName(((BlockItem) item).getBlock().getRegistryName());

            Item.BLOCK_TO_ITEM.put(((BlockItem) item).getBlock(), item);
        }

        registry.getRegistry().register(item);
    }


    public static void registerItems(RegistryEvent.Register<Item> registry) {
        registry.getRegistry().register(BASIS.setRegistryName("basis"));
        registry.getRegistry().register(REDSTONEBASIS.setRegistryName("redstone_basis"));
    }
}