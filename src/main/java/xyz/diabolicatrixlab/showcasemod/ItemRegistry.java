package xyz.diabolicatrixlab.showcasemod;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import xyz.diabolicatrixlab.showcasemod.item.ItemCoin;
import xyz.diabolicatrixlab.showcasemod.item.ItemShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.item.ItemShowcaseModifier;

@Mod.EventBusSubscriber(modid = ShowcaseMod.MODID)
public class ItemRegistry
{
	@GameRegistry.ObjectHolder(ShowcaseMod.MODID + ":" + "copper_coin")
	public static ItemCoin copperCoin;
	
	@GameRegistry.ObjectHolder(ShowcaseMod.MODID + ":" + "silver_coin")
	public static ItemCoin silverCoin;
	
	@GameRegistry.ObjectHolder(ShowcaseMod.MODID + ":" + "gold_coin")
	public static ItemCoin goldCoin;
	
	@GameRegistry.ObjectHolder(ShowcaseMod.MODID + ":" + "showcase_modifier")
	public static ItemShowcaseModifier showcaseModifier;

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();

		final Item[] items = { new ItemCoin("copper"), new ItemCoin("silver"), new ItemCoin("gold"), new ItemShowcaseModifier()};

		registry.registerAll(items);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerModels(){
		copperCoin.registerModel();
		silverCoin.registerModel();
		goldCoin.registerModel();
		showcaseModifier.registerModel();
	}

}
