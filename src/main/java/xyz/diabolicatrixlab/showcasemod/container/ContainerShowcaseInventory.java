package xyz.diabolicatrixlab.showcasemod.container;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class ContainerShowcaseInventory extends Container {
	private TileEntityShowcase te;

	public ContainerShowcaseInventory(IInventory playerInv, TileEntityShowcase te) {
		this.te = te;
		this.addSlots();
		this.addInventorySlots(playerInv);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.isUsableByPlayer(playerIn) && te.canEdit(playerIn);
	}

	private void addInventorySlots(IInventory inv) {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int x = 8 + col * 18;
				int y = 101 + row * 18;
				this.addSlotToContainer(new Slot(inv, col + row * 9 + 10, x, y));
			}
		}

		for (int col = 0; col < 9; col++) {
			int x = 8 + col * 18;
			int y = 89 + 70;
			this.addSlotToContainer(new Slot(inv, col, x, y));
		}
	}

	private void addSlots() {
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

		// Inventory
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				int x = 8 + col * 18;
				int y = 14 + row * 18;
				this.addSlotToContainer(new SlotInventory(itemHandler, te, col + row * 4, x, y));
			}
		}

		// Coins
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				int x = 97 + col * 18;
				int y = 14 + row * 18;
				this.addSlotToContainer(new SlotInventory(itemHandler, te, 16 + col + row * 4, x, y));
			}
		}
	}

	@Nullable
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {

			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < TileEntityShowcase.SIZE) {
				if (!this.mergeItemStack(itemstack1, TileEntityShowcase.SIZE, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, TileEntityShowcase.SIZE, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}
}
