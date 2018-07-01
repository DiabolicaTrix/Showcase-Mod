package xyz.diabolicatrixlab.showcasemod.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import xyz.diabolicatrixlab.showcasemod.ItemRegistry;
import xyz.diabolicatrixlab.showcasemod.ShowcaseConfig;
import xyz.diabolicatrixlab.showcasemod.item.ItemCoin;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class CoinUtils {

	public static ITextComponent buy(TileEntityShowcase shop, EntityPlayer player, int price, int quantity) {
		if(shop.getItem() == null) return error("showcasemod.message.nothingtosell");
		if (!canAfford(player, price))
			return error("showcasemod.message.cannotafford");
		if(quantity == 0) return error("showcasemod.message.cannotbuynothing");
		IItemHandler itemHandler = shop.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		
		if(!shop.adminShop)
		{
			int itemsRemaining = 0;
			for(int i = 0; i < itemHandler.getSlots(); i++){
				ItemStack stack = itemHandler.getStackInSlot(i); 
				if(stack != ItemStack.EMPTY && (stack.getItem() == shop.getItem())){
					if(stack.getMetadata() == shop.getItemStack().getMetadata() && stack.getTagCompound() == shop.getItemStack().getTagCompound())
					{
						itemsRemaining += stack.getCount();
					}
				}
			}
			if(itemsRemaining < (quantity * shop.getItemStack().getCount())) return error("showcasemod.message.notenoughitemsinshop");
		}
		
		int bank = removeCoins(player);
		
		InventoryPlayer inv = player.inventory;
		int stacks = Math.floorDiv(quantity * shop.getItemStack().getCount(), 64);
		int lastStack = (quantity * shop.getItemStack().getCount()) % 64;
		for(int i = 0; i < stacks; i++)
		{
			ItemStack stack = new ItemStack(shop.getItem(),64,shop.getItemStack().getMetadata(), shop.getItemStack().getTagCompound());
			ItemHandlerHelper.giveItemToPlayer(player, stack);
		}
		if(lastStack > 0){
			ItemStack stack = new ItemStack(shop.getItem(),lastStack,shop.getItemStack().getMetadata(), shop.getItemStack().getTagCompound());
			ItemHandlerHelper.giveItemToPlayer(player, stack);
		}
		
		if(!shop.adminShop){
			shop.removeItems(shop.getItem(), quantity * shop.getItemStack().getCount());
			shop.addCoins(price);
		}
		
		giveCoins(player, (bank-price));
		return success("showcasemod.message.successbuy");
	}

	public static boolean canAfford(EntityPlayer player, int price) {
		int amount = removeCoins(player, true);
		return price <= amount;
	}
	
	public static int removeCoins(EntityPlayer player, boolean simulate){
		InventoryPlayer inv = player.inventory;
		int amount = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++){
			if(inv.getStackInSlot(i) != ItemStack.EMPTY && inv.getStackInSlot(i).getItem() instanceof ItemCoin){
				ItemStack stack = inv.getStackInSlot(i);
				switch (ResourceLocation.splitObjectName(stack.getItem().getRegistryName().toString())[1]) {
				case "copper_coin":
					amount += stack.getCount();
					break;
				case "silver_coin":
					amount += stack.getCount() * ShowcaseConfig.silverValue;
					break;
				case "gold_coin":
					amount += stack.getCount() * ShowcaseConfig.goldValue;
					break;
				}
				if(!simulate){
					inv.setInventorySlotContents(i, ItemStack.EMPTY);
				}
			}
		}
		return amount;
	}
	
	public static int removeCoins(EntityPlayer player){
		return removeCoins(player, false);
	}
	
	public static void giveCoins(EntityPlayer player, int price){
		int gold = Math.floorDiv(price, ShowcaseConfig.goldValue);
		int silver = Math.floorDiv(price - (gold * ShowcaseConfig.goldValue), ShowcaseConfig.silverValue);
		int copper = price - (gold * ShowcaseConfig.goldValue + silver * ShowcaseConfig.silverValue);
		
		if(gold > 0) ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ItemRegistry.goldCoin, gold));
		if(silver > 0) ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ItemRegistry.silverCoin, silver));
		if(copper > 0) ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ItemRegistry.copperCoin, copper));

	}
	
	public static ITextComponent error(String msg){
		return new TextComponentTranslation(msg, new Object[0]).setStyle(new Style().setColor(TextFormatting.DARK_RED));
	}
	
	public static ITextComponent success(String msg){
		return new TextComponentTranslation(msg, new Object[0]).setStyle(new Style().setColor(TextFormatting.GREEN));
	}

}
