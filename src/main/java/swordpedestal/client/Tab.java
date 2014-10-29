package swordpedestal.client;

import java.util.Locale;

enum Tab {
    LightBeam, Floating, Redstone, Other;

    @Override
    public String toString(){
        return "gui.pedestal."+name().toLowerCase(Locale.ENGLISH);
    }
}
