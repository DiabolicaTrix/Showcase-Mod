package xyz.diabolicatrixlab.showcasemod.compat;

import net.minecraftforge.fml.common.Loader;
import xyz.diabolicatrixlab.showcasemod.compat.top.TOPCompatibility;

public class CompatibilityHandler {
	
	public static void register(){
		registerTOP();
	}
	
	public static void registerTOP(){
		if (Loader.isModLoaded("theoneprobe")) {
            TOPCompatibility.register();
        }
	}
}
