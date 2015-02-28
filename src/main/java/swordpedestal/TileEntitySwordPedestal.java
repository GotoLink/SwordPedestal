package swordpedestal;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySwordPedestal extends TileEntity implements IInventory {
    public ItemStack sword = null;
    public int rotation = 0;
    public int sinShift;
    public int rotationSpeed = 4;
    public int[] colorRGBA = new int[]{255, 255, 255, 48};
    public boolean isFloating = false;
    public boolean isRotating = false;
    public boolean lightBeamEnabled = false;
    public boolean enchantmentGlimmer = false;
    public String pedestalName = "Basic";
    public int baseRotation;
    public boolean clockwiseRotation;
    public int floatingHeight = 45;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.isRotating) {
            this.rotation += this.rotationSpeed;
            if (this.rotation >= 359 || this.rotation < 0) {
                this.rotation = 0;
            }
        }
        this.sinShift += 2;
        if (this.sinShift > 359) {
            this.sinShift = 0;
        }
    }

    public void markBlockUpdate(){
        if(hasWorldObj() && !getWorldObj().isRemote){
            getWorldObj().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        if(worldObj.isRemote) {
            NBTTagCompound tag = pkt.func_148857_g();
            if (this.xCoord == tag.getInteger("x") && this.yCoord == tag.getInteger("y") && this.zCoord == tag.getInteger("z")) {
                readFromNBT(tag);
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        if(!worldObj.isRemote) {
            NBTTagCompound nbt = new NBTTagCompound();
            writeToNBT(nbt);
            return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbt);
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.sword = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Data"));
        this.rotationSpeed = nbt.getInteger("Speed");
        this.colorRGBA = nbt.getIntArray("Color");
        this.isFloating = nbt.getBoolean("isFloating");
        this.lightBeamEnabled = nbt.getBoolean("lightBeamEnabled");
        this.enchantmentGlimmer = nbt.getBoolean("enchantmentGlimmer");
        this.baseRotation = nbt.getInteger("baseRotation");
        this.pedestalName = nbt.getString("pedestalName");
        this.clockwiseRotation = nbt.getBoolean("clockwiseRotation");
        this.isRotating = nbt.getBoolean("isRotating");
        this.rotation = nbt.getInteger("rotValue");
        this.floatingHeight = nbt.getInteger("floatingHeight");
        if (!SwordPedestalMain.proxy.pedestalNames.contains(this.pedestalName)) {
            this.pedestalName = "Basic";
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Speed", this.rotationSpeed);
        nbt.setIntArray("Color", this.colorRGBA);
        nbt.setBoolean("isFloating", this.isFloating);
        nbt.setBoolean("lightBeamEnabled", this.lightBeamEnabled);
        nbt.setBoolean("enchantmentGlimmer", this.enchantmentGlimmer);
        nbt.setInteger("baseRotation", this.baseRotation);
        nbt.setString("pedestalName", this.pedestalName);
        nbt.setBoolean("clockwiseRotation", this.clockwiseRotation);
        nbt.setBoolean("isRotating", this.isRotating);
        nbt.setInteger("rotValue", this.rotation);
        nbt.setInteger("floatingHeight", this.floatingHeight);
        NBTTagCompound tag = new NBTTagCompound();
        if (this.sword != null) {
            this.sword.writeToNBT(tag);
        }
        nbt.setTag("Data", tag);
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return this.sword;
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        if (this.sword != null) {
            if (this.sword.stackSize <= var2) {
                ItemStack var3 = this.sword;
                this.sword = null;
                return var3;
            }
            ItemStack var3 = this.sword.splitStack(var2);
            if (this.sword.stackSize == 0) {
                this.sword = null;
            }
            return var3;
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int var1) {
        if (this.sword != null) {
            ItemStack var2 = this.sword;
            this.sword = null;
            return var2;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        this.sword = var2;
    }

    @Override
    public String getInventoryName() {
        return "Pedestal";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(this.xCoord, this.yCoord, this.zCoord) < 64;
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return stack != null && (stack.getItem() instanceof ItemSword || SwordPedestalMain.instance.swordItemIdList.contains(GameData.getItemRegistry().getNameForObject(stack.getItem())));
    }
}