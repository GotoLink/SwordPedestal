package swordpedestal.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiSlider;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import swordpedestal.ContainerPedestal;
import swordpedestal.PacketHandler;
import swordpedestal.SwordPedestalMain;
import swordpedestal.TileEntitySwordPedestal;

public class GuiPedestal extends GuiContainer {
    private static final ResourceLocation tex = new ResourceLocation("swordpedestal:textures/pedestalgui.png");
    private static final int TEXT_COLOR = 16777215;
    private final TileEntitySwordPedestal te;
    private ModelBase model;
    private GuiCheckBox cb1, cb2, cb3, cb4, cb5;
    private GuiSlider sliderSpd;
    private GuiSlider rotKnob;
    private GuiSlider rotKnobFloating;
    private GuiSlider heightKnobFloating;
    private GuiButton fakeSlot;
    private Tab currentTab = Tab.LightBeam;
    private GuiTextField textField;
    private GuiSlider sliderR, sliderG, sliderB, sliderA;
    private GuiButton btn;

    public GuiPedestal(IInventory iInventory, TileEntitySwordPedestal te) {
        super(new ContainerPedestal(iInventory, te));
        this.te = te;
        this.xSize = 176;
        this.ySize = 214;
        this.model = ((ClientProxy) SwordPedestalMain.proxy).pedestalMap.get(this.te.pedestalName);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    public void setupTab(Tab tab) {
        switch (tab) {
            case LightBeam:
                this.cb1.enabled = true;
                this.cb1.visible = true;
                this.sliderR.enabled = true;
                this.sliderR.visible = true;
                this.sliderG.enabled = true;
                this.sliderG.visible = true;
                this.sliderB.enabled = true;
                this.sliderB.visible = true;
                this.sliderA.enabled = true;
                this.sliderA.visible = true;
                if (this.textField.getText().length() == 6) {
                    this.btn.enabled = true;
                }
                this.btn.visible = true;
                this.textField.setVisible(true);
                this.textField.setFocused(true);
                break;
            case Floating:
                this.cb2.enabled = true;
                this.cb2.visible = true;
                this.cb4.enabled = true;
                this.cb4.visible = true;
                this.cb5.enabled = true;
                this.cb5.visible = true;
                this.sliderSpd.enabled = true;
                this.sliderSpd.visible = true;
                if (!this.te.isRotating) {
                    this.rotKnobFloating.enabled = true;
                }
                this.rotKnobFloating.visible = true;
                if(this.te.isFloating) {
                    this.heightKnobFloating.enabled = true;
                }
                this.heightKnobFloating.visible = true;
                break;
            case Redstone:
                break;
            case Other:
                this.cb3.enabled = true;
                this.cb3.visible = true;
                this.rotKnob.enabled = true;
                this.rotKnob.visible = true;
                break;
        }
    }

    @Override
    public void updateScreen() {
        this.textField.updateCursorCounter();
        disableButtons();
        setupTab(this.currentTab);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int x = this.guiLeft;
        int y = this.guiTop;
        this.buttonList.add(new GuiButtonExt(1, x + 4, y + 46, 80, 16, I18n.format(Tab.LightBeam.toString())));
        this.buttonList.add(new GuiButtonExt(2, x + 4, y + 62, 80, 16, I18n.format(Tab.Floating.toString())));
        this.buttonList.add(new GuiButtonExt(3, x + 4, y + 78, 80, 16, I18n.format(Tab.Redstone.toString())));
        this.buttonList.add(new GuiButtonExt(4, x + 4, y + 94, 80, 16, I18n.format(Tab.Other.toString())));
        ((GuiButton) this.buttonList.get(2)).enabled = false;

        this.buttonList.add(this.cb1 = new GuiCheckBox(6, x + 150, y + 4, "", this.te.lightBeamEnabled));
        this.buttonList.add(this.cb2 = new GuiCheckBox(7, x + 150, y + 4, "", this.te.isFloating));
        this.buttonList.add(this.cb3 = new GuiCheckBox(8, x + 150, y + 4, "", this.te.enchantmentGlimmer));

        this.buttonList.add(this.sliderSpd = makeSlider(13, x + 90, y + 47, 360.0F/15.0F, (this.te.rotationSpeed - 1) / 15.0F, "gui.pedestal.rotatespeed"));

        this.buttonList.add(this.rotKnob = new GuiSlide(14, x + 90, y + 30, "gui.pedestal.oriented", -180.0F, 180.0F, this.te.baseRotation, false, INSTANCE));

        this.buttonList.add(this.cb4 = new GuiCheckBox(9, x + 150, y + 15, "", this.te.isRotating));
        this.buttonList.add(this.cb5 = new GuiCheckBox(10, x + 150, y + 25, "", this.te.clockwiseRotation));

        this.buttonList.add(this.sliderR = makeSlider(16, x + 90, y + 37, this.te.colorRGBA[0] / 255.0F));

        this.buttonList.add(this.sliderG = makeSlider(17, x + 90, y + 57, this.te.colorRGBA[1] / 255.0F));

        this.buttonList.add(this.sliderB = makeSlider(18, x + 90, y + 77, this.te.colorRGBA[2] / 255.0F));

        this.buttonList.add(this.sliderA = makeSlider(19, x + 90, y + 97, this.te.colorRGBA[3] / 95.0F));

        this.buttonList.add(this.btn = new GuiButtonExt(20, x + 140, y + 25, 30, 11, I18n.format("gui.pedestal.set")));

        this.buttonList.add(this.heightKnobFloating = new GuiSlide(15, x + 90, y + 92, "gui.pedestal.height", 250, !this.te.isFloating ? 45 : this.te.floatingHeight, false, ResetFloatChange));
        this.buttonList.add(this.rotKnobFloating = new GuiSlide(21, x + 90, y + 70, "gui.pedestal.rotation", 360.0F, this.te.isRotating ? 0.0F : this.te.rotation, false, ResetRotChange));

        this.textField = new GuiTextField(this.fontRendererObj, x + 91, y + 26, 45, 10);
        this.textField.setVisible(false);
        this.textField.setFocused(false);
        this.textField.setMaxStringLength(6);
        int i = (this.te.colorRGBA[0] << 16) + (this.te.colorRGBA[1] << 8) + this.te.colorRGBA[2];
        String s = Integer.toHexString(i).toUpperCase();
        if (s.length() != 6) {
            for (int j = 0; j < 7 - s.length(); j++) {
                s = "0" + s;
            }
        }
        this.textField.setText(s);

        this.buttonList.add(this.fakeSlot = new GuiInvisibleButton(5, x + 53, y + 15, 25, 25, this.te.pedestalName));
        disableButtons();
        setupTab(this.currentTab);
    }

    private GuiSlider makeSlider(int id, int xpos, int ypos, double max, double value, String text) {
        return new GuiSlide(id, xpos, ypos, text, max, value, true, INSTANCE);
    }
    private GuiSlider makeSlider(int id, int xpos, int ypos, double max, double value) {
        return makeSlider(id, xpos, ypos, max, value, "");
    }

    private GuiSlider makeSlider(int id, int xpos, int ypos, double value) {
        return makeSlider(id, xpos, ypos, 1.00D, value);
    }

    private static final GuiSlider.ISlider INSTANCE = new SendChange();
    private static class SendChange implements GuiSlider.ISlider {
        @Override
        public void onChangeSliderValue(GuiSlider slider) {
            PacketHandler.sendGuiChange(slider.id, slider.getValue());
        }
    }
    private final GuiSlider.ISlider ResetFloatChange = new SendChange(){
        @Override
        public void onChangeSliderValue(GuiSlider slider) {
            if(!te.isFloating)
                slider.setValue(45);
            else
                super.onChangeSliderValue(slider);
        }
    };
    private final GuiSlider.ISlider ResetRotChange = new SendChange(){
        @Override
        public void onChangeSliderValue(GuiSlider slider) {
            if (te.isRotating)
                slider.setValue(0.00D);
            else
                super.onChangeSliderValue(slider);
        }
    };

    private void disableButtons() {
        for (int i = 4; i < this.buttonList.size() - 1; i++) {
            GuiButton b = (GuiButton) this.buttonList.get(i);
            b.enabled = false;
            b.visible = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        switch (par1GuiButton.id) {
            case 1:case 2:case 3:case 4:
                this.currentTab = Tab.values()[par1GuiButton.id-1];
                break;
            case 5:
                if(this.inventorySlots.enchantItem(this.mc.thePlayer, par1GuiButton.id)) {
                    this.fakeSlot.displayString = this.te.pedestalName;
                    this.model = (((ClientProxy) SwordPedestalMain.proxy).pedestalMap.get(this.te.pedestalName));
                    this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, par1GuiButton.id);
                }
                break;
            case 6:case 7:case 8:case 9:case 10:
                if(this.inventorySlots.enchantItem(this.mc.thePlayer, par1GuiButton.id))
                    this.mc.playerController.sendEnchantPacket(this.inventorySlots.windowId, par1GuiButton.id);
                break;
            case 20:
                try {
                    int i = Integer.parseInt(this.textField.getText(), 16);
                    this.te.colorRGBA[0] = (i >> 16 & 0xFF);
                    this.te.colorRGBA[1] = (i >> 8 & 0xFF);
                    this.te.colorRGBA[2] = (i & 0xFF);
                } catch (NumberFormatException e) {
                }
                sliderR.setValue((float)this.te.colorRGBA[0] / 255.0F);
                sliderG.setValue((float)this.te.colorRGBA[1] / 255.0F);
                sliderB.setValue((float)this.te.colorRGBA[2] / 255.0F);
                sliderA.setValue((float)this.te.colorRGBA[3] / 95.0F);
                INSTANCE.onChangeSliderValue(sliderR);
                INSTANCE.onChangeSliderValue(sliderG);
                INSTANCE.onChangeSliderValue(sliderB);
                INSTANCE.onChangeSliderValue(sliderA);
                break;
            default:
                break;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        GL11.glDisable(2896);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.fontRendererObj.drawString(I18n.format("gui.pedestal"), 6, 6, 4210752);

        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 93, 4210752);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2896);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(tex);
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        switch (this.currentTab) {
            case LightBeam:
                this.textField.drawTextBox();
                Gui.drawRect(x + 90, y + 14, x + 170, y + 24, -9803158);
                int color = -16777216 + (this.te.colorRGBA[0] << 16) + (this.te.colorRGBA[1] << 8) + this.te.colorRGBA[2];
                Gui.drawRect(x + 91, y + 15, x + 169, y + 23, color);
                this.fontRendererObj.drawStringWithShadow(I18n.format("gui.pedestal.enabled"), x + 90, y + 5, TEXT_COLOR);
                break;
            case Floating:
                this.fontRendererObj.drawStringWithShadow(I18n.format("gui.pedestal.enabled"), x + 90, y + 5, TEXT_COLOR);
                this.fontRendererObj.drawStringWithShadow(I18n.format("gui.pedestal.rotate"), x + 90, y + 15, TEXT_COLOR);
                this.fontRendererObj.drawStringWithShadow(I18n.format("gui.pedestal.clock"), x + 90, y + 25, TEXT_COLOR);
                break;
            case Redstone:
                break;
            case Other:
                this.fontRendererObj.drawStringWithShadow(I18n.format("gui.pedestal.enchanted"), x + 90, y + 5, TEXT_COLOR);
                break;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslated(x + 48, y + 16, 64.0D);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glPushMatrix();
        GL11.glEnable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2884);
        GL11.glEnable(32826);

        GL11.glScaled(24.0D, 24.0D, 24.0D);
        this.model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glDisable(32826);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GL11.glDisable(2929);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glPopMatrix();
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        if (par2 == 1) {
            FMLClientHandler.instance().getClientPlayerEntity().closeScreen();
        }
        if (this.currentTab == Tab.LightBeam) {
            this.textField.textboxKeyTyped(par1, par2);

            if ((par1 < 'A' || par1 > 'F') && (par1 < 'a' || par1 > 'f') && (par1 < '0' || par1 > '9') && ChatAllowedCharacters.isAllowedCharacter(par1)) {
                this.textField.deleteFromCursor(-1);
            }

            this.textField.setText(this.textField.getText().toUpperCase());

            if (par1 == '\r') {
                actionPerformed((GuiButton) this.buttonList.get(0));
            }

            if (this.textField.getText().length() == 6) {
                this.btn.enabled = true;
            }
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        if (this.currentTab == Tab.LightBeam) {
            this.textField.mouseClicked(par1, par2, par3);
        }
    }
}