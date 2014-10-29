package swordpedestal.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiInvisibleButton extends GuiButton {

    public GuiInvisibleButton(int id, int posx, int posy, int width, int height, String display) {
        super(id, posx, posy, width, height, display);
        visible = false;
        packedFGColour = 14737632;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    @Override
    public void drawButton(Minecraft mc, int p_146112_2_, int p_146112_3_)
    {
        this.drawCenteredString(mc.fontRenderer, this.displayString, this.xPosition, this.yPosition + this.height - 4, packedFGColour);
    }
}
