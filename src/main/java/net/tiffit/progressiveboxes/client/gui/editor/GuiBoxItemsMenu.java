package net.tiffit.progressiveboxes.client.gui.editor;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.data.LootData;
import net.tiffit.tiffitlib.client.GuiChildScreen;

public class GuiBoxItemsMenu extends GuiChildScreen {

	private BoxData data;
	private int scroll = 0;

	public GuiBoxItemsMenu(GuiScreen parent, BoxData data) {
		super(parent);
		this.data = data;
	}

	@Override
	public void initGui() {
		buttonList.clear();
		int y = 0;
		int x = 0;
		for (int i = 0; i < data.loot.length; i++) {
			buttonList.add(new LootButton(i, x, y - scroll, data.loot[i]));
			x++;
			if (this.width < x * 110 + 110) {
				x = 0;
				y++;
			}
		}
		buttonList.add(new LootButton(data.loot.length, x, y - scroll, null));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof LootButton) {
			LootButton lb = (LootButton) button;
			if(lb.data != null){
				mc.displayGuiScreen(new GuiLootEditor(this, lb.data));
			}else{
				LootData ld = new LootData();
				List<LootData> list = Lists.newArrayList(data.loot);
				list.add(ld);
				data.loot = list.toArray(new LootData[0]);
				initGui();
				mc.displayGuiScreen(new GuiLootEditor(this, ld));
			}
		}
	}	
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK) {
			for (GuiButton button : buttonList) {
				if (button.isMouseOver()) {
					if (button instanceof LootButton) {
						LootData data = ((LootButton) button).data;
						List<LootData> list = Lists.newArrayList(this.data.loot);
						list.remove(data);
						this.data.loot = list.toArray(new LootData[0]);
						initGui();
					}
				}
			}
		}
	}
	
	@Override
	public void handleMouseInput(){
		try {
			super.handleMouseInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = Integer.signum(Mouse.getEventDWheel());
		if (i != 0){
			scroll -= i;
			if(scroll < 0)scroll = 0;
			initGui();
		}
	}

	public static class LootButton extends GuiButton {

		private LootData data;
		private ItemStack item;

		public LootButton(int id, int x, int y, LootData data) {
			super(id, x * 110 + 10, y * 110 + 10, 100, 100, "");
			this.data = data;
			if (data != null)
				item = data.item.getStack();
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			Gui.drawRect(x, y, x + width, y + height, 0x55ffffff);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int color = 0x55000000;
			if (hovered)
				color = 0x44000000;
			GuiScreen.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, color);
			float scale = 50 / 16f;
			FontRenderer fr = mc.fontRenderer;
			if (data == null) {
				drawCenteredString(fr, "Add New Item", x + width / 2, y - 5 + height/2, 0xffffffff);
			} else {
				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 25, y + 25, 0);
				GlStateManager.scale(scale, scale, 0);
				RenderHelper.enableGUIStandardItemLighting();
	            GlStateManager.color(1, 1, 1, 1);
				mc.getRenderItem().renderItemAndEffectIntoGUI(item, 0, 0);
				String text = "";
	            if(item.getCount() > 1)text = item.getCount() + "";
	            mc.getRenderItem().renderItemOverlayIntoGUI(fr, item, 0, 0, text);
	            RenderHelper.disableStandardItemLighting();
				GlStateManager.popMatrix();
			}
		}
	}

}
