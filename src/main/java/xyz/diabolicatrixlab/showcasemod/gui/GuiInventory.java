package xyz.diabolicatrixlab.showcasemod.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.container.ContainerShowcase;
import xyz.diabolicatrixlab.showcasemod.container.ContainerShowcaseInventory;
import xyz.diabolicatrixlab.showcasemod.packet.PacketOpenGui;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class GuiInventory extends GuiContainer {

	public static final int HEIGHT = 183;
	public static final int WIDTH = 176;
	
	public static final ResourceLocation GUI = new ResourceLocation(ShowcaseMod.MODID, "textures/gui/container/showcase_inv.png");
	
	public TileEntityShowcase te;
	private IInventory player = Minecraft.getMinecraft().player.inventory;
	
	public GuiInventory(TileEntityShowcase te, ContainerShowcaseInventory container) {
		super(container);
		this.te = te;
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		this.addButton(new GuiInventoryButton(0, guiLeft + xSize - 1, guiTop + 20 + 4));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.mc.getTextureManager().bindTexture(GUI);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	
		if(!te.adminShop && te.canEdit(Minecraft.getMinecraft().player)){
			this.drawTexturedModalRect(guiLeft + xSize - 3, guiTop + 20, 18, 183, 25, 26);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, 4, 4210752);
		this.fontRenderer.drawString(I18n.format("container.showcaseinventory.coins"), 97, 4, 4210752);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id) {
		case 0:
			NetRegistry.network.sendToServer(new PacketOpenGui(te.getPos(), 0));
			break;
		}
	}

}
