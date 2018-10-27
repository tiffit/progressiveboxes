package net.tiffit.progressiveboxes.client.gui.editor;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.tiffit.progressiveboxes.client.gui.editor.GuiBoxEditor.GuiButtonClean;
import net.tiffit.tiffitlib.client.GuiChildScreen;

public class GuiInventoryImport extends GuiChildScreen {

	public ItemStack stack = ItemStack.EMPTY;
	public boolean selected;

	public GuiInventoryImport(GuiScreen parent) {
		super(parent);
		
	}

	@Override
	public void initGui() {
		buttonList.clear();
		InventoryPlayer ip = mc.player.inventory;
		NonNullList<ItemStack> inv = ip.mainInventory;
		int x = 0;
		int y = 0;
		for(int i = 0 ; i < inv.size(); i++){
			buttonList.add(new GuiButtonSlot(i, x, y, inv.get(i)));
			x++;
			if(x >= 9){
				x = 0;
				y++;
			}
		}
		buttonList.add(new GuiButtonClean(0, GuiInventoryImport.this.width/2 - 9*17/2, GuiInventoryImport.this.height/2 + 2*17 + 5, 17*9, 15, "Import"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button instanceof GuiButtonSlot){
			GuiButtonSlot slot = (GuiButtonSlot) button;
			stack = slot.stack;
		}else if(button.id == 0){
			selected = true;
			returnToParent();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		if(!stack.isEmpty()){
			drawCenteredString(fontRenderer, stack.getDisplayName() + " x" + stack.getCount(), width/2, height/2 - 17*2 - 10*3, 0xffffffff);
			drawCenteredString(fontRenderer, "Meta: " + stack.getMetadata(), width/2, height/2 - 17*2 - 10*2, 0xffcccccc);
			drawCenteredString(fontRenderer, stack.hasTagCompound() ? stack.getTagCompound().toString() : "No NBT", width/2, height/2 - 17*2 - 10*1, 0xff999999);
		}else{
			drawCenteredString(fontRenderer, "No Item Selected", width/2, height/2 - 17*2 - 10*2, 0xffffffff);
		}
		for(GuiButton button : buttonList){
			if(button instanceof GuiButtonSlot){
				GuiButtonSlot slot = (GuiButtonSlot) button;
		        if(slot.isMouseOver()){
		        	if(!slot.stack.isEmpty())renderToolTip(slot.stack, mouseX, mouseY);
		        	break;
		        }
			}
		}
	}
	
	private class GuiButtonSlot extends GuiButton{

		private final ItemStack stack;
		
		public GuiButtonSlot(int id, int x, int y, ItemStack stack) {
			super(id, GuiInventoryImport.this.width/2 - 9*17/2 + x*17, GuiInventoryImport.this.height/2 - 4*17/2 + y*17, 16, 16, "");
			this.stack = stack;
		}
		
		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			Gui.drawRect(x, y, x + width, y + height, 0x55ffffff);
			GuiScreen.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0x55000000);
			RenderHelper.enableStandardItemLighting();
			mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
			mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, stack.getCount() != 1 ? stack.getCount() + "" : "");
            RenderHelper.disableStandardItemLighting();
		}
		
	}

}
