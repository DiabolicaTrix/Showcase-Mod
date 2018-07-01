package xyz.diabolicatrixlab.showcasemod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class ItemShowcaseModifier extends ItemShowcaseMod {

	public ItemShowcaseModifier() {
		super("showcase_modifier");
	}
}
