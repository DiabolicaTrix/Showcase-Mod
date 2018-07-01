package xyz.diabolicatrixlab.showcasemod;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xyz.diabolicatrixlab.showcasemod.packet.PacketBuyItem;
import xyz.diabolicatrixlab.showcasemod.packet.PacketOpenGui;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncAdminShop;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncConfig;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncItem;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncOwner;
import xyz.diabolicatrixlab.showcasemod.packet.PacketUpdateShowcase;
import xyz.diabolicatrixlab.showcasemod.packet.PacketUpdateShowcaseGui;

public class NetRegistry 
{
	public static int packetID = 0;
	public static SimpleNetworkWrapper network;
	
	public static void registerChannels(){
		network = NetworkRegistry.INSTANCE.newSimpleChannel(ShowcaseMod.MODID);
	}
	
	public static int nextID(){
		return packetID++;
	}
	
	public static void registerPackets(){
		network.registerMessage(PacketUpdateShowcaseGui.Handler.class, PacketUpdateShowcaseGui.class, nextID(), Side.SERVER);
		network.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, nextID(), Side.SERVER);
		network.registerMessage(PacketBuyItem.Handler.class, PacketBuyItem.class, nextID(), Side.SERVER);
		network.registerMessage(PacketSyncOwner.Handler.class, PacketSyncOwner.class, nextID(), Side.CLIENT);
		network.registerMessage(PacketSyncItem.Handler.class, PacketSyncItem.class, nextID(), Side.CLIENT);
		network.registerMessage(PacketUpdateShowcase.Handler.class, PacketUpdateShowcase.class, nextID(), Side.CLIENT);
		network.registerMessage(PacketSyncConfig.Handler.class, PacketSyncConfig.class, nextID(), Side.CLIENT);
		network.registerMessage(PacketSyncAdminShop.Handler.class, PacketSyncAdminShop.class, nextID(), Side.CLIENT);
	}
}
