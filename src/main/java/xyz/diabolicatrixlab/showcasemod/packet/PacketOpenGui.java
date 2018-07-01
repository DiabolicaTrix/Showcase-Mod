package xyz.diabolicatrixlab.showcasemod.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xyz.diabolicatrixlab.showcasemod.proxy.GuiProxy;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class PacketOpenGui implements IMessage {

	private BlockPos pos;
	private int id = 0;
	
	public PacketOpenGui() {}
	
	public PacketOpenGui(BlockPos pos, int id) {
		this.pos = pos;
		this.id = id;
	}
	
	public PacketOpenGui(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.id);
	}
	
	public static class Handler implements IMessageHandler<PacketOpenGui, IMessage>{

		@Override
		public IMessage onMessage(PacketOpenGui message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		public void handle(PacketOpenGui message, MessageContext ctx){
			World world = ctx.getServerHandler().player.world;
			if(world.isBlockLoaded(message.pos))
			{
				if(world.getTileEntity(message.pos) instanceof TileEntityShowcase){
					TileEntityShowcase te = (TileEntityShowcase) world.getTileEntity(message.pos);
					if(te.canEdit(ctx.getServerHandler().player))
					{
						GuiProxy.openGui(ctx.getServerHandler().player, message.id, message.pos);
					}
				} else {
					GuiProxy.openGui(ctx.getServerHandler().player, message.id, message.pos);
				}
			}
		}
		
	}

}
