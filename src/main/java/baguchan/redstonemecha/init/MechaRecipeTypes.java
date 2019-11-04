package baguchan.redstonemecha.init;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.recipe.MechaTableRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class MechaRecipeTypes {
    public static final IRecipeType<MechaTableRecipe> MECHACRAFT = registerRecipeType("mechacrafting");

    private static <T extends IRecipe<?>> IRecipeType<T> registerRecipeType(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(RedstoneMechaCore.MODID, key), new IRecipeType<T>() {
            public String toString() {
                return key;
            }
        });
    }
}