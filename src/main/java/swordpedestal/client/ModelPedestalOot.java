package swordpedestal.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class ModelPedestalOot extends ModelBase {
    public ResourceLocation texture = new ResourceLocation("swordpedestal:textures/ootpedestal.png");

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);
        Tessellator t = Tessellator.instance;

        GL11.glPushMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glTranslated(0.0D, -0.75D, -0.375D);

        t.startDrawingQuads();

        t.setNormal(0.0F, 0.0F, 1.0F);

        t.addVertexWithUV(0.0625D, 0.0D, 0.09375D, 0.0D, 0.5D);
        t.addVertexWithUV(0.9375D, 0.0D, 0.09375D, 1.0D, 0.5D);
        t.addVertexWithUV(0.8125D, 0.25D, 0.0D, 0.859375D, 0.25D);
        t.addVertexWithUV(0.1875D, 0.25D, 0.0D, 0.140625D, 0.25D);

        t.addVertexWithUV(0.1875D, 0.25D, -0.25D, 0.140625D, 0.25D);
        t.addVertexWithUV(0.8125D, 0.25D, -0.25D, 0.859375D, 0.25D);
        t.addVertexWithUV(0.9375D, 0.0D, -0.34375D, 1.0D, 0.5D);
        t.addVertexWithUV(0.0625D, 0.0D, -0.34375D, 0.0D, 0.5D);

        t.addVertexWithUV(0.1875D, 0.25D, -0.25D, 0.0D, 0.25D);
        t.addVertexWithUV(0.1875D, 0.25D, 0.0D, 0.0D, 0.0D);
        t.addVertexWithUV(0.8125D, 0.25D, 0.0D, 0.625D, 0.0D);
        t.addVertexWithUV(0.8125D, 0.25D, -0.25D, 0.625D, 0.25D);

        t.addVertexWithUV(0.1875D, 0.25D, 0.0D, 0.6875D, 0.0D);
        t.addVertexWithUV(0.1875D, 0.25D, -0.25D, 0.9375D, 0.0D);
        t.addVertexWithUV(0.0625D, 0.0D, -0.34375D, 1.0D, 0.25D);
        t.addVertexWithUV(0.0625D, 0.0D, 0.09375D, 0.625D, 0.25D);

        t.addVertexWithUV(0.8125D, 0.25D, -0.25D, 0.703125D, 0.0D);
        t.addVertexWithUV(0.8125D, 0.25D, 0.0D, 0.921875D, 0.0D);
        t.addVertexWithUV(0.9375D, 0.0D, 0.09375D, 1.0D, 0.25D);
        t.addVertexWithUV(0.9375D, 0.0D, -0.34375D, 0.625D, 0.25D);

        t.addVertexWithUV(0.0625D, 0.0D, 0.09375D, 0.0D, 0.5D);
        t.addVertexWithUV(0.0625D, 0.0D, -0.34375D, 0.0D, 0.75D);
        t.addVertexWithUV(0.9375D, 0.0D, -0.34375D, 1.0D, 0.75D);
        t.addVertexWithUV(0.9375D, 0.0D, 0.09375D, 1.0D, 0.5D);

        t.draw();

        GL11.glPopMatrix();
    }
}