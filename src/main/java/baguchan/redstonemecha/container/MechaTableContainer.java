package baguchan.redstonemecha.container;

import baguchan.redstonemecha.init.MechaBlocks;
import baguchan.redstonemecha.init.MechaContainers;
import baguchan.redstonemecha.init.MechaRecipeTypes;
import baguchan.redstonemecha.recipe.MechaTableRecipe;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class MechaTableContainer extends Container {
    private final IWorldPosCallable iWorldPosCallable;
    private final IntReferenceHolder intReferenceHolder = IntReferenceHolder.single();
    private final World world;
    private List<MechaTableRecipe> recipes = Lists.newArrayList();
    private ItemStack result = ItemStack.EMPTY;
    private long timeElapsed;
    private final Slot inputSlot;
    private final Slot inputSlot2;
    private final Slot outputSlot;
    private Runnable inventoryUpdateListener = () -> {
    };
    private final IInventory inventory = new Inventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();
            onCraftMatrixChanged(this);
            inventoryUpdateListener.run();
        }
    };
    private final CraftResultInventory resultInventory = new CraftResultInventory();

    public MechaTableContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
    }

    public MechaTableContainer(int id, PlayerInventory playerInventory, final IWorldPosCallable iWorldPosCallable) {
        super(MechaContainers.MECHATABLE, id);
        this.iWorldPosCallable = iWorldPosCallable;
        this.world = playerInventory.player.world;
        this.inputSlot = addSlot(new Slot(this.inventory, 0, 20, 33));
        this.inputSlot2 = addSlot(new Slot(this.inventory, 1, 20, 51));
        this.outputSlot = addSlot(new Slot(this.resultInventory, 2, 143, 33) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                ItemStack itemstack = inputSlot.decrStackSize(1);
                ItemStack itemstack2 = inputSlot2.decrStackSize(1);
                if (!itemstack.isEmpty() || !itemstack2.isEmpty()) {
                    func_217082_i();
                }
                stack.getItem().onCreated(stack, thePlayer.world, thePlayer);
                iWorldPosCallable.consume((world, pos) -> {
                    long l = world.getGameTime();
                    if (timeElapsed != l) {
                        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1f, 1f);
                        timeElapsed = l;
                    }
                });
                return super.onTake(thePlayer, stack);
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
        trackInt(this.intReferenceHolder);
    }

    @OnlyIn(Dist.CLIENT)
    public int func_217073_e() {
        return this.intReferenceHolder.get();
    }

    @OnlyIn(Dist.CLIENT)
    public List<MechaTableRecipe> getRecipeList() {
        return this.recipes;
    }

    @OnlyIn(Dist.CLIENT)
    public int getRecipeListSize() {
        return this.recipes.size();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_217083_h() {
        return this.inputSlot.getHasStack() && !this.recipes.isEmpty();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.iWorldPosCallable.applyOrElse((world, pos) -> world.getBlockState(pos).getBlock() == MechaBlocks.MECHATABLE && playerIn.getDistanceSq((double) pos.getX() + 0.5d, (double) pos.getY() + 0.5d, (double) pos.getZ() + 0.5d) <= 64d, true);
    }

    @Override
    public boolean enchantItem(PlayerEntity playerIn, int id) {
        if (id >= 0 && id < this.recipes.size()) {
            this.intReferenceHolder.set(id);
            func_217082_i();
        }
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        ItemStack stack = this.inputSlot.getStack();
        ItemStack stack2 = this.inputSlot2.getStack();
        if (stack.getItem() != this.result.getItem()) {
            this.result = stack.copy();
            updateAvailableRecipes(inventoryIn, stack);
        }else if(stack2.getItem() != this.result.getItem()){
            this.result = stack2.copy();
            updateAvailableRecipes(inventoryIn, stack2);
        }
    }

    private void updateAvailableRecipes(IInventory inventoryIn, ItemStack stack) {
        this.recipes.clear();
        if (!stack.isEmpty()) {
            this.recipes = this.world.getRecipeManager().getRecipes(MechaRecipeTypes.MECHACRAFT, inventoryIn, this.world);
        }else {
            this.outputSlot.putStack(ItemStack.EMPTY);
            this.intReferenceHolder.set(-1);
        }
    }

    private void func_217082_i() {
        if (!this.recipes.isEmpty()) {
            MechaTableRecipe recipe = this.recipes.get(this.intReferenceHolder.get());
            this.outputSlot.putStack(recipe.getCraftingResult(this.inventory));
        } else {
            this.outputSlot.putStack(ItemStack.EMPTY);
        }
        detectAndSendChanges();
    }

    @Override
    public ContainerType<?> getType() {
        return MechaContainers.MECHATABLE;
    }

    @OnlyIn(Dist.CLIENT)
    public void setInventoryUpdateListener(Runnable runnable) {
        this.inventoryUpdateListener = runnable;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            Item item = itemstack1.getItem();
            stack = itemstack1.copy();
            if (index == 1) {
                item.onCreated(itemstack1, playerIn.world, playerIn);
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, stack);
            } else if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 30) {
                if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            slot.onSlotChanged();
            if (itemstack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
            detectAndSendChanges();
        }

        return stack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.resultInventory.removeStackFromSlot(1);
        this.resultInventory.removeStackFromSlot(2);
        this.iWorldPosCallable.consume((world, pos) -> clearContainer(playerIn, world, this.inventory));
    }
}