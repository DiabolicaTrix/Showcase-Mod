package xyz.diabolicatrixlab.showcasemod.proxy;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.server.permission.PermissionAPI;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.PermissionRegistry;
import xyz.diabolicatrixlab.showcasemod.ShowcaseConfig;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.compat.CompatibilityHandler;
import xyz.diabolicatrixlab.showcasemod.event.ShowcasePlayerEvent;

/**
 * Author: DiabolicaTrix
 * Date: 2017-01-19
 */
public class CommonProxy
{
	public static Configuration config;
	
    public void preInit(File cfg)
    {
    	config = new Configuration(new File(cfg.getPath(), "showcasemod_config.cfg"));
    	ShowcaseConfig.readConfig();
    }

    public void init()
    {
    	NetworkRegistry.INSTANCE.registerGuiHandler(ShowcaseMod.instance, new GuiProxy());
    	
    	NetRegistry.registerChannels();
    	NetRegistry.registerPackets();
    	
    	PermissionRegistry.registerNodes();
    	
    	CompatibilityHandler.register();
    	
    	MinecraftForge.EVENT_BUS.register(new ShowcasePlayerEvent());
    }
    
    public void postInit()
    {
    	if(config.hasChanged()){
    		config.save();
    	}
    }
    
}
