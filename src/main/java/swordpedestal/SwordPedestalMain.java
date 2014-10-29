package swordpedestal;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;

@Mod(modid = SwordPedestalMain.MODID, name = "Sword Pedestal")
public class SwordPedestalMain {
    public static final String MODID = "SwordPedestal";
    public final ArrayList<String> swordItemIdList = new ArrayList<String>();
    public static SimpleNetworkWrapper networkWrapper;

    @Mod.Instance(MODID)
    public static SwordPedestalMain instance;

    @SidedProxy(modId = MODID, clientSide = "swordpedestal.client.ClientProxy", serverSide = "swordpedestal.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preLoad(FMLPreInitializationEvent event) {
        Configuration c = new Configuration(event.getSuggestedConfigurationFile());
        addItems(c.getStringList("Item white list", "General", new String[]{""}, "To add items that can be inserted and displayed on the pedestal, by item name.\n All swords are accepted by default."));
        c.save();

        Block pedestal = new BlockPedestalBase(Material.iron).setBlockName("pedestal").setBlockTextureName("stone").setCreativeTab(CreativeTabs.tabDecorations).setHardness(0.8F);
        GameRegistry.registerBlock(pedestal, "pedestal");
        GameRegistry.registerTileEntity(TileEntitySwordPedestal.class, "Pedestal");

        GameRegistry.addRecipe(new ItemStack(pedestal), " G ", "SSS", 'G', Items.gold_ingot, 'S', Blocks.stone_slab);

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        networkWrapper.registerMessage(PacketHandler.class, PacketHandler.GuiChange.class, 0, Side.SERVER);
        proxy.registerRenderers();
    }

    public static void addItems(int[] numbers) {
        for (int i : numbers) {
            instance.swordItemIdList.add(GameData.getItemRegistry().getNameForObject(Item.getItemById(i)));
        }
    }

    public static void addItems(String[] items) {
        for(String item : items) {
            if (GameData.getItemRegistry().containsKey(item))
                instance.swordItemIdList.add(item);
        }
    }
}