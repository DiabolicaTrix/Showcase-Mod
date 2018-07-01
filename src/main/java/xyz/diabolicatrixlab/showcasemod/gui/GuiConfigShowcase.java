package xyz.diabolicatrixlab.showcasemod.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import xyz.diabolicatrixlab.showcasemod.ShowcaseConfig;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.proxy.CommonProxy;

public class GuiConfigShowcase extends GuiConfig {
	
	public GuiConfigShowcase(GuiScreen parent) {
		super(parent, getElements(), ShowcaseMod.MODID, false, false, CommonProxy.config.getConfigFile().getAbsolutePath());	
	}
	
	public static List<IConfigElement> getElements(){
		List<IConfigElement> elements = new ArrayList<IConfigElement>();
		elements.addAll(new ConfigElement(CommonProxy.config.getCategory(ShowcaseConfig.CATEGORY_GENERAL)).getChildElements());
		elements.addAll(new ConfigElement(CommonProxy.config.getCategory(ShowcaseConfig.CATEGORY_ECONOMY)).getChildElements());
		return elements;
	}
	
}
