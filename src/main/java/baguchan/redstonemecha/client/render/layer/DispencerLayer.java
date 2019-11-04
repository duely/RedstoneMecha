package baguchan.redstonemecha.client.render.layer;

import baguchan.redstonemecha.client.model.RedWorkerModel;
import baguchan.redstonemecha.client.render.RedWorkerRender;
import baguchan.redstonemecha.entity.RedWorkerEntity;
import baguchan.redstonemecha.init.GearTypeRegister;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DispencerLayer<T extends RedWorkerEntity> extends LayerRenderer<T, RedWorkerModel<T>> {
    public DispencerLayer(RedWorkerRender<T> tRedWorkerRender) {
        super(tRedWorkerRender);
    }

    @Override
    public void render(T entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
        if (!entityIn.isInvisible() && entityIn.getInventory().getStackInSlot(1).getItem() == Item.getItemFromBlock(Blocks.DISPENSER)) {
            GlStateManager.pushMatrix();
            float f = 0.625F;
            this.getEntityModel().getBody().postRender(0.0625F);
            GlStateManager.translatef(0.0F, -0.34375F, -0.45F);
            GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scalef(0.325F, -0.325F, -0.325F);
            Minecraft.getInstance().getFirstPersonRenderer().renderItem(entityIn, new ItemStack(Blocks.DISPENSER), ItemCameraTransforms.TransformType.HEAD);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
