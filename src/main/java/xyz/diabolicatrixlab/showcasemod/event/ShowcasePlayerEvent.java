package xyz.diabolicatrixlab.showcasemod.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.ShowcaseConfig;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncConfig;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class ShowcasePlayerEvent 
{
	@SubscribeEvent
	public void onBlockBreak(PlayerEvent.BreakSpeed event){
		if(event.getEntityPlayer().world.getTileEntity(event.getPos()) instanceof TileEntityShowcase){
			TileEntityShowcase te = (TileEntityShowcase) event.getEntityPlayer().world.getTileEntity(event.getPos());
			if(!te.isOwner(event.getEntityPlayer())){
				event.setNewSpeed(0.0F);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event){
		if(!event.player.world.isRemote){
			NetRegistry.network.sendTo(new PacketSyncConfig(ShowcaseConfig.copperValue, ShowcaseConfig.silverValue, ShowcaseConfig.goldValue, ShowcaseConfig.isOffline), (EntityPlayerMP) event.player);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event){
		if(event.player.world.isRemote){
			ShowcaseConfig.resetConfig();
		}
	}
}
