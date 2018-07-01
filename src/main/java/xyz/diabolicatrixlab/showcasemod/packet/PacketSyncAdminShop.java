package xyz.diabolicatrixlab.showcasemod.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;
import xyz.diabolicatrixlab.showcasemod.util.PlayerUtil;

public class PacketSyncAdminShop implements IMessage {

	public BlockPos pos;
	public boolean adminShop;
	public UUID uid;
	
	public PacketSyncAdminShop(BlockPos pos, UUID playerId, boolean adminShop) {
		this.pos = pos;
		this.adminShop = adminShop;
		this.uid = playerId;
	}
	
	public PacketSyncAdminShop() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		this.adminShop = buf.readBoolean();
		this.uid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(this.pos.toLong());
		buf.writeBoolean(this.adminShop);
		ByteBufUtils.writeUTF8String(buf, this.uid.toString());
	}
	
	public static class Handler implements IMessageHandler<PacketSyncAdminShop, IMessage> {

		@Override
		public IMessage onMessage(PacketSyncAdminShop message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message,ctx));
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		public void handle(PacketSyncAdminShop message, MessageContext ctx){
			World world = Minecraft.getMinecraft().world;
			if(world.isBlockLoaded(message.pos) && (world.getTileEntity(message.pos) instanceof TileEntityShowcase)){
				TileEntityShowcase te = (TileEntityShowcase) world.getTileEntity(message.pos);
				te.adminShop = message.adminShop;
				if(PlayerUtil.getUniqueID(Minecraft.getMinecraft().player).equals(message.uid)){
					Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("showcasemod.message.modifier.admin", new Object[] {message.adminShop}).setStyle(new Style().setColor(TextFormatting.GREEN)));
				}
			}
		}
		
	}

}
