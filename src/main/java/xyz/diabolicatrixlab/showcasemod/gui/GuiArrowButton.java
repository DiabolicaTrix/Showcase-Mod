package xyz.diabolicatrixlab.showcasemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class GuiArrowButton extends GuiButton {

	private boolean isDown = false;
	
	public GuiArrowButton(int buttonId, int x, int y, boolean down) {
		super(buttonId, x, y, 15, 10, "");
		this.isDown = down;
	}
	
	public GuiArrowButton(int buttonId, int x, int y) {
		this(buttonId, x, y, false);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		
		if (this.visible)
        {
            mc.getTextureManager().bindTexture(GuiShowcase.GUI);
            
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            int textureX = isDown ? 102 : 87;
            
            this.drawTexturedModalRect(this.x, this.y, textureX, 183 + i * 10, this.width, this.height);           
            
            this.mouseDragged(mc, mouseX, mouseY);
        }
	}
	
	@Override
	protected int getHoverState(boolean e) {
		
		return !this.enabled ? 2 : (e ? 1 : 0);
	}

}
