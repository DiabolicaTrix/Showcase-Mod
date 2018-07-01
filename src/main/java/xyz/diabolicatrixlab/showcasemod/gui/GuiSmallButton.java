package xyz.diabolicatrixlab.showcasemod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiSmallButton extends GuiButton {

	public GuiSmallButton(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, 49, 15, buttonText);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible)
        {
			FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(GuiShowcase.GUI);
            
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            this.drawTexturedModalRect(this.x, this.y, 0, 193 + i * 15, this.width, this.height);           
            
            this.mouseDragged(mc, mouseX, mouseY);
            
            int j = 14737632;
            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
	}
	
	@Override
	protected int getHoverState(boolean e) {
		
		return !this.enabled ? 2 : (e ? 1 : 0);
	}

}
