package baguchan.redstonemecha.init;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.entity.RedWorkerEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(RedstoneMechaCore.MODID)
@Mod.EventBusSubscriber(modid = RedstoneMechaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MechaEntitys {
    public static final EntityType<RedWorkerEntity> REDWORKER = EntityType.Builder.create(RedWorkerEntity::new, EntityClassification.MISC).setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).size(1.2F, 1.35F).build(prefix("redworker"));

    @SubscribeEvent
    public static void onRegisterEntity(final RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(REDWORKER.setRegistryName("redworker"));
    }

    private static String prefix(String path) {
        return RedstoneMechaCore.MODID + "." + path;
    }
}