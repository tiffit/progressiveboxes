package net.tiffit.progressiveboxes.client.gui.editor;

import java.io.IOException;
import java.util.HashMap;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.data.DropData;
import net.tiffit.progressiveboxes.item.BoxItem;
import net.tiffit.tiffitlib.client.GuiChildScreen;

public class GuiBoxEditor extends GuiChildScreen {

	private BoxData data;

	private HashMap<String, GuiTextField> textfields = new HashMap<>();
	
	private GuiButtonClean buttonPlayerDrops;
	
	public GuiBoxEditor(GuiScreen parent, BoxData data) {
		super(parent);
		this.data = data;
	}

	@Override
	public void initGui() {
		textfields.clear();
		buttonList.clear();
		GuiTextField tfname = new GuiTextField(0, fontRenderer, 10, 40, 200, 20);
		setGuiTextField(tfname, data.name, "name", (id, str) -> {
			data.name = str;
		});
		GuiTextField tfid = new GuiTextField(0, fontRenderer, 213, 40, 200, 20);
		setGuiTextField(tfid, data.id, "id", (id, str) -> {
			data.id = str;
		});
		GuiTextField tfcolor = new GuiTextField(0, fontRenderer, 10, 80, 200, 20);
		tfcolor.setValidator((str) -> {
			if (str.isEmpty())
				return true;
			try {
				Integer.parseInt(str, 16);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setGuiTextField(tfcolor, data.color, "color", (id, str) -> {
			data.color = str;
		});
		GuiTextField ftopenmsg = new GuiTextField(0, fontRenderer, 10, 40 * 3, 200, 20);
		setGuiTextField(ftopenmsg, data.openmessage, "openmsg", (id, str) -> {
			data.openmessage = str;
		});
		GuiTextField ftdescription = new GuiTextField(0, fontRenderer, 10, 40 * 4, 200, 20);
		setGuiTextField(ftdescription, data.description, "description", (id, str) -> {
			data.description = str;
		});
		GuiTextField ftamount = new GuiTextField(0, fontRenderer, 10, 40 * 5, 75, 20);
		ftamount.setValidator((str) -> {
			if (str.isEmpty())
				return true;
			try {
				Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setGuiTextField(ftamount, "" + data.amount, "amount", (id, str) -> {
			data.amount = Integer.valueOf(str.isEmpty() ? "1" : str);
		});
		GuiTextField ftrarity = new GuiTextField(0, fontRenderer, 10, 40 * 6, 75, 20);
		ftrarity.setValidator((str) -> {
			if (str.isEmpty() || str.equals("-"))
				return true;
			try {
				Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setGuiTextField(ftrarity, "" + data.rarity, "rarity", (id, str) -> {
			data.rarity = Integer.valueOf(str.isEmpty() | str.equals("-") ? "-1" : str);
		});

		buttonList.add(new GuiCheckBox(0, 87, 40 * 5 + 5, "Unique", data.unique));
		buttonList.add(new GuiButtonClean(1, width-205, 40, 200, 20, "Items..."));
		buttonList.add(new GuiButtonClean(2, width-205, 40 + 23*1, 200, 20, "Requirements..."));
		buttonList.add(new GuiButtonClean(3, width-205, 40 + 23*2, 200, 20, "Mob Drops..."));
		buttonList.add(buttonPlayerDrops = new GuiButtonClean(4, width-205 + 52, 40 + 23*3, 200 - 52, 20, "Mob Drops (Player)..."));
		buttonPlayerDrops.enabled = data.dropchance_player != null;
		buttonList.add(new GuiButtonClean(5, width-205, 40 + 23*3, 51, 20, buttonPlayerDrops.enabled ? "Delete" : "Create"));
	}

	private void setGuiTextField(GuiTextField field, String text, String type, GuiResponderString responder) {
		field.setText(text);
		field.setGuiResponder(responder);
		textfields.put(type, field);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			GuiCheckBox cb = (GuiCheckBox) button;
			data.unique = cb.isChecked();
		}else if(button.id == 1){
			mc.displayGuiScreen(new GuiBoxItemsMenu(this, data));
		}else if(button.id == 2){
			mc.displayGuiScreen(new GuiReqEditor(this, Lists.newArrayList(data.requirements)));
		}else if(button.id == 3){
			mc.displayGuiScreen(new GuiDropsEditor(this, data.dropchance));
		}else if(button.id == 4){
			mc.displayGuiScreen(new GuiDropsEditor(this, data.dropchance_player));
		}else if(button.id == 5){
			if(data.dropchance_player == null){
				data.dropchance_player = new DropData();
			}else{
				data.dropchance_player = null;
			}
			initGui();
		}
	}
	
	@Override
	public void returnTo(GuiChildScreen child) {
		if(child instanceof GuiReqEditor){
			GuiReqEditor gui = (GuiReqEditor) child;
			data.requirements = gui.getModifiedData();
		}
	}

	@Override
	public void onGuiClosed() {
		data.save();
		super.onGuiClosed();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, data.name + " Box Editor", this.width / 2, 5, 0xffffffff);
		drawHorizontalLine(0, width, 15, 0xffffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
		fontRenderer.drawString("Name", 10, 30, 0xffffffff);
		fontRenderer.drawString("ID", 213, 30, 0xffffffff);
		fontRenderer.drawString("Color " + TextFormatting.GRAY + "(Hex)", 10, 70, 0xffffffff);
		fontRenderer.drawString("Requirements Message", 10, 40 * 3 - 10, 0xffffffff);
		fontRenderer.drawString("Description", 10, 40 * 4 - 10, 0xffffffff);
		fontRenderer.drawString("Rolls", 10, 40 * 5 - 10, 0xffffffff);
		fontRenderer.drawString("Tier (rarity)", 10, 40 * 6 - 10, 0xffffffff);
		mc.getRenderItem().renderItemIntoGUI(BoxItem.getStack(data), 210, 81);
		for (GuiTextField tf : textfields.values()) {
			tf.drawTextBox();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (GuiTextField tf : textfields.values()) {
			tf.mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		for (GuiTextField tf : textfields.values()) {
			tf.textboxKeyTyped(typedChar, keyCode);
		}
		super.keyTyped(typedChar, keyCode);
	}

	public static interface GuiResponderString extends GuiResponder {

		@Override
		public default void setEntryValue(int id, boolean value) {
		}

		@Override
		public default void setEntryValue(int id, float value) {
		}

	}

	public static class GuiButtonClean extends GuiButton {

		public GuiButtonClean(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
			super(buttonId, x, y, widthIn, heightIn, buttonText);
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				FontRenderer fontrenderer = mc.fontRenderer;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
				Gui.drawRect(x, y, x + width, y + height, 0x55ffffff);
				GuiScreen.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0x55000000);
				this.mouseDragged(mc, mouseX, mouseY);
				int j = 14737632;
				if (packedFGColour != 0) {
					j = packedFGColour;
				} else if (!this.enabled) {
					j = 10526880;
				} else if (this.hovered) {
					j = 16777120;
				}
				this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
			}
		}
	}

}
