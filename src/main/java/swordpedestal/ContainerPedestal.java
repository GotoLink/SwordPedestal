package swordpedestal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ContainerPedestal extends Container {
    private final TileEntitySwordPedestal pedestal;

    public ContainerPedestal(IInventory par1InventoryPlayer, TileEntitySwordPedestal te) {
        this.pedestal = te;
        addSlotToContainer(new SlotPedestal(te, 0, 13, 19));

        for (int var3 = 0; var3 < 3; var3++) {
            for (int var4 = 0; var4 < 9; var4++) {
                addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 132 + var3 * 18));
            }
        }

        for (int var3 = 0; var3 < 9; var3++) {
            addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 190));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting p_75132_1_)
    {
        super.addCraftingToCrafters(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.pedestal.rotationSpeed);
        p_75132_1_.sendProgressBarUpdate(this, 1, this.pedestal.colorRGBA[0]);
        p_75132_1_.sendProgressBarUpdate(this, 2, this.pedestal.colorRGBA[1]);
        p_75132_1_.sendProgressBarUpdate(this, 3, this.pedestal.colorRGBA[2]);
        p_75132_1_.sendProgressBarUpdate(this, 4, this.pedestal.colorRGBA[3]);
        p_75132_1_.sendProgressBarUpdate(this, 5, this.pedestal.baseRotation);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int type, int data)
    {
        switch (type){
            case 0:
                this.pedestal.rotationSpeed = MathHelper.clamp_int(data, 0, 359);
                break;
            case 1:case 2:case 3:case 4:
                this.pedestal.colorRGBA[type-1] = MathHelper.clamp_int(data, 0, 255);
                break;
            case 5:
                this.pedestal.baseRotation = MathHelper.clamp_int(data, 0, 359);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int button){
        if(button<5 || button>10)
            return false;
        switch (button) {
            case 5:
                int currentName = SwordPedestalMain.proxy.pedestalNames.indexOf(this.pedestal.pedestalName);
                currentName = SwordPedestalMain.proxy.pedestalNames.size() <= currentName + 1 ? 0: currentName + 1;
                this.pedestal.pedestalName = SwordPedestalMain.proxy.pedestalNames.get(currentName);
                break;
            case 6:
                this.pedestal.lightBeamEnabled = !this.pedestal.lightBeamEnabled;
                break;
            case 7:
                this.pedestal.isFloating = !this.pedestal.isFloating;
                break;
            case 8:
                this.pedestal.enchantmentGlimmer = !this.pedestal.enchantmentGlimmer;
                break;
            case 9:
                this.pedestal.isRotating = !this.pedestal.isRotating;
                break;
            case 10:
                this.pedestal.clockwiseRotation = !this.pedestal.clockwiseRotation;
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par1) {
        ItemStack var2 = null;
        Slot var3 = (Slot) this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack()) {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 == 0) {
                if (!mergeItemStack(var4, 1, 37, true)) {
                    return null;
                }
            } else {
                if ((((Slot) this.inventorySlots.get(0)).getHasStack()) || (!((Slot) this.inventorySlots.get(0)).isItemValid(var4))) {
                    return null;
                }
                ((Slot) this.inventorySlots.get(0)).putStack(var4.splitStack(1));
            }

            if (var4.stackSize == 0) {
                var3.putStack(null);
            } else {
                var3.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize) {
                return null;
            }

            var3.onPickupFromSlot(player, var4);
        }

        return var2;
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        return this.pedestal.isUseableByPlayer(var1);
    }
}