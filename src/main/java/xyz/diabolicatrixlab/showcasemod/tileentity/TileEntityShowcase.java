package xyz.diabolicatrixlab.showcasemod.tileentity;

import java.util.UUID;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.server.permission.PermissionAPI;
import xyz.diabolicatrixlab.showcasemod.ItemRegistry;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.ShowcaseConfig;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.item.ItemCoin;
import xyz.diabolicatrixlab.showcasemod.item.ItemShowcaseModifier;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncAdminShop;
import xyz.diabolicatrixlab.showcasemod.packet.PacketSyncOwner;
import xyz.diabolicatrixlab.showcasemod.packet.PacketUpdateShowcaseGui;
import xyz.diabolicatrixlab.showcasemod.util.PlayerUtil;

public class TileEntityShowcase extends TileEntity implements ITickable {

	public static final int SIZE = 32;
	private String name;

	public int coinId = 0;
	public int price = 1;
	public int totalPrice = 0;

	public float customAge = 0;
	public EntityItem item;

	public boolean adminShop = false;

	public UUID ownerId;

	protected ItemStackHandler showcaseSlot;
	protected ItemStackHandler inventorySlots;
	private ItemStackHandler showcaseSlotWrapper;

	public TileEntityShowcase() {
		showcaseSlot = new ItemStackHandler(1) {
			@Override
			protected void onContentsChanged(int slot) {
				TileEntityShowcase.this.markDirty();
			}
		};
		inventorySlots = new ItemStackHandler(SIZE) {
			@Override
			protected void onContentsChanged(int slot) {
				TileEntityShowcase.this.markDirty();
			}
		};
		showcaseSlotWrapper = new ItemStackHandlerShowcase(showcaseSlot);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey(ShowcaseMod.MODID + ":Values")) {
			int[] values = compound.getIntArray(ShowcaseMod.MODID + ":Values");
			this.coinId = values[0];
			this.price = values[1];
		}
		if (compound.hasUniqueId(ShowcaseMod.MODID + ":OwnerID")) {
			this.ownerId = compound.getUniqueId(ShowcaseMod.MODID + ":OwnerID");
		}
		if(compound.hasKey(ShowcaseMod.MODID + ":AdminShop")){
			this.adminShop = compound.getBoolean(ShowcaseMod.MODID + ":AdminShop");
		}
		
