package swordpedestal.client;

import cpw.mods.fml.client.config.GuiSlider;
import net.minecraft.client.resources.I18n;

/**
 * Created by Olivier on 21/02/2015.
 */
public class GuiSlide extends GuiSlider {
    private boolean init = true;

    public GuiSlide(int id, int xPos, int yPos, String prefix, double minVal, double maxVal, double currentVal, boolean showDec, ISlider slider) {
        super(id, xPos, yPos, 75, 20, prefix, "", minVal, maxVal, currentVal, showDec, true, slider);
        updateSlider();
        init = false;
    }

    public GuiSlide(int id, int xPos, int yPos, String prefix, double maxVal, double currentVal, boolean showDec, ISlider slider) {
        super(id, xPos, yPos, 75, 20, prefix, "", 0.0D, maxVal, currentVal, showDec, true, slider);
        updateSlider();
        init = false;
    }

    @Override
    public void updateSlider(){
        if (this.sliderValue < 0.0F){
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F){
            this.sliderValue = 1.0F;
        }
        String val;
        if (showDecimal){
            val = Double.toString(getValue());
            if (val.substring(val.indexOf(".") + 1).length() > precision)
            {
                val = val.substring(0, val.indexOf(".") + precision + 1);

                if (val.endsWith("."))
                {
                    val = val.substring(0, val.indexOf(".") + precision);
                }
            }
            else
            {
                while (val.substring(val.indexOf(".") + 1).length() < precision)
                {
                    val = val + "0";
                }
            }
        }else{
            val = Integer.toString(getValueInt());
        }
        if(!dispString.isEmpty()) {
            displayString = I18n.format(dispString, val);
        }else{
            displayString = val;
        }
        if (parent != null && !init){
            parent.onChangeSliderValue(this);
        }
    }
}
