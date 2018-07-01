package xyz.diabolicatrixlab.showcasemod.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerShowcase extends ItemStackHandler{
	
	private final ItemStackHandler handler;
	
	public ItemStackHandlerShowcase(ItemStackHandler handler) {
		super();
		this.handler = handler;
	}
	
	@Override
	public void setSize(int size) {
		stacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
	}
	
	@Override
	public int getSlots() {
		return handler.getSlots();
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return handler.getStackInSlot(slot);
	}
	
	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		handler.setStackInSlot(slot, stack);
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return stack;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return handler.extractItem(slot, amount, simulate);
	}
	
}