		if (compound.hasKey(ShowcaseMod.MODID + ":ShowcaseSlot")) {
			showcaseSlot.deserializeNBT((NBTTagCompound) compound.getTag(ShowcaseMod.MODID + ":ShowcaseSlot"));
			if(showcaseSlot.getStackInSlot(0) != ItemStack.EMPTY) { 
				this.item = new EntityItem(this.getWorld(), 0,0,0, showcaseSlot.getStackInSlot(0)); 
			}
			inventorySlots.deserializeNBT((NBTTagCompound) compound.getTag(ShowcaseMod.MODID + ":InventorySlots"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setIntArray(ShowcaseMod.MODID + ":Values", new int[] { this.coinId, this.price });
		if(this.ownerId != null){
			compound.setUniqueId(ShowcaseMod.MODID + ":OwnerID", this.ownerId);
		}
		compound.setBoolean(ShowcaseMod.MODID + ":AdminShop", this.adminShop);
		compound.setTag(ShowcaseMod.MODID + ":ShowcaseSlot", showcaseSlot.serializeNBT());
		compound.setTag(ShowcaseMod.MODID + ":InventorySlots", inventorySlots.serializeNBT());
		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			this.markDirty();
			if (world != null && world.getBlockState(pos).getBlock() != getBlockType()) {
				return (T) new CombinedInvWrapper(inventorySlots, showcaseSlotWrapper);
			}
			if(this.adminShop){
				return null;
			}
			if (facing == null) {
				return (T) new CombinedInvWrapper(inventorySlots, showcaseSlotWrapper);
			}else {
				return (T) inventorySlots;
			}
		}
		return super.getCapability(capability, facing);
	}

	public boolean isItemValidForSlot(IItemHandler handler, int slot, ItemStack stack) {
		boolean isCoin = stack.getItem() instanceof ItemCoin;
		return handler instanceof ItemStackHandlerShowcase ? true : slot >= 0 && slot <= 15 ? !isCoin : isCoin;
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}

	@Override
	public void update() {
	}

	/*
	 * Sync with server
	 */
	public void update(int coinId, int price) {

		this.coinId = coinId;
		this.price = price;
		this.markDirty();
		NetRegistry.network.sendToServer(new PacketUpdateShowcaseGui(this, coinId, price));
	}

	/*
	 * Removes the quantity of items bought
	 */
	public void removeItems(Item item, int quantity) {
		for (int i = 0; i < this.inventorySlots.getSlots(); i++) {
			ItemStack stack = this.inventorySlots.getStackInSlot(i);
			if (stack != ItemStack.EMPTY && (this.getItem() == stack.getItem())) {
				if (this.getItemStack().getMetadata() == stack.getMetadata()
						&& this.getItemStack().getTagCompound() == stack.getTagCompound()) {
					if (quantity < stack.getCount()) {
						ItemStack is = stack.copy();
						is.setCount(is.getCount() - quantity);
						this.inventorySlots.setStackInSlot(i, is);
						return;
					}
					this.inventorySlots.setStackInSlot(i, ItemStack.EMPTY);
					quantity -= stack.getCount();
				}
			}
		}
	}

	/*
	 * Add coins in the inventory after purchase
	 */
	public void addCoins(int quantity) {
		int gold = Math.floorDiv(quantity, ShowcaseConfig.goldValue);
		int silver = Math.floorDiv(quantity - (gold * ShowcaseConfig.goldValue), ShowcaseConfig.silverValue);
		int copper = quantity - (gold * ShowcaseConfig.goldValue + silver * ShowcaseConfig.silverValue);

		if (gold > 0)
			ItemHandlerHelper.insertItem(this.inventorySlots, new ItemStack(ItemRegistry.goldCoin, gold), false);
		if (silver > 0)
			ItemHandlerHelper.insertItem(this.inventorySlots, new ItemStack(ItemRegistry.silverCoin, silver), false);
		if (copper > 0)
			ItemHandlerHelper.insertItem(this.inventorySlots, new ItemStack(ItemRegistry.copperCoin, copper), false);

	}

	/*
	 * Sets the showcase owner and syncs with everyone Supposed to be called on
	 * server side
	 */
	public void setOwner(UUID uid) {
		this.ownerId = uid;
		this.markDirty();
		NetRegistry.network.sendToAll(new PacketSyncOwner(pos, uid));
	}

	/*
	 * Checks if the player is the owner of the showcase
	 */
	public boolean isOwner(EntityPlayer player) {
		UUID uid = PlayerUtil.getUniqueID(player);
		return ownerId == null ? false : this.ownerId.equals(uid);
	}

	/*
	 * Return the item displayed in the showcase
	 */
	public Item getItem() {
		if (this.item == null)
			return null;
		return this.item.getItem().getItem();
	}

	/*
	 * Return the ItemStack displayed (quicker access)
	 */
	public ItemStack getItemStack() {
		if (this.item == null)
			return ItemStack.EMPTY;
		return this.item.getItem();
	}

	/*
	 * Converts the price to copper
	 */
	public int getTotal(int quantity) {
		int value = this.coinId == 0 ? ShowcaseConfig.copperValue
				: this.coinId == 1 ? ShowcaseConfig.silverValue : ShowcaseConfig.goldValue;
		return this.price * quantity * value;
	}
	
	public IItemHandler getShowcaseSlotHandler(){
		return this.showcaseSlot;
	}
	
	public void toggleAdminShop(EntityPlayer player){
		this.adminShop = !this.adminShop;
		this.markDirty();
		NetRegistry.network.sendToAll(new PacketSyncAdminShop(this.pos, PlayerUtil.getUniqueID(player), this.adminShop));
	}
	
	public boolean canEdit(EntityPlayer player){
		return isOwner(player) || (player.getHeldItemMainhand().getItem() instanceof ItemShowcaseModifier);
	}
}
