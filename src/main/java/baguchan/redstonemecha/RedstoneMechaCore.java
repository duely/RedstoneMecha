package baguchan.redstonemecha;

import baguchan.redstonemecha.client.render.ClientRegistrar;
import baguchan.redstonemecha.init.GearTypeRegister;
import baguchan.redstonemecha.init.MechaBlocks;
import baguchan.redstonemecha.init.MechaItems;
import baguchan.redstonemecha.network.MechaPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("redstonemecha")
public class RedstoneMechaCore
{
    public final KeyBinding keyBindAction0 = new KeyBinding("key.redstonemecha.action0", 320, "key.categories.redstonemecha");
    public final KeyBinding keyBindAction1 = new KeyBinding("key.redstonemecha.action1", 321, "key.categories.redstonemecha");
    public final KeyBinding keyBindAction2 = new KeyBinding("key.redstonemecha.action2", 322, "key.categories.redstonemecha");

    // Directly reference a log4j logger.
    public static final String MODID = "redstonemecha";
    public static RedstoneMechaCore instance;

    public RedstoneMechaCore() {
        instance = this;

        MechaPacketHandler.register();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        GearTypeRegister.registerGearType();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistrar.render();
  /*      event.getMinecraftSupplier().get().gameSettings.setKeyBindingCode(keyBindAction0,keyBindAction0.getDefault());
        event.getMinecraftSupplier().get().gameSettings.setKeyBindingCode(keyBindAction1,keyBindAction1.getDefault());
        event.getMinecraftSupplier().get().gameSettings.setKeyBindingCode(keyBindAction2,keyBindAction2.getDefault());
*/
        ClientRegistry.registerKeyBinding(keyBindAction0);
        ClientRegistry.registerKeyBinding(keyBindAction1);
        ClientRegistry.registerKeyBinding(keyBindAction2);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
    }

    private void processIMC(final InterModProcessEvent event)
    {

    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            MechaBlocks.registerBlocks(event);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            MechaBlocks.registerItemBlocks(event);
            MechaItems.registerItems(event);
        }
    }
}
