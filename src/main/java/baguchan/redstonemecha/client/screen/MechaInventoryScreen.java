package baguchan.redstonemecha.client.screen;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.container.MechaContainer;
import baguchan.redstonemecha.entity.MechaBaseEntity;
import baguchan.redstonemecha.recipe.MechaTableRecipe;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MechaInventoryScreen extends ContainerScreen<MechaContainer> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(RedstoneMechaCore.MODID,"textures/gui/inventory/mecha_inventory.png");

    protected final MechaBaseEntity mecha;
    private float mousePosx;
    private float mousePosY;

    public MechaInventoryScreen(MechaContainer containerIn, PlayerInventory playerInv, ITextComponent title) {
        super(containerIn, playerInv, title);
        this.mecha = containerIn.getMechaEntity();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTick) {
        super.render(mouseX, mouseY, partialTick);
        this.renderHoveredToolTip(mouseX, mouseY);
        this.mousePosx = (float)mouseX;
        this.mousePosY = (float)mouseY;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8f, 4f, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8f, (float) (this.ySize - 94), 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.renderBackground();
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        InventoryScreen.drawEntityOnScreen(i + 80, j + 60, 17, (float)(i + 51) - this.mousePosx, (float)(j + 75 - 50) - this.mousePosY, this.mecha);
    }
}