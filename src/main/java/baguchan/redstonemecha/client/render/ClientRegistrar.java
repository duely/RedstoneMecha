package baguchan.redstonemecha.client.render;

import baguchan.redstonemecha.client.screen.MechaInventoryScreen;
import baguchan.redstonemecha.client.screen.MechaTableScreen;
import baguchan.redstonemecha.entity.RedWorkerEntity;
import baguchan.redstonemecha.init.MechaContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientRegistrar {
    public static void render(){
        RenderingRegistry.registerEntityRenderingHandler(RedWorkerEntity.class, RedWorkerRender::new);
        ScreenManager.registerFactory(MechaContainers.MECHATABLE, MechaTableScreen::new);
        ScreenManager.registerFactory(MechaContainers.MECHA_INVENTORY, MechaInventoryScreen::new);
    }
}
