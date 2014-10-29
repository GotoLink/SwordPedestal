package swordpedestal;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPedestal extends Slot {
    public SlotPedestal(IInventory par2IInventory, int par3, int par4, int par5) {
        super(par2IInventory, par3, par4, par5);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return this.inventory.isItemValidForSlot(getSlotIndex(), is);
    }
}