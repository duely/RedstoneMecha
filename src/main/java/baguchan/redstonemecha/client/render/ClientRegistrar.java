package baguchan.redstonemecha.client.render;

import baguchan.redstonemecha.client.screen.MechaInventoryScreen;
import baguchan.redstonemecha.client.screen.MechaTableScreen;
import baguchan.redstonemecha.entity.RedWorkerEntity;
import baguchan.redstonemecha.init.MechaContainers;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientRegistrar {
    public static final KeyBinding keyBindAction0 = new KeyBinding("key.redstonemecha.action0", 320, "key.categories.redstonemecha");
    public static final KeyBinding keyBindAction1 = new KeyBinding("key.redstonemecha.action1", 321, "key.categories.redstonemecha");
    public static final KeyBinding keyBindAction2 = new KeyBinding("key.redstonemecha.action2", 322, "key.categories.redstonemecha");

    public static void render(){
        RenderingRegistry.registerEntityRenderingHandler(RedWorkerEntity.class, RedWorkerRender::new);
        ScreenManager.registerFactory(MechaContainers.MECHATABLE, MechaTableScreen::new);
        ScreenManager.registerFactory(MechaContainers.MECHA_INVENTORY, MechaInventoryScreen::new);
    }

    public static void setup (final FMLCommonSetupEvent event) {
        ClientRegistrar.render();
  /*      event.getMinecraftSupplier().get().gameSettings.setKeyBindingCode(keyBindAction0,keyBindAction0.getDefault());
        event.getMinecraftSupplier().get().gameSettings.setKeyBindingCode(keyBindAction1,keyBindAction1.getDefault());
        event.getMinecraftSupplier().get().gameSettings.setKeyBindingCode(keyBindAction2,keyBindAction2.getDefault());
*/
        ClientRegistry.registerKeyBinding(keyBindAction0);
        ClientRegistry.registerKeyBinding(keyBindAction1);
        ClientRegistry.registerKeyBinding(keyBindAction2);
    }
}
