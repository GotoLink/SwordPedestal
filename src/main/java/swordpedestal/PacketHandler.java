package swordpedestal;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public final class PacketHandler implements IMessageHandler<PacketHandler.GuiChange,IMessage>{

    @Override
    public IMessage onMessage(GuiChange message, MessageContext ctx) {
        message.handle(SwordPedestalMain.proxy.getPlayer(ctx));
        return null;
    }

    public static void sendGuiChange(int id, double value) {
        if(id>12 && id <22)
            SwordPedestalMain.networkWrapper.sendToServer(new GuiChange((byte)id, value));
    }

    public static class GuiChange implements IMessage{
        private byte buttonId;
        private double value;

        public GuiChange(){}
        public GuiChange(byte id, double value){
            this.buttonId = id;
            this.value = value;
        }
        @Override
        public void fromBytes(ByteBuf buf) {
            buttonId = (byte) buf.readUnsignedByte();
            value = buf.readDouble();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(buttonId);
            buf.writeDouble(value);
        }

        public void handle(EntityPlayer player) {
            if(buttonId > 12 && buttonId < 22 && !player.worldObj.isRemote && player.openContainer instanceof ContainerPedestal) {
                TileEntitySwordPedestal tileEntity = ((ContainerPedestal) player.openContainer).pedestal;
                if(tileEntity != null) {
                    switch(buttonId){
                        case 13:
                            tileEntity.rotationSpeed = (int) MathHelper.clamp_double(value * 15.0F + 1, 0, 359);
                            break;
                        case 19:
                            tileEntity.colorRGBA[3] = (int) MathHelper.clamp_double(value * 95.0F, 0, 255);
                            break;
                        case 16:case 17:case 18:
                            tileEntity.colorRGBA[buttonId-16] = (int) MathHelper.clamp_double(value * 255.0F, 0, 255);
                            break;
                        case 14:
                            tileEntity.baseRotation = (int) MathHelper.clamp_double(value, -180, 180);
                            break;
                        case 15:
                            if(tileEntity.isFloating) {
                                tileEntity.floatingHeight = (int) MathHelper.clamp_double(value, 0, 250);
                                break;
                            }
                        case 21:
                            if (!tileEntity.isRotating) {
                                tileEntity.rotation = (int) MathHelper.clamp_double(value, 0, 359);
                                break;
                            }
                        default:
                            return;
                    }
                    player.openContainer.detectAndSendChanges();
                }
            }
        }
    }
}