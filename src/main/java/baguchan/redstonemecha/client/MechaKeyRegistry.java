package baguchan.redstonemecha.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class MechaKeyRegistry {
    public static final KeyBinding keyBindAction0 = new KeyBinding("key.redstonemecha.action0", 320, "key.categories.redstonemecha");
    public static final KeyBinding keyBindAction1 = new KeyBinding("key.redstonemecha.action1", 321, "key.categories.redstonemecha");
    public static final KeyBinding keyBindAction2 = new KeyBinding("key.redstonemecha.action2", 322, "key.categories.redstonemecha");


    public static void register() {

        ClientRegistry.registerKeyBinding(keyBindAction0);
        ClientRegistry.registerKeyBinding(keyBindAction1);
        ClientRegistry.registerKeyBinding(keyBindAction2);
    }
}
