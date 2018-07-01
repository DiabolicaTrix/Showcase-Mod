package xyz.diabolicatrixlab.showcasemod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import xyz.diabolicatrixlab.showcasemod.block.BlockShowcase;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

/**
 * Author: DiabolicaTrix Date: 2017-01-19
 */
@Mod.EventBusSubscriber(modid = ShowcaseMod.MODID)
public class BlockRegistry {
	
	@GameRegistry.ObjectHolder(ShowcaseMod.MODID + ":" + "showcase_block")
	public static BlockShowcase showcaseBlock;
	public static Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		Block[] blocks = { new BlockShowcase(Material.GLASS) };
		registry.registerAll(blocks);
		GameRegistry.registerTileEntity(TileEntityShowcase.class, new ResourceLocation(ShowcaseMod.MODID, "showcaseblock"));
	}

	@SubscribeEvent
	public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
		ItemBlock[] items = { new ItemBlock(showcaseBlock), };

		IForgeRegistry<Item> registry = event.getRegistry();

		for (ItemBlock item : items) {
			Block block = item.getBlock();
			ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(),
					"Block %s has null registry name", block);
			registry.register(item.setRegistryName(registryName));
			ITEM_BLOCKS.add(item);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerModels(){
		showcaseBlock.registerModel();
	}
}
