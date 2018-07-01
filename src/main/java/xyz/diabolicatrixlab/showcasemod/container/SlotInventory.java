package xyz.diabolicatrixlab.showcasemod.container;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class SlotInventory extends SlotItemHandler {
	
	private TileEntityShowcase te;

	public SlotInventory(IItemHandler itemHandler, TileEntityShowcase te, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.te = te;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return te.isItemValidForSlot(getItemHandler(), slotNumber, stack);
	}
}
