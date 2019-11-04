package baguchan.redstonemecha.init;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.recipe.MechaTableRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(RedstoneMechaCore.MODID)
@Mod.EventBusSubscriber(modid = RedstoneMechaCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MechaRecipeSerializers {
    public static final IRecipeSerializer<MechaTableRecipe> MECHACRAFT = new MechaTableRecipe.Serializer<>(MechaTableRecipe::new);

    @SubscribeEvent
    public static void onRegisterSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(MECHACRAFT.setRegistryName(new ResourceLocation(RedstoneMechaCore.MODID, "mechacrafting")));
    }
}