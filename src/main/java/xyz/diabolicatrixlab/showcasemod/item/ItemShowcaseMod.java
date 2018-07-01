package xyz.diabolicatrixlab.showcasemod.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;

public class ItemShowcaseMod extends Item {
	
	public ItemShowcaseMod(String name) {
		setName(name);
		setCreativeTab(ShowcaseMod.showcaseTab);
	}
	
	public void setName(String name){
		this.setRegistryName(new ResourceLocation(ShowcaseMod.MODID, name));
		this.setUnlocalizedName(getRegistryName().toString());
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModel(){
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

}
