package xyz.diabolicatrixlab.showcasemod.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemShowcaseModifier extends ItemShowcaseMod {

	public ItemShowcaseModifier() {
		super("showcase_modifier");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote){
			ItemStack is = playerIn.getHeldItemMainhand();
			if(is.getItem() instanceof ItemShowcaseModifier && playerIn.isSneaking()){
				NBTTagCompound nbt = is.hasTagCompound() ? is.getTagCompound() : new NBTTagCompound();
				int mode = nbt.hasKey("Mode") ? nbt.getInteger("Mode") : 0;
				if(mode == 1) {
					mode = 0;
				} else {
					mode = 1;
				}
				nbt.setInteger("Mode", mode);
				is.setTagCompound(nbt);
				playerIn.sendMessage(new TextComponentTranslation("showcasemod.message.mode", new Object[]{new TextComponentTranslation(modeToString(mode))}).setStyle(new Style().setColor(TextFormatting.GREEN)));
			}
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		int mode = nbt.hasKey("Mode") ? nbt.getInteger("Mode") : 0;
		tooltip.add("Mode: " + I18n.format(modeToString(mode)));
	}
	
	public String modeToString(int mode){
		switch(mode){
		case 0: 
			return "showcasemod.mode.edit";
		case 1:
			return "showcasemod.mode.toggle";
		}
		return "showcasemod.mode.edit";
	}
	
}
