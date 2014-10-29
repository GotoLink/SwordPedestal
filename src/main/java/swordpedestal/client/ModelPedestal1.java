package swordpedestal.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelPedestal1 extends ModelBase {
    public ResourceLocation texture = new ResourceLocation("swordpedestal:textures/pedestal.png");
    public final ModelRenderer box1 = new ModelRenderer(this, 0, 0).setTextureSize(48, 24);
    public final ModelRenderer box2 = new ModelRenderer(this, 0, 10).setTextureSize(48, 24);

    public ModelPedestal1() {
        this.box1.addBox(2.0F, -10.0F, -11.0F, 12, 4, 6, 0.0F);
        this.box2.addBox(2.5F, -12.0F, -10.5F, 11, 2, 5, 0.0F);
    }

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);
        this.box1.render(0.0625F);
        this.box2.render(0.0625F);
    }
}