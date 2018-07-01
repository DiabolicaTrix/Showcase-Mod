package xyz.diabolicatrixlab.showcasemod.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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

public class PacketSyncOwner implements IMessage {

	public BlockPos pos;
	public UUID uid;
	
	public PacketSyncOwner(BlockPos pos, UUID uid) {
		this.pos = pos;
		this.uid = uid;
	}
	
	public PacketSyncOwner() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.uid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		ByteBufUtils.writeUTF8String(buf, uid.toString());
	}

	public static class Handler implements IMessageHandler<PacketSyncOwner, IMessage>{

		@Override
		public IMessage onMessage(PacketSyncOwner message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(PacketSyncOwner message, MessageContext ctx){
			World world = Minecraft.getMinecraft().world;
			if(world.isBlockLoaded(message.pos) && (world.getTileEntity(message.pos) instanceof TileEntityShowcase)){
				TileEntityShowcase te = (TileEntityShowcase) world.getTileEntity(message.pos);
				te.ownerId = message.uid;
			}
		}
	}
}
