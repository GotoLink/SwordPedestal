package swordpedestal;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import swordpedestal.client.GuiPedestal;

import java.util.ArrayList;
import java.util.List;

public class CommonProxy implements IGuiHandler {
    public List<String> pedestalNames;

    public void registerRenderers() {
        addPedestal("Basic");
        addPedestal("Ocarina of Time");
    }

    public void addPedestal(String name) {
        if (pedestalNames == null)
            pedestalNames = new ArrayList<String>();
        pedestalNames.add(name);
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntitySwordPedestal) {
            return new ContainerPedestal(player.inventory, (TileEntitySwordPedestal) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntitySwordPedestal) {
            return new GuiPedestal(player.inventory, (TileEntitySwordPedestal) te);
        }
        return null;
    }

    public int getPedestalRenderId() {
        return 0;
    }

    public EntityPlayer getPlayer(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }
}