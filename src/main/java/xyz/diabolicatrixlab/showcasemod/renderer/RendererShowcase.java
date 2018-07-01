package xyz.diabolicatrixlab.showcasemod.renderer;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class RendererShowcase extends TileEntitySpecialRenderer<TileEntityShowcase> {
	
	private final Random random = new Random();
	
	@Override
	public void render(TileEntityShowcase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		x += 0.5D;
		y += 0.5D;
		z += 0.5D;
		
		EntityItem item = te.item;
		if (item == null) {
			return;
		}
		
		te.customAge += 0.5F;
		
		float bobY = MathHelper.sin((te.customAge + partialTicks) / 10.0f + item.hoverStart) * 0.1F + 0.1F;
        float rotationAngle = ((te.customAge + partialTicks) / 20.0f + item.hoverStart) * 57.295776F;
		
        GlStateManager.pushMatrix();
        
		GlStateManager.translate(x,y + bobY,z);
		GlStateManager.rotate(rotationAngle, 0.0F, 1.0F, 0.0F);
		
		if(item.getItem().getItem() instanceof ItemBed){
			GlStateManager.scale(.3f, .3f, .3f);
		}
		else {
			GlStateManager.scale(.5f, .5f, .5f);
		}
		
		Minecraft.getMinecraft().getRenderItem().renderItem(item.getItem(), ItemCameraTransforms.TransformType.NONE);
		
        GlStateManager.popMatrix();

	}
}
