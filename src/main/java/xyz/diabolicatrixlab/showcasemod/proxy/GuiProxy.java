package xyz.diabolicatrixlab.showcasemod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.container.ContainerShowcase;
import xyz.diabolicatrixlab.showcasemod.container.ContainerShowcaseInventory;
import xyz.diabolicatrixlab.showcasemod.gui.GuiInventory;
import xyz.diabolicatrixlab.showcasemod.gui.GuiShowcase;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class GuiProxy implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityShowcase && ID == 0) {
        	return new ContainerShowcase(player, (TileEntityShowcase) te);
        } else if (te instanceof TileEntityShowcase && ID == 1){
        	return new ContainerShowcaseInventory(player.inventory, (TileEntityShowcase) te);
        }
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityShowcase && ID == 0) {
        	TileEntityShowcase containerTileEntity = (TileEntityShowcase) te;
        	return new GuiShowcase(containerTileEntity, new ContainerShowcase(player, containerTileEntity));
        } else if (te instanceof TileEntityShowcase && ID == 1){
        	TileEntityShowcase containerTileEntity = (TileEntityShowcase) te;
        	return new GuiInventory(containerTileEntity, new ContainerShowcaseInventory(player.inventory, containerTileEntity));
        }
		return null;
	}
	
	public static void openGui(EntityPlayer player, int id, BlockPos pos){
		player.openGui(ShowcaseMod.instance, id, player.world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static void openGui(EntityPlayer player, BlockPos pos){
		openGui(player, 1, pos);
	}

}
