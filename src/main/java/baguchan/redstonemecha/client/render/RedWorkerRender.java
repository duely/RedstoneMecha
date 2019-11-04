package baguchan.redstonemecha.client.render;

import baguchan.redstonemecha.RedstoneMechaCore;
import baguchan.redstonemecha.client.model.RedWorkerModel;
import baguchan.redstonemecha.client.render.layer.DispencerLayer;
import baguchan.redstonemecha.entity.RedWorkerEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RedWorkerRender <T extends RedWorkerEntity> extends MobRenderer<T, RedWorkerModel<T>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RedstoneMechaCore.MODID, "textures/entity/redworker.png");
    public RedWorkerRender(EntityRendererManager p_i50959_1_) {
        super(p_i50959_1_, new RedWorkerModel<>(), 0.5F);
        this.addLayer(new DispencerLayer<>(this));
    }

    @Override
    protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {
        float f = 1.5F;
        GlStateManager.scalef(1.5F, 1.5F, 1.5F);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(RedWorkerEntity entity) {
        return TEXTURE;
    }
}