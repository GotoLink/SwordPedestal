package swordpedestal.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import swordpedestal.SwordPedestalMain;
import swordpedestal.TileEntitySwordPedestal;

public final class TileEntitySwordPedestalRenderer extends TileEntitySpecialRenderer {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final ResourceLocation BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    private void doRender(TileEntitySwordPedestal te, double x, double y, double z, float f) {
        ModelBase pedestal = (((ClientProxy) SwordPedestalMain.proxy).pedestalMap.get(te.pedestalName));
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5D, y + 0.75D, z + 0.5D);

        GL11.glRotatef(-te.baseRotation, 0.0F, 1.0F, 0.0F);

        GL11.glPushMatrix();

        GL11.glTranslated(-0.5D, 0.0D, 0.5D);
        pedestal.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        GL11.glPopMatrix();

        if (te.sword != null) {

            if (te.isFloating) {
                GL11.glTranslated(0.0D, 0.45D, 0.0D);
                GL11.glTranslated(0.0D, Math.sin(Math.toRadians(te.sinShift * 2)) / 32.0D, 0.0D);
                if (te.clockwiseRotation) {
                    GL11.glRotatef(-te.rotation, 0.0F, 1.0F, 0.0F);
                } else {
                    GL11.glRotatef(te.rotation, 0.0F, 1.0F, 0.0F);
                }
            }

            GL11.glTranslated(0.6875D, -0.25D, 0.0D);
            GL11.glRotatef(135.0F, 0.0F, 0.0F, 1.0F);

            IIcon icon = te.sword.getIconIndex();
            if(icon!=null) {
                FMLClientHandler.instance().getClient().getTextureManager().bindTexture(FMLClientHandler.instance().getClient().getTextureManager().getResourceLocation(te.sword.getItemSpriteNumber()));
                ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 16F / 256F);
            }
            if (te.enchantmentGlimmer) {//render enchantment effect
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                FMLClientHandler.instance().getClient().getTextureManager().bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(768, 1, 1, 0);
                GL11.glColor4f(0.38F, 0.19F, 0.608F, 1.0F);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glPushMatrix();
                float f8 = 0.125F;
                GL11.glScalef(f8, f8, f8);
                float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                GL11.glTranslatef(f9, 0.0F, 0.0F);
                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(Tessellator.instance, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 16F / 256F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f8, f8, f8);
                f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                GL11.glTranslatef(-f9, 0.0F, 0.0F);
                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                ItemRenderer.renderItemIn2D(Tessellator.instance, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 16F / 256F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }
            if (te.lightBeamEnabled) {
                GL11.glPopMatrix();
                GL11.glPushMatrix();

                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                FMLClientHandler.instance().getClient().getTextureManager().bindTexture(BEACON_BEAM);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glDepthMask(false);
                float f2 = f + te.getWorldObj().getWorldTime();
                float f3 = -f2 * 0.1F;
                double d3 = -f2 * 0.0375D;
                Tessellator t = Tessellator.instance;
                double d4 = 0.2D;
                double d5 = Math.PI / 4D;
                double d7 = 0.5D + Math.cos(d3 + d5 * 3D) * d4;
                double d9 = 0.5D + Math.sin(d3 + d5 * 3D) * d4;
                double d11 = 0.5D + Math.cos(d3 + d5) * d4;
                double d13 = 0.5D + Math.sin(d3 + d5) * d4;
                double d15 = 0.5D + Math.cos(d3 + d5 * 3.2D) * d4;
                double d17 = 0.5D + Math.sin(d3 + d5 * 3.2D) * d4;
                double d19 = 0.5D + Math.cos(d3 + d5 * 7D) * d4;
                double d21 = 0.5D + Math.sin(d3 + d5 * 7D) * d4;
                double d23 = te.getWorldObj().getActualHeight();
                double d20 = 0.0D;
                double d22 = 1.0D;
                double d24 = f3 - d22;
                double d29 = d23 * (0.5D / d4) + d24;
                //Inner ray
                t.startDrawingQuads();
                t.setColorRGBA(te.colorRGBA[0], te.colorRGBA[1], te.colorRGBA[2], te.colorRGBA[3]);
                t.addVertexWithUV(x + d7, y + d23, z + d9, d22, d29);
                t.addVertexWithUV(x + d7, y, z + d9, d22, d24);
                t.addVertexWithUV(x + d11, y, z + d13, d20, d24);
                t.addVertexWithUV(x + d11, y + d23, z + d13, d20, d29);
                
                t.addVertexWithUV(x + d19, y + d23, z + d21, d22, d29);
                t.addVertexWithUV(x + d19, y, z + d21, d22, d24);
                t.addVertexWithUV(x + d15, y, z + d17, d20, d24);
                t.addVertexWithUV(x + d15, y + d23, z + d17, d20, d29);
                
                t.addVertexWithUV(x + d11, y + d23, z + d13, d22, d29);
                t.addVertexWithUV(x + d11, y, z + d13, d22, d24);
                t.addVertexWithUV(x + d19, y, z + d21, d20, d24);
                t.addVertexWithUV(x + d19, y + d23, z + d21, d20, d29);
                
                t.addVertexWithUV(x + d15, y + d23, z + d17, d22, d29);
                t.addVertexWithUV(x + d15, y, z + d17, d22, d24);
                t.addVertexWithUV(x + d7, y, z + d9, d20, d24);
                t.addVertexWithUV(x + d7, y + d23, z + d9, d20, d29);
                t.draw();
                double d6 = 1.0D - d4;
                double d26 = d23 + d24;
                //Outer ray
                t.startDrawingQuads();
                t.setColorRGBA(te.colorRGBA[0], te.colorRGBA[1], te.colorRGBA[2], te.colorRGBA[3]);
                t.addVertexWithUV(x + d4, y + d23, z + d4, d22, d26);
                t.addVertexWithUV(x + d4, y, z + d4, d22, d24);
                t.addVertexWithUV(x + d6, y, z + d4, d20, d24);
                t.addVertexWithUV(x + d6, y + d23, z + d4, d20, d26);
                
                t.addVertexWithUV(x + d6, y + d23, z + d6, d22, d26);
                t.addVertexWithUV(x + d6, y, z + d6, d22, d24);
                t.addVertexWithUV(x + d4, y, z + d6, d20, d24);
                t.addVertexWithUV(x + d4, y + d23, z + d6, d20, d26);
                
                t.addVertexWithUV(x + d6, y + d23, z + d4, d22, d26);
                t.addVertexWithUV(x + d6, y, z + d4, d22, d24);
                t.addVertexWithUV(x + d6, y, z + d6, d20, d24);
                t.addVertexWithUV(x + d6, y + d23, z + d6, d20, d26);
                
                t.addVertexWithUV(x + d4, y + d23, z + d6, d22, d26);
                t.addVertexWithUV(x + d4, y, z + d6, d22, d24);
                t.addVertexWithUV(x + d4, y, z + d4, d20, d24);
                t.addVertexWithUV(x + d4, y + d23, z + d4, d20, d26);
                t.draw();
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(true);
            }
        }

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        doRender((TileEntitySwordPedestal) var1, var2, var4, var6, var8);
    }
}
