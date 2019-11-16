package baguchan.redstonemecha.init;

import baguchan.redstonemecha.block.MechaTableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;

public class MechaBlocks {

    public static final Block MECHATABLE = new MechaTableBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).hardnessAndResistance(10.0F).noDrops());

    public static void registerBlocks(RegistryEvent.Register<Block> registry) {
        //Terrain
        registry.getRegistry().register(MECHATABLE.setRegistryName("mecha_table"));
    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> registry) {

        MechaItems.register(registry, new BlockItem(MECHATABLE, (new Item.Properties()).group(ItemGroup.MISC)));
    }

}