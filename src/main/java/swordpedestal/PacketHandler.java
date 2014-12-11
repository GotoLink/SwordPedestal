package swordpedestal;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public final class PacketHandler implements IMessageHandler<PacketHandler.GuiChange,IMessage>{

    @Override
    public IMessage onMessage(GuiChange message, MessageContext ctx) {
        message.handle(SwordPedestalMain.proxy.getPlayer(ctx), ctx.side);
        return null;
    }

    public static void sendGuiChange(int id, TileEntity te, double value) {
        if(id>12 && id <22)
            SwordPedestalMain.networkWrapper.sendToServer(new GuiChange((byte)id, te.xCoord, te.yCoord, te.zCoord, value));
    }

    public static class GuiChange implements IMessage{
        private byte buttonId;
        private int x,y,z;
        private double value;

        public GuiChange(){}
        public GuiChange(byte id, int x, int y, int z, double value){
            this.buttonId = id;
            this.x = x;
            this.y = y;
            this.z = z;
            this.value = value;
        }
        @Override
        public void fromBytes(ByteBuf buf) {
            buttonId = (byte) buf.readUnsignedByte();
            x = buf.readInt();
            y = buf.readInt();
            z = buf.readInt();
            value = buf.readDouble();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(buttonId);
            buf.writeInt(x);
            buf.writeInt(y);
            buf.writeInt(z);
            buf.writeDouble(value);
        }

        public void handle(EntityPlayer player, Side side) {
            if(buttonId > 12 && buttonId < 22 && y > 0 && side.isServer() && player.openContainer instanceof ContainerPedestal) {
                TileEntity tileEntity = player.getEntityWorld().getTileEntity(x, y, z);
                if(tileEntity instanceof TileEntitySwordPedestal) {
                    switch(buttonId){
                        case 13:
                            ((TileEntitySwordPedestal)tileEntity).rotationSpeed = (int) MathHelper.clamp_double(value * 15.0F + 1, 0, 359);
                            break;
                        case 19:
                            ((TileEntitySwordPedestal)tileEntity).colorRGBA[3] = (int) MathHelper.clamp_double(value * 95.0F, 0, 255);
                            break;
                        case 16:case 17:case 18:
                            ((TileEntitySwordPedestal)tileEntity).colorRGBA[buttonId-16] = (int) MathHelper.clamp_double(value * 255.0F, 0, 255);
                            break;
                        case 14:
                            ((TileEntitySwordPedestal)tileEntity).baseRotation = (int) MathHelper.clamp_double(value, 0, 359);
                            break;
                        case 21:
                            if (!((TileEntitySwordPedestal)tileEntity).isRotating) {
                                ((TileEntitySwordPedestal)tileEntity).rotation = (int) MathHelper.clamp_double(value, 0, 359);
                                break;
                            }
                        default:
                            return;
                    }
                    player.getEntityWorld().markBlockForUpdate(x,y,z);
                }
            }
        }
    }
}