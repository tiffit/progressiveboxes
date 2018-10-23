package net.tiffit.progressiveboxes.client.gui.editor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.ProgressiveBoxes;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.item.BoxItem;
import net.tiffit.tiffitlib.client.GuiChildScreen;

public class GuiBoxEditorMenu extends GuiChildScreen {

	private List<BoxData> boxes = new ArrayList<>();
	private int scroll = 0;

	public GuiBoxEditorMenu(GuiScreen parent) {
		super(parent);
		for (BoxData data : BoxRegistry.LOADED_BOXES) {
			if (data.file != null) {
				boxes.add(data);
			}
		}
	}

	@Override
	public void initGui() {
		buttonList.clear();
		int y = 0;
		int x = 0;
		for (int i = 0; i < boxes.size(); i++) {
			buttonList.add(new ButtonBox(i, x, y - scroll, boxes.get(i)));
			x++;
			if (this.width < x * 110 + 110) {
				x = 0;
				y++;
			}
		}
		buttonList.add(new ButtonBox(boxes.size(), x, y - scroll, null));
	}

	@Override
	public void handleMouseInput() {
		try {
			super.handleMouseInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = Integer.signum(Mouse.getEventDWheel());
		if (i != 0) {
			scroll -= i;
			if (scroll < 0)
				scroll = 0;
			initGui();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK) {
			for (GuiButton button : buttonList) {
				if (button.isMouseOver()) {
					if (button instanceof ButtonBox) {
						BoxData data = ((ButtonBox) button).data;
						this.boxes.remove(data);
						data.delete();
						initGui();
						break;
					}
				}
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof ButtonBox) {
			ButtonBox bb = (ButtonBox) button;
			if (bb.data != null) {
				mc.displayGuiScreen(new GuiBoxEditor(this, bb.data));
			} else {
				BoxData data = new BoxData();
				data.file = getUsableFile();
				data.save();
				boxes.add(data);
				initGui();
				BoxRegistry.LOADED_BOXES.add(data);
				mc.displayGuiScreen(new GuiBoxEditor(this, data));
			}
		}
	}

	private File getUsableFile() {
		String str = "pregen_" + RandomStringUtils.randomAlphanumeric(10);
		File f = new File(ProgressiveBoxes.configFolder + "/boxes", str + ".json");
		if (f.exists())
			return getUsableFile();
		return f;
	}

	public static class ButtonBox extends GuiButton {

		private BoxData data;
		private ItemStack item;

		public ButtonBox(int id, int x, int y, BoxData data) {
			super(id, x * 110 + 10, y * 110 + 10, 100, 100, data == null ? "" : data.name + " Box");
			this.data = data;
			if (data != null)
				item = BoxItem.getStack(data);
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			Gui.drawRect(x, y, x + width, y + height, 0x55ffffff);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int color = 0x55000000;
			if (hovered)
				color = 0x44000000;
			GuiScreen.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, color);
			float scale = 100 / 16f;
			FontRenderer fr = mc.fontRenderer;
			if (data == null) {
				drawCenteredString(fr, "Create New Box", x + width / 2, y - 5 + height / 2, 0xffffffff);
			} else {
				GlStateManager.pushMatrix();
				GlStateManager.translate(x, y - 4, 0);
				GlStateManager.scale(scale, scale, 0);
				mc.getRenderItem().renderItemAndEffectIntoGUI(item, 0, 0);
				GlStateManager.popMatrix();
				drawCenteredString(fr, displayString, x + width / 2, y + 5, 0xffffffff);
				drawCenteredString(fr, "id: " + data.id, x + width / 2, y + height - 10, 0xff999999);
			}
		}
	}

}
