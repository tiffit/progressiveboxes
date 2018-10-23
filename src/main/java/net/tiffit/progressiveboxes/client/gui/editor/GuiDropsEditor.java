package net.tiffit.progressiveboxes.client.gui.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.tiffit.progressiveboxes.client.gui.editor.GuiBoxEditor.GuiButtonClean;
import net.tiffit.progressiveboxes.data.DropData;
import net.tiffit.progressiveboxes.data.MobGroupOverrideData;
import net.tiffit.tiffitlib.client.GuiChildScreen;

public class GuiDropsEditor extends GuiChildScreen {

	private DropData data;
	private List<MobGroupOverrideData> overrides;
	private List<Pair<GuiTextField, GuiTextField>> tfMap = new ArrayList<>();
	private GuiTextField tfgeneric;
	private GuiTextField tfgeneric_value;

	public GuiDropsEditor(GuiScreen parent, DropData data) {
		super(parent);
		this.data = data;
		overrides = Lists.newArrayList(data.overrides);
	}

	@Override
	public void initGui() {
		tfMap.clear();
		buttonList.clear();
		tfgeneric = new GuiTextField(-1, fontRenderer, 10, 40, (width-23)/2, 20);
		tfgeneric.setText("default");
		tfgeneric.setEnabled(false);
		
		tfgeneric_value = new GuiTextField(-1, fontRenderer, width/2 + 3, 40, (width-23)/2, 20);
		tfgeneric_value.setText(data.chance + "");
		tfgeneric_value.setGuiResponder(new GuiResponderString() {
			@Override
			public void setEntryValue(int id, String value) {
				data.chance = Double.valueOf(value.isEmpty() ? "0" : value);
			}
		});
		tfgeneric_value.setValidator((str) -> {
			if(str.isEmpty())return true;
			try{
				double val = Double.valueOf(str);
				if(val < 0 || val > 1)return false;
			}catch(NumberFormatException e){
				return false;
			}
			return true;
		});
		for(int i = 0; i < overrides.size(); i++){
			MobGroupOverrideData rd = overrides.get(i);
			GuiTextField key = new GuiTextField(i, fontRenderer, 10, 40 + (i+1)*23, (width-23)/2, 20);
			key.setGuiResponder(new GuiResponderString() {
				@Override
				public void setEntryValue(int id, String value) {
					rd.mobgroup = value;
				}
			});
			key.setText(rd.mobgroup);
			GuiTextField value = new GuiTextField(i, fontRenderer, width/2 + 3, 40 + (i+1)*23, (width-23)/2, 20);
			value.setGuiResponder(new GuiResponderString() {
				@Override
				public void setEntryValue(int id, String value) {
					rd.chance = Double.valueOf(value.isEmpty() ? "0" : value);
				}
			});
			value.setText(rd.chance + "");
			value.setValidator((str) -> {
				if(str.isEmpty())return true;
				try{
					double val = Double.valueOf(str);
					if(val < 0 || val > 1)return false;
				}catch(NumberFormatException e){
					return false;
				}
				return true;
			});
			tfMap.add(Pair.of(key, value));
		}
		buttonList.add(new GuiButtonClean(0, 9, 40 + (overrides.size()+1)*23, width - 17, 20, "Add Override"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id == 0){
			overrides.add(new MobGroupOverrideData());
			initGui();
		}
	}

	@Override
	public void onGuiClosed() {
		data.overrides = overrides.toArray(new MobGroupOverrideData[0]);
		super.onGuiClosed();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, "Drop Chance Editor", this.width / 2, 5, 0xffffffff);
		drawHorizontalLine(0, width, 15, 0xffffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(fontRenderer, "Mob Group", width/4, 27, 0xfff4d142);
		drawCenteredString(fontRenderer, "Chance", width/4 * 3, 27, 0xfff4d142);
		for (Pair<GuiTextField, GuiTextField> tf : tfMap) {
			tf.getKey().drawTextBox();
			tf.getValue().drawTextBox();
		}
		tfgeneric.drawTextBox();
		tfgeneric_value.drawTextBox();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (Pair<GuiTextField, GuiTextField> tf : tfMap) {
			tf.getKey().mouseClicked(mouseX, mouseY, mouseButton);
			tf.getValue().mouseClicked(mouseX, mouseY, mouseButton);
		}
		tfgeneric.mouseClicked(mouseX, mouseY, mouseButton);
		tfgeneric_value.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(keyCode == Keyboard.KEY_DELETE){
			for (Pair<GuiTextField, GuiTextField> tf : tfMap) {
				if((tf.getKey().isFocused() || tf.getValue().isFocused()) && tf.getKey().getId() >= 0){
					overrides.remove(tf.getKey().getId());
					initGui();
					break;
				}
			}
		}else{
			for (Pair<GuiTextField, GuiTextField> tf : tfMap) {
				tf.getKey().textboxKeyTyped(typedChar, keyCode);
				tf.getValue().textboxKeyTyped(typedChar, keyCode);
			}
			tfgeneric.textboxKeyTyped(typedChar, keyCode);
			tfgeneric_value.textboxKeyTyped(typedChar, keyCode);
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
