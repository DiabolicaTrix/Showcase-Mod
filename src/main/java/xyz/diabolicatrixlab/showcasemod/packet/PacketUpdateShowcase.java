package xyz.diabolicatrixlab.showcasemod.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.gui.GuiShowcase;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class PacketUpdateShowcase implements IMessage {

	private int coinId;
	private int price;
	private BlockPos pos;
	
	public PacketUpdateShowcase(BlockPos pos, int coinId, int price) {
		this.coinId = coinId;
		this.price = price;
		this.pos = pos;
	}
	
	public PacketUpdateShowcase() {}
	
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
	
	public static class Handler implements IMessageHandler<PacketUpdateShowcase, IMessage>{

		@Override
		public IMessage onMessage(PacketUpdateShowcase message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		private void handle(PacketUpdateShowcase message, MessageContext ctx){
			World world = Minecraft.getMinecraft().world;
			if(world.isBlockLoaded(message.pos) && world.getTileEntity(message.pos) instanceof TileEntityShowcase){
				TileEntityShowcase te = (TileEntityShowcase) world.getTileEntity(message.pos);
				te.coinId = message.coinId;
				te.price = message.price;
				if(Minecraft.getMinecraft().currentScreen instanceof GuiShowcase){
					GuiShowcase gui = (GuiShowcase) Minecraft.getMinecraft().currentScreen;
					gui.updatePrice();
				}
			}
		}
		
	}

}
