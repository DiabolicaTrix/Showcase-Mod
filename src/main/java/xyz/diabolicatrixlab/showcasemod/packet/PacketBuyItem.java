package xyz.diabolicatrixlab.showcasemod.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import scala.tools.nsc.doc.base.comment.Chain;
import xyz.diabolicatrixlab.showcasemod.proxy.GuiProxy;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;
import xyz.diabolicatrixlab.showcasemod.util.CoinUtils;

public class PacketBuyItem implements IMessage {

	private BlockPos pos;
	private int quantity;
	
	public PacketBuyItem() {}
	
	public PacketBuyItem(BlockPos pos, int quantity) {
		this.pos = pos;
		this.quantity = quantity;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.quantity = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeInt(this.quantity);
	}
	
	public static class Handler implements IMessageHandler<PacketBuyItem, IMessage>{

		@Override
		public IMessage onMessage(PacketBuyItem message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		public void handle(PacketBuyItem message, MessageContext ctx){
			World world = ctx.getServerHandler().player.world;
			if(world.isBlockLoaded(message.pos) && world.getTileEntity(message.pos) instanceof TileEntityShowcase)
			{
				TileEntityShowcase shop = (TileEntityShowcase) world.getTileEntity(message.pos);
				ctx.getServerHandler().player.sendMessage(CoinUtils.buy(shop, ctx.getServerHandler().player, shop.getTotal(message.quantity), message.quantity));
			}
		}
		
	}

}
