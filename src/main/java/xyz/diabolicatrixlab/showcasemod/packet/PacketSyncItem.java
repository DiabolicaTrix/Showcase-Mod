package xyz.diabolicatrixlab.showcasemod.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class PacketSyncItem implements IMessage {

	public BlockPos pos;
	public ItemStack stack;
	
	public PacketSyncItem(BlockPos pos, ItemStack stack) {
		this.pos = pos;
		this.stack = stack;
	}
	
	public PacketSyncItem() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.stack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		ByteBufUtils.writeItemStack(buf, stack);
	}
	
	public static class Handler implements IMessageHandler<PacketSyncItem, IMessage> {

		@Override
		public IMessage onMessage(PacketSyncItem message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message,ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(PacketSyncItem message, MessageContext ctx){
			World world = Minecraft.getMinecraft().world;
			if(world.isBlockLoaded(message.pos) && (world.getTileEntity(message.pos) instanceof TileEntityShowcase)){
				TileEntityShowcase te = (TileEntityShowcase) world.getTileEntity(message.pos);
				te.item = new EntityItem(te.getWorld(), 0, 0, 0, message.stack);
			}
		}
		
	}

}
