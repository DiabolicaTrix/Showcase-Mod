package xyz.diabolicatrixlab.showcasemod.container;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncItem;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class SlotShowcase extends SlotItemHandler {

	public TileEntityShowcase te;
	public EntityPlayer player;
	
	public SlotShowcase(IItemHandler itemHandler, EntityPlayer player, TileEntityShowcase te, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.te = te;
		this.player = player;
	}
	
	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		
		IItemHandler handler = te.getShowcaseSlotHandler();
		
		ItemStack stack = handler.getStackInSlot(0);
		if(stack == ItemStack.EMPTY){
			te.item = null;
		}
		if(te.item == null || stack.getItem() != te.getItem()){
			te.item = new EntityItem(te.getWorld(), 0, 0, 0, stack);
			if(!te.getWorld().isRemote)
			{
				NetRegistry.network.sendToAll(new PacketSyncItem(te.getPos(), stack));
			}
		}
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return te.canEdit(player);
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return te.canEdit(playerIn);
	}
	
	
}
