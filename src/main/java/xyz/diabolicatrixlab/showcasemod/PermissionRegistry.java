package xyz.diabolicatrixlab.showcasemod;

import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public class PermissionRegistry {
	
	public static void registerNodes(){
		PermissionAPI.registerNode("showcasemod.shops.edit", DefaultPermissionLevel.OP, "Allows player to edit shops and create admin shops.");
	}

}
