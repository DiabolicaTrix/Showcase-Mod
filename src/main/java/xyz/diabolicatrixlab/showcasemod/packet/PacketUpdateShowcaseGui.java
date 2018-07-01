package xyz.diabolicatrixlab.showcasemod.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class PacketUpdateShowcaseGui implements IMessage {

	private int coinId;
	private int price;
	private BlockPos pos;
	
	public PacketUpdateShowcaseGui(TileEntityShowcase te, int coinId, int price) {
		this.coinId = coinId;
		this.price = price;
		this.pos = te.getPos();
	}
	
	public PacketUpdateShowcaseGui() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.coinId = buf.readInt();
		this.price = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.coinId);
		buf.writeInt(this.price);
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateShowcaseGui, IMessage>{

		@Override
		public IMessage onMessage(PacketUpdateShowcaseGui message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		private void handle(PacketUpdateShowcaseGui message, MessageContext ctx){
			World world = ctx.getServerHandler().player.getEntityWorld();
			if(world.isBlockLoaded(message.pos) && world.getTileEntity(message.pos) instanceof TileEntityShowcase){
				TileEntityShowcase te = (TileEntityShowcase) world.getTileEntity(message.pos);
				if(te.canEdit(ctx.getServerHandler().player))
				{
					te.coinId = message.coinId;
					te.price = message.price;
					NetRegistry.network.sendToAll(new PacketUpdateShowcase(message.pos, message.coinId, message.price));
				}else {
					NetRegistry.network.sendToAll(new PacketUpdateShowcase(message.pos, te.coinId, te.price));
				}
			}
		}
		
	}

}
