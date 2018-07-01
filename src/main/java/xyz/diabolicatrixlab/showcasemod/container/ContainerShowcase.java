package xyz.diabolicatrixlab.showcasemod.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class ContainerShowcase extends Container {

	private TileEntityShowcase te;
	public EntityPlayer player;
	
	public ContainerShowcase(EntityPlayer player, TileEntityShowcase te) {
		this.te = te;
		this.player = player;
		this.addSlots();
		this.addInventorySlots(player.inventory);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.isUsableByPlayer(playerIn);
	}
	
	private void addInventorySlots(IInventory inv){
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 9; col++){
				int x = 8 + col * 18;
				int y = 101 + row * 18; 
				this.addSlotToContainer(new Slot(inv, col + row * 9 + 10, x, y));
			}
		}
		
		for(int col = 0; col < 9; col++){
			int x = 8 + col * 18;
			int y = 89 + 70;
			this.addSlotToContainer(new Slot(inv, col, x, y));
		}
	}
	
	private void addSlots()
	{
		IItemHandler itemHandler = te.getShowcaseSlotHandler();
		this.addSlotToContainer(new SlotShowcase(itemHandler, player, te, 0, 126, 23));
	}
	
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

        return ItemStack.EMPTY;
    }
    
    

}
