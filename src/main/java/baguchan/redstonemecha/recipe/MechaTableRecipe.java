package baguchan.redstonemecha.recipe;

import baguchan.redstonemecha.init.MechaBlocks;
import baguchan.redstonemecha.init.MechaRecipeSerializers;
import baguchan.redstonemecha.init.MechaRecipeTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class MechaTableRecipe implements IRecipe<IInventory> {
    protected final NonNullList<Ingredient> recipeItems;
    protected final ItemStack result;
    private final IRecipeType<?> type;
    private final IRecipeSerializer<?> serializer;
    protected final ResourceLocation id;
    protected final String group;

    public MechaTableRecipe(ResourceLocation id, String group, NonNullList<Ingredient> recipeItemsIn, ItemStack result) {
        this.type = MechaRecipeTypes.MECHACRAFT;
        this.serializer = MechaRecipeSerializers.MECHACRAFT;
        this.id = id;
        this.group = group;
        this.recipeItems = recipeItemsIn;
        this.result = result;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        if(this.recipeItems.size() == 1){
            return this.recipeItems.get(0).test(inv.getStackInSlot(0)) || this.recipeItems.get(0).test(inv.getStackInSlot(1));
        }


        return this.recipeItems.get(0).test(inv.getStackInSlot(0)) && this.recipeItems.get(1).test(inv.getStackInSlot(1)) || this.recipeItems.get(0).test(inv.getStackInSlot(1)) && this.recipeItems.get(1).test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getGroup() {
        return this.group;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return this.type;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(MechaBlocks.MECHATABLE);
    }

    public static class Serializer<T extends MechaTableRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
        final IRecipeFactory<T> factory;

        public Serializer(IRecipeFactory<T> factory) {
            this.factory = factory;
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray p_199568_0_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < p_199568_0_.size(); ++i) {
                Ingredient ingredient = Ingredient.deserialize(p_199568_0_.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Override
        @SuppressWarnings("deprecation")
        public T read(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getString(json, "group", "");
            NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for MechaTable recipe");
            }

            String s1 = JSONUtils.getString(json, "result");
            int i = JSONUtils.getInt(json, "count");
            ItemStack itemstack = new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(s1)), i);
            return this.factory.create(recipeId, s, nonnulllist, itemstack);
        }

        @Override
        public T read(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readString(32767);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.read(buffer));
            }

            ItemStack itemstack = buffer.readItemStack();
            return this.factory.create(recipeId, s, nonnulllist, itemstack);
        }

        @Override
        public void write(PacketBuffer buffer, T recipe) {
            buffer.writeString(recipe.group);
            buffer.writeVarInt(recipe.recipeItems.size());

            for(Ingredient ingredient : recipe.recipeItems) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.result);
        }

        public interface IRecipeFactory<T extends MechaTableRecipe> {
            T create(ResourceLocation id, String group, NonNullList<Ingredient> ingredient, ItemStack result);
        }
    }
}