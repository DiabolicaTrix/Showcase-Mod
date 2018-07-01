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
import xyz.diabolicatrixlab.showcasemod.ShowcaseConfig;
import xyz.diabolicatrixlab.showcasemod.proxy.GuiProxy;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;
import xyz.diabolicatrixlab.showcasemod.util.CoinUtils;

public class PacketSyncConfig implements IMessage {

	int copperValue;
	int silverValue;
	int goldValue;
	boolean isOffline;
	
	public PacketSyncConfig() {}
	
	public PacketSyncConfig(int copperValue, int silverValue, int goldValue, boolean isOffline) {
		this.copperValue = copperValue;
		this.silverValue = silverValue;
		this.goldValue = goldValue;
		this.isOffline = isOffline;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.copperValue = buf.readInt();
		this.silverValue = buf.readInt();
		this.goldValue = buf.readInt();
		this.isOffline = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.copperValue);
		buf.writeInt(this.silverValue);
		buf.writeInt(this.goldValue);
		buf.writeBoolean(this.isOffline);
	}
	
	public static class Handler implements IMessageHandler<PacketSyncConfig, IMessage>{

		@Override
		public IMessage onMessage(PacketSyncConfig message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}
		
		public void handle(PacketSyncConfig message, MessageContext ctx){
			ShowcaseConfig.copperValue = message.copperValue;
			ShowcaseConfig.silverValue = message.silverValue;
			ShowcaseConfig.goldValue = message.goldValue;
			ShowcaseConfig.isOffline = message.isOffline;
		}
		
	}

}
