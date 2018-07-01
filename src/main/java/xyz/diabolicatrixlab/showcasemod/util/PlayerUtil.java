package xyz.diabolicatrixlab.showcasemod.util;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import xyz.diabolicatrixlab.showcasemod.ShowcaseConfig;

public class PlayerUtil {
	
	public static UUID getUniqueID(EntityPlayer player){
		if(ShowcaseConfig.isOffline){
			return EntityPlayer.getOfflineUUID(player.getName());
		}
		return player.getUniqueID();
	}
	
}
