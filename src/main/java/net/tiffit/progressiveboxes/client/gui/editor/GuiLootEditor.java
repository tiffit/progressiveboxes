package net.tiffit.progressiveboxes.client.gui.editor;

import java.io.IOException;
import java.util.HashMap;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.tiffit.progressiveboxes.ConfigUtil;
import net.tiffit.progressiveboxes.client.gui.editor.GuiBoxEditor.GuiButtonClean;
import net.tiffit.progressiveboxes.client.gui.editor.GuiBoxEditor.GuiResponderString;
import net.tiffit.progressiveboxes.data.ItemData;
import net.tiffit.progressiveboxes.data.LootData;
import net.tiffit.tiffitlib.client.GuiChildScreen;

public class GuiLootEditor extends GuiChildScreen {

	private LootData data;

	private HashMap<String, GuiTextField> textfields = new HashMap<>();

	public GuiLootEditor(GuiScreen parent, LootData data) {
		super(parent);
		this.data = data;
	}

	@Override
	public void initGui() {
		textfields.clear();
		GuiTextField tfitem = new GuiTextField(0, fontRenderer, 10, 40, 200, 20);
		tfitem.setMaxStringLength(100);
		setGuiTextField(tfitem, data.item.item, "item", (id, str) -> {
			data.item.item = str;
		});
		
		GuiTextField ftamount = new GuiTextField(0, fontRenderer, 10, 40 * 2, 30, 20);
		ftamount.setMaxStringLength(2);
		ftamount.setValidator((str) -> {
			if (str.isEmpty())
				return true;
			try {
				int parsed = Integer.parseInt(str);
				if(parsed < 0)return false;
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setGuiTextField(ftamount, "" + data.item.amount, "amount", (id, str) -> {
			data.item.amount = Integer.valueOf(str.isEmpty() ? "1" : str);
		});
		
		GuiTextField ftmeta = new GuiTextField(0, fontRenderer, 10 + 45, 40 * 2, 30, 20);
		ftamount.setMaxStringLength(6);
		ftmeta.setValidator((str) -> {
			if (str.isEmpty())
				return true;
			try {
				int parsed = Integer.parseInt(str);
				if(parsed < 0)return false;
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setGuiTextField(ftmeta, "" + data.item.meta, "meta", (id, str) -> {
			data.item.meta = Integer.valueOf(str.isEmpty() ? "0" : str);
		});
		
		GuiTextField tfnbt = new GuiTextField(0, fontRenderer, 10, 40*3, 200, 20);
		tfnbt.setMaxStringLength(1000);
		setGuiTextField(tfnbt, ConfigUtil.gsonSingleLine.toJson(data.item.nbt).replaceAll("\n", ""), "nbt", (id, str) -> {
			try {
				data.item.nbt = ConfigUtil.gson.fromJson(str, JsonObject.class);
			} catch (JsonSyntaxException e) {
				data.item.nbt = new JsonObject();
			}
		});
		
		GuiTextField ftweight = new GuiTextField(0, fontRenderer, 10, 40 * 4, 60, 20);
		ftweight.setValidator((str) -> {
			if (str.isEmpty())
				return true;
			try {
				int parsed = Integer.parseInt(str);
				if(parsed < 0)return false;
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setGuiTextField(ftweight, "" + data.weight, "weight", (id, str) -> {
			data.weight = Integer.valueOf(str.isEmpty() ? "1" : str);
		});
		
		GuiTextField ftgroup = new GuiTextField(0, fontRenderer, 10 + 70, 40 * 4, 60, 20);
		ftgroup.setValidator((str) -> {
			if (str.isEmpty())
				return true;
			try {
				Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setGuiTextField(ftgroup, "" + data.group, "group", (id, str) -> {
			data.group = Integer.valueOf(str.isEmpty() ? "-1" : str);
		});
		
		buttonList.add(new GuiButtonClean(0, 10, 40*5, 200, 20, "Requirements..."));
		if(mc.player != null && mc.player.inventory != null)buttonList.add(new GuiButtonClean(1, 10, 40*6, 200, 20, "Import Item From Inventory..."));
	}

	private void setGuiTextField(GuiTextField field, String text, String type, GuiResponderString responder) {
		field.setText(text);
		field.setGuiResponder(responder);
		textfields.put(type, field);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id == 0){
			mc.displayGuiScreen(new GuiReqEditor(this, Lists.newArrayList(data.requirements)));
		}else if(button.id == 1){
			mc.displayGuiScreen(new GuiInventoryImport(this));
		}
	}

	@Override
	public void returnTo(GuiChildScreen child) {
		if(child instanceof GuiReqEditor){
			GuiReqEditor gui = (GuiReqEditor) child;
			data.requirements = gui.getModifiedData();
		}else if(child instanceof GuiInventoryImport){
			GuiInventoryImport importer = (GuiInventoryImport) child;
			if(importer.selected){
				ItemStack in = importer.stack;
				ItemData data = this.data.item;
				data.item = in.getItem().getRegistryName().toString();
				data.amount = in.getCount();
				data.meta = in.getMetadata();
				data.nbt = in.hasTagCompound() ? ConfigUtil.gson.fromJson(in.getTagCompound().toString(), JsonObject.class) : new JsonObject();
				initGui();
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, "Loot Data Editor", this.width / 2, 5, 0xffffffff);
		drawHorizontalLine(0, width, 15, 0xffffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
		Item itm = Item.REGISTRY.getObject(new ResourceLocation(data.item.item));
		fontRenderer.drawString("Item" + (itm == null ? TextFormatting.RED + " Invalid!" : ""), 10, 30, 0xffffffff);
		fontRenderer.drawString("Amount", 10, 40*2-10, 0xffffffff);
		fontRenderer.drawString("Meta", 10+45, 40*2-10, 0xffffffff);
		boolean validnbt = true;
		try {
			ConfigUtil.gson.fromJson(textfields.get("nbt").getText(), JsonObject.class);
		} catch (JsonSyntaxException e) {
			validnbt = false;
		}
		fontRenderer.drawString("NBT" + (!validnbt ? TextFormatting.RED + " Invalid!" : ""), 10, 40*3-10, 0xffffffff);
		for (GuiTextField tf : textfields.values()) {
			tf.drawTextBox();
		}
		fontRenderer.drawString("Weight", 10, 40*4-10, 0xffffffff);
		fontRenderer.drawString("Group", 10 + 70, 40*4-10, 0xffffffff);
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(225, 50, 0);
			float scale = 5;
			ItemStack item = data.item.getStack();
			GlStateManager.scale(scale, scale, 0);
			RenderHelper.enableStandardItemLighting();
            GlStateManager.color(1, 1, 1, 1);
			mc.getRenderItem().renderItemAndEffectIntoGUI(item, 0, 0);
			String text = "";
            if(item.getCount() > 1)text = item.getCount() + "";
            mc.getRenderItem().renderItemOverlayIntoGUI(fontRenderer, item, 0, 0, text);
            RenderHelper.disableStandardItemLighting();
			GlStateManager.popMatrix();
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

}
