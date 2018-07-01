package xyz.diabolicatrixlab.showcasemod;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.diabolicatrixlab.showcasemod.proxy.CommonProxy;

@Mod(modid = ShowcaseMod.MODID, version = ShowcaseMod.VERSION, guiFactory = ShowcaseMod.GUIFACTORY)
public class ShowcaseMod
{
    public static final String MODID = "showcasemod";
    public static final String VERSION = "1.0";
    public static final String GUIFACTORY = "xyz.diabolicatrixlab.showcasemod.GuiFactoryShowcase";

    @Mod.Instance(ShowcaseMod.MODID)
    public static ShowcaseMod instance;

    @SidedProxy(clientSide = "xyz.diabolicatrixlab.showcasemod.proxy.ClientProxy", serverSide = "xyz.diabolicatrixlab.showcasemod.proxy.ServerProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    public static CreativeTabs showcaseTab = new CreativeTabs("showcase_tab")
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(BlockRegistry.showcaseBlock);
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event.getModConfigurationDirectory());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }
}
