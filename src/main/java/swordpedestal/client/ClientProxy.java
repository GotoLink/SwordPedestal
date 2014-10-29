package swordpedestal.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import swordpedestal.CommonProxy;
import swordpedestal.TileEntitySwordPedestal;

import java.util.HashMap;

public class ClientProxy extends CommonProxy {
    public static final int PEDESTAL_RENDERER_ID = RenderingRegistry.getNextAvailableRenderId();
    public final HashMap<String, ModelBase> pedestalMap = new HashMap<String, ModelBase>();

    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySwordPedestal.class, new TileEntitySwordPedestalRenderer());
        RenderingRegistry.registerBlockHandler(new RenderBlockPedestal());
        addPedestal(new ModelPedestal1(), "Basic");
        addPedestal(new ModelPedestalOot(), "Ocarina of Time");
    }

    public void addPedestal(ModelBase model, String name) {
        addPedestal(name);
        pedestalMap.put(name, model);
    }

    @Override
    public int getPedestalRenderId() {
        return PEDESTAL_RENDERER_ID;
    }

    @Override
    public EntityPlayer getPlayer(MessageContext ctx){
        return FMLClientHandler.instance().getClientPlayerEntity();
    }
}