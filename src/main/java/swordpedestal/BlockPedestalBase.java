package swordpedestal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockPedestalBase extends BlockContainer {
    private static final float MIN_XZ = 0.0625F, MAX_XZ = 0.9375F;
    public BlockPedestalBase(Material par2Material) {
        super(par2Material);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z) {
        if (w.getBlockMetadata(x, y, z) == 0) {
            float height = 0.4375F;
            TileEntitySwordPedestal te = (TileEntitySwordPedestal) w.getTileEntity(x, y, z);
            if (te != null && te.getStackInSlot(0) != null) {
                height = 1.5F;
                if (te.isFloating) {
                    height = 2.0F;
                }
            }
            return AxisAlignedBB.getBoundingBox(x + MIN_XZ, y, z + MIN_XZ, x + MAX_XZ, y + height, z + MAX_XZ);
        }
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iba, int x, int y, int z) {
        int meta = iba.getBlockMetadata(x, y, z);

        if (meta == 0) {
            TileEntitySwordPedestal te = (TileEntitySwordPedestal) iba.getTileEntity(x, y, z);
            if (te != null && te.getStackInSlot(0) != null && te.isFloating)
                setBlockBounds(MIN_XZ, 0.01F, MIN_XZ, MAX_XZ, 2.0F, MAX_XZ);
            else
                setBlockBounds(MIN_XZ, 0.01F, MIN_XZ, MAX_XZ, 1.5F, MAX_XZ);
        }
        else if (meta == 1) {
            TileEntitySwordPedestal te = (TileEntitySwordPedestal) iba.getTileEntity(x, y - 1, z);
            if (te != null && te.getStackInSlot(0) != null && te.isFloating)
                setBlockBounds(MIN_XZ, -0.99F, MIN_XZ, MAX_XZ, 1.0F, MAX_XZ);
            else
                setBlockBounds(MIN_XZ, -0.99F, MIN_XZ, MAX_XZ, 0.5F, MAX_XZ);
        }
    }

    @Override
    public void onNeighborBlockChange(World w, int x, int y, int z, Block neighborId) {
        int meta = w.getBlockMetadata(x, y, z);

        if (meta == 1) {
            if (w.getBlock(x, y - 1, z) != this || w.getBlockMetadata(x, y - 1, z) != 0) {
                w.setBlockToAir(x, y, z);
            }

        } else if (w.getBlock(x, y + 1, z) != this || w.getBlockMetadata(x, y + 1, z) != 1) {
            w.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void breakBlock(World w, int x, int y, int z, Block par5, int par6) {
        TileEntitySwordPedestal te = (TileEntitySwordPedestal) w.getTileEntity(x, y, z);
        if (te != null && te.getStackInSlot(0)!=null && !w.isRemote) {
            float rand1 = w.rand.nextFloat() * 0.8F + 0.1F;
            float rand2 = w.rand.nextFloat() * 0.8F + 0.1F;
            float rand3 = w.rand.nextFloat() * 0.8F + 0.1F;

            w.spawnEntityInWorld(new EntityItem(w, x + rand1, y + rand2, z + rand3, te.getStackInSlot(0)));
        }

        super.breakBlock(w, x, y, z, par5, par6);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        TileEntitySwordPedestal te = null;

        int meta = world.getBlockMetadata(x, y, z);

        if (meta == 0) {
            te = (TileEntitySwordPedestal) world.getTileEntity(x, y, z);
        } else if (meta == 1) {
            te = (TileEntitySwordPedestal) world.getTileEntity(x, y - 1, z);
        }

        if (te != null) {
            if (te.getStackInSlot(0) != null) {
                ItemStack tmp = player.getHeldItem();
                player.inventory.setInventorySlotContents(player.inventory.currentItem, te.getStackInSlot(0));
                if (tmp != null && !world.isRemote) {
                    ForgeHooks.onPlayerTossEvent(player, tmp, true);
                }
                te.setInventorySlotContents(0, null);
            }
            else if (te.isItemValidForSlot(0, player.getHeldItem())) {
                te.setInventorySlotContents(0, player.getHeldItem());
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            else if (te.isUseableByPlayer(player)) {
                player.openGui(SwordPedestalMain.instance, 0, world, x, y - meta, z);
            }
        }

        return true;
    }

    @Override
    public int getRenderType() {
        return SwordPedestalMain.proxy.getPedestalRenderId();
    }

    @Override
    public void onBlockAdded(World w, int x, int y, int z) {
        super.onBlockAdded(w, x, y, z);
        w.setTileEntity(x, y, z, createNewTileEntity(w, w.getBlockMetadata(x, y, z)));
        if (w.getBlockMetadata(x, y, z) == 0 && w.isAirBlock(x, y + 1, z)) {
            w.setBlock(x, y + 1, z, this, 1, 3);
        }
        onNeighborBlockChange(w, x, y, z, this);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == 0) {
            return new TileEntitySwordPedestal();
        }
        return null;
    }
}