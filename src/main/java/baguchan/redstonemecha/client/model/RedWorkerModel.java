package baguchan.redstonemecha.client.model;

import baguchan.redstonemecha.entity.RedWorkerEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RedWorkerModel<T extends RedWorkerEntity> extends EntityModel<T> {
    private final RendererModel body;
    private final RendererModel legR;
    private final RendererModel legL;

    public RedWorkerModel() {
        textureWidth = 128;
        textureHeight = 64;

        body = new RendererModel(this);
        body.setRotationPoint(0.0F, 18.0F, 0.5F);
        body.cubeList.add(new ModelBox(body, 0, 0, -5.0F, -9.0F, -7.0F, 10, 7, 2, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 24, 9, -4.0F, -9.0F, -8.0F, 8, 6, 1, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 36, 0, -5.0F, -9.0F, 4.0F, 10, 7, 2, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 9, 5.0F, -9.0F, -6.0F, 2, 7, 10, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 9, -7.0F, -9.0F, -6.0F, 2, 7, 10, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 0, -2.0F, -8.0F, -5.0F, 4, 1, 2, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 27, -5.0F, -2.0F, -5.0F, 10, 2, 9, 0.0F, false));
        body.cubeList.add(new ModelBox(body, 0, 38, -3.5F, -1.0F, -3.5F, 7, 4, 7, 0.0F, false));

        legR = new RendererModel(this);
        legR.setRotationPoint(4.5F, 19.0F, 1.0F);
        legR.cubeList.add(new ModelBox(legR, 24, 1, -1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F, false));

        legL = new RendererModel(this);
        legL.setRotationPoint(-4.5F, 19.0F, 1.0F);
        legL.cubeList.add(new ModelBox(legL, 24, 1, -1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F, false));
    }

    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        body.render(scale);
        legR.render(scale);
        legL.render(scale);
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        this.legR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.legL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
    }

    public void setRotationAngle(RendererModel rendererModel, float x, float y, float z) {
        rendererModel.rotateAngleX = x;
        rendererModel.rotateAngleY = y;
        rendererModel.rotateAngleZ = z;
    }

    public RendererModel getBody() {
        return this.body;
    }
}