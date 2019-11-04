package baguchan.redstonemecha.init;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.container.MechaContainer;
import baguchan.redstonemecha.container.MechaTableContainer;
import baguchan.redstonemecha.entity.MechaBaseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(RedstoneMechaCore.MODID)
@Mod.EventBusSubscriber(modid = RedstoneMechaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MechaContainers {

    public static final ContainerType<MechaTableContainer> MECHATABLE = new ContainerType<>(MechaTableContainer::new);
    public static final ContainerType<MechaContainer> MECHA_INVENTORY = IForgeContainerType.create((windowId, inv, data) -> {

        Entity entity = inv.player.world.getEntityByID(data.readInt());
        
        if(entity instanceof MechaBaseEntity) {
            return new MechaContainer(windowId, inv, (MechaBaseEntity)entity);
        } else {
            return null;
        }
    });


    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        event.getRegistry().register(MECHATABLE.setRegistryName(RedstoneMechaCore.MODID, "mecha_table"));
        event.getRegistry().register(MECHA_INVENTORY.setRegistryName(RedstoneMechaCore.MODID, "mecha_inventory"));
    }

}