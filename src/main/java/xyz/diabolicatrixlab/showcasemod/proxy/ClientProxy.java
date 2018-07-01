package xyz.diabolicatrixlab.showcasemod.proxy;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import xyz.diabolicatrixlab.showcasemod.BlockRegistry;
import xyz.diabolicatrixlab.showcasemod.ItemRegistry;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncItem;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncOwner;

/**
 * Author: DiabolicaTrix
 * Date: 2017-01-19
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(File configFile)
    {
        super.preInit(configFile);
    }

    @Override
    public void init()
    {
        super.init();
    }
    
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
    	BlockRegistry.registerModels();
    	ItemRegistry.registerModels();
    }
}
