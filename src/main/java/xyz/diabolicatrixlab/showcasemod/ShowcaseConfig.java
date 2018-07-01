package xyz.diabolicatrixlab.showcasemod;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import xyz.diabolicatrixlab.showcasemod.proxy.CommonProxy;

public class ShowcaseConfig {
	
	public static final String CATEGORY_GENERAL = "general";
	public static final String CATEGORY_ECONOMY = "economy";
	
	public static int copperValue = 1;
	public static int silverValue = 4;
	public static int goldValue = 16;
	public static boolean isOffline = false;
	
	public static void readConfig(){
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			ShowcaseConfig.initConfig(cfg);
		} catch (Exception e) {
			ShowcaseMod.logger.log(Level.ERROR, "Could not read config file!", e);
		} finally {
			if(cfg.hasChanged()){
				cfg.save();
			}
		}
	}
	
	public static void initConfig(Configuration cfg){
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General");
		isOffline = cfg.getBoolean("isOffline", CATEGORY_GENERAL, false, "Is your server offline?");
		cfg.addCustomCategoryComment(CATEGORY_ECONOMY, "Economy");
		copperValue = cfg.getInt("copperValue", CATEGORY_ECONOMY, 1, 1, Integer.MAX_VALUE, "How many coins of the precedent type you need to make one of this type");
		silverValue = cfg.getInt("silverValue", CATEGORY_ECONOMY, 4, 4, Integer.MAX_VALUE, "How many coins of the precedent type you need to make one of this type");
		goldValue = cfg.getInt("goldValue", CATEGORY_ECONOMY, 16, 16, Integer.MAX_VALUE, "How many coins of the precedent type you need to make one of this type");
	}
	
	public static void resetConfig(){
		readConfig();
	}

}
