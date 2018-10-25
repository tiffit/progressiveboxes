package net.tiffit.progressiveboxes.client.gui.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.tiffit.progressiveboxes.client.gui.editor.GuiBoxEditor.GuiButtonClean;
import net.tiffit.progressiveboxes.data.ReqData;
import net.tiffit.tiffitlib.client.GuiChildScreen;

public class GuiReqEditor extends GuiChildScreen {

	private List<ReqData> data;
	private List<Pair<GuiTextField, GuiTextField>> tfMap = new ArrayList<>();

	public GuiReqEditor(GuiScreen parent, List<ReqData> data) {
		super(parent);
		this.data = data;
	}

	@Override
	public void initGui() {
		tfMap.clear();
		buttonList.clear();
		for (int i = 0; i < data.size(); i++) {
			ReqData rd = data.get(i);
			GuiTextField key = new GuiTextField(i, fontRenderer, 10, 40 + i * 23, (width - 23) / 2, 20);
			key.setGuiResponder(new GuiResponderString() {
				@Override
				public void setEntryValue(int id, String value) {
					rd.type = value;
				}
			});
			key.setMaxStringLength(50);
			key.setText(rd.type);
			GuiTextField value = new GuiTextField(i, fontRenderer, width / 2 + 3, 40 + i * 23, (width - 23) / 2, 20);
			value.setGuiResponder(new GuiResponderString() {
				@Override
				public void setEntryValue(int id, String value) {
					rd.value = value;
				}
			});
			value.setMaxStringLength(100);
			value.setText(rd.value);
			tfMap.add(Pair.of(key, value));
		}
		buttonList.add(new GuiButtonClean(0, 9, 40 + data.size() * 23, width - 17, 20, "Add Requirement"));
	}

	public ReqData[] getModifiedData() {
		return data.toArray(new ReqData[0]);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 0) {
			data.add(new ReqData());
			initGui();
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, "Requirements Editor", this.width / 2, 5, 0xffffffff);
		drawHorizontalLine(0, width, 15, 0xffffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(fontRenderer, "Type", width / 4, 27, 0xfff4d142);
		drawCenteredString(fontRenderer, "Value", width / 4 * 3, 27, 0xfff4d142);
		for (Pair<GuiTextField, GuiTextField> tf : tfMap) {
			tf.getKey().drawTextBox();
			tf.getValue().drawTextBox();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (Pair<GuiTextField, GuiTextField> tf : tfMap) {
			tf.getKey().mouseClicked(mouseX, mouseY, mouseButton);
			tf.getValue().mouseClicked(mouseX, mouseY, mouseButton);
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_DELETE) {
			for (Pair<GuiTextField, GuiTextField> tf : tfMap) {
				if ((tf.getKey().isFocused() || tf.getValue().isFocused()) && tf.getKey().getId() >= 0) {
					data.remove(tf.getKey().getId());
					initGui();
					break;
				}
			}
		} else {
			for (Pair<GuiTextField, GuiTextField> tf : tfMap) {
				tf.getKey().textboxKeyTyped(typedChar, keyCode);
				tf.getValue().textboxKeyTyped(typedChar, keyCode);
			}
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

}
