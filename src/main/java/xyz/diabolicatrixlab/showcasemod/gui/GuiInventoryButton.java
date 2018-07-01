package xyz.diabolicatrixlab.showcasemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiInventoryButton extends GuiButton {

	public GuiInventoryButton(int buttonId, int x, int y) {
		super(buttonId, x, y, 18, 18, "");
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible)
        {
            mc.getTextureManager().bindTexture(GuiShowcase.GUI);
            
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            
            this.drawTexturedModalRect(this.x, this.y, 69, 183 + i * 18, this.width, this.height);
            this.drawTexturedModalRect(this.x + 4, this.y + 4, 73, 241, 10, 9); 
                        
            this.mouseDragged(mc, mouseX, mouseY);
        }
	}
	
	@Override
	protected int getHoverState(boolean e) {
		
		return !this.enabled ? 2 : (e ? 1 : 0);
	}


}
