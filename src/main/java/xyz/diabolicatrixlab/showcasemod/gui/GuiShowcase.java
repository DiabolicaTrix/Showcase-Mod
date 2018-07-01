package xyz.diabolicatrixlab.showcasemod.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.server.permission.PermissionAPI;
import xyz.diabolicatrixlab.showcasemod.NetRegistry;
import xyz.diabolicatrixlab.showcasemod.ShowcaseMod;
import xyz.diabolicatrixlab.showcasemod.container.ContainerShowcase;
import xyz.diabolicatrixlab.showcasemod.item.ItemShowcaseModifier;
import xyz.diabolicatrixlab.showcasemod.packet.PacketBuyItem;
import xyz.diabolicatrixlab.showcasemod.packet.PacketOpenGui;
import xyz.diabolicatrixlab.showcasemod.tileentity.TileEntityShowcase;

public class GuiShowcase extends GuiContainer {
	
	public static final int HEIGHT = 183;
	public static final int WIDTH = 176;
	
	public static final ResourceLocation GUI = new ResourceLocation(ShowcaseMod.MODID, "textures/gui/container/showcase.png");
	
	public TileEntityShowcase te;
	private IInventory player = Minecraft.getMinecraft().player.inventory;
	private GuiTextField amountField;
	
	public GuiShowcase(TileEntityShowcase te, ContainerShowcase container) {
		super(container);
		
		this.xSize = WIDTH;
		this.ySize = HEIGHT;
		this.te = te;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.initButtons();
		
		int fieldX = guiLeft + 8 + this.fontRenderer.getStringWidth(I18n.format("container.showcase.amount") + ": ");
		this.amountField = new GuiTextField(4, this.fontRenderer, fieldX, this.ySize - 96, 50, 10);
		amountField.setMaxStringLength(3);
		this.amountField.setText("1");
		te.totalPrice = 0;
		this.updatePrice();
	}
	
	public void initButtons(){
		this.buttonList.clear();
		
		if(canEdit()){
			this.buttonList.add(new GuiArrowButton(0, guiLeft + 25, guiTop + 14));
			this.buttonList.add(new GuiArrowButton(1, guiLeft + 25, guiTop + 40, true));
			this.buttonList.add(new GuiArrowButton(2, guiLeft + 45, guiTop + 14));
			this.buttonList.add(new GuiArrowButton(3, guiLeft + 45, guiTop + 40, true));	
			if(!te.adminShop){
				this.buttonList.add(new GuiInventoryButton(4, guiLeft + xSize - 1, guiTop + 20 + 4));
			}
			this.checkButtons();
		}
		
		this.addButton(new GuiSmallButton(5, guiLeft + 112, guiTop + this.ySize - 126, I18n.format("container.showcase.buy")));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		this.mc.getTextureManager().bindTexture(GUI);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		this.drawTexturedModalRect(guiLeft + 46, guiTop + 26, 87, 213 + te.coinId * 10, 12, 10);
		this.drawTexturedModalRect(guiLeft + 8 + this.fontRenderer.getStringWidth(I18n.format("container.showcase.totalprice") + ": ") + 3 +this.fontRenderer.getStringWidth(this.te.totalPrice + ""), guiTop + this.ySize - 94 - 15, 87, 213 + te.coinId * 10, 12, 10);
	
		if(!te.adminShop && canEdit()){
			this.drawTexturedModalRect(guiLeft + xSize - 3, guiTop + 20, 117, 183, 25, 26);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		this.amountField.drawTextBox();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		//Titles
		this.fontRenderer.drawString(I18n.format("container.showcase"), this.getXSize() / 2 - this.fontRenderer.getStringWidth(I18n.format("container.showcase"))/2, 5, 4210752);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
        
        //Gui
        this.fontRenderer.drawString(I18n.format("container.showcase.amount") + ": ", 8, this.ySize - 94 - 29, 4210752);
        this.fontRenderer.drawString(I18n.format("container.showcase.totalprice") + ": ", 8, this.ySize - 94 - 14, 4210752);
        this.fontRenderer.drawString(this.te.totalPrice + "", 8 + this.fontRenderer.getStringWidth(I18n.format("container.showcase.totalprice") + ": "), this.ySize - 94 - 14, 4210752);
        this.fontRenderer.drawString("" + this.te.price, 33 - (this.fontRenderer.getStringWidth("" + this.te.price) / 2), 28, 4210752);
	}
	
	public void checkButtons(){
		GuiButton arrowUp = this.buttonList.get(2);
		GuiButton arrowDown = this.buttonList.get(3);
		if(te.coinId >= 2)
		{
			arrowUp.enabled = false;
		}
		if(te.coinId <= 0)
		{
			arrowDown.enabled = false;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id){
		case 0:
			GuiButton btn1 = buttonList.get(1);
			if(!btn1.enabled)
				btn1.enabled = true;
			te.update(te.coinId, isShiftKeyDown() ? te.price + 10 : te.price + 1);
			this.updatePrice();
			break;
		case 1:
			te.update(te.coinId, isShiftKeyDown() ? te.price <= 10 ? te.price = 0 : te.price - 10 : te.price - 1);
			this.updatePrice();
			if(te.price <= 0){
				button.enabled = false;
			}
			break;
		case 2:
			GuiButton btn3 = buttonList.get(3);
			if(!btn3.enabled)
				btn3.enabled = true;	
			te.update(te.coinId+1, te.price);
			if(te.coinId >= 2){
				button.enabled = false;
			}
			break;
		case 3:
			GuiButton btn2 = buttonList.get(2);
			if(!btn2.enabled)
				btn2.enabled = true;	
			te.update(te.coinId-1, te.price);
			if(te.coinId <= 0){
				button.enabled = false;
			}
			break;
		case 4:
			NetRegistry.network.sendToServer(new PacketOpenGui(te.getPos(),1));
			break;
		case 5:
			NetRegistry.network.sendToServer(new PacketBuyItem(te.getPos(),Integer.parseInt(this.amountField.getText())));
			break;
		}
	}
	
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(Character.isDigit(typedChar) || keyCode == Keyboard.KEY_BACK){
			this.amountField.textboxKeyTyped(typedChar, keyCode);
			this.updatePrice();
		}
		if(!(this.amountField.isFocused() && (keyCode == Keyboard.KEY_E)))
		{
			super.keyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.amountField.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.amountField.updateCursorCounter();
	}
	
	public void updatePrice(){
		if(this.amountField != null && !this.amountField.getText().isEmpty())
		{
			int amount = Integer.parseInt(this.amountField.getText());
			te.totalPrice = amount * te.price;
		} else {
			te.totalPrice = 0;
		}
	}
	
	public boolean canEdit(){
		EntityPlayer player = Minecraft.getMinecraft().player;
		return te.canEdit(player);
	}

}
