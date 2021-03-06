package net.tiffit.progressiveboxes.support.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.item.BoxItem;

public class ProgressiveBoxRecipeWrapper implements IRecipeWrapper {

	private BoxData data;

	public ProgressiveBoxRecipeWrapper(BoxData data) {
		this.data = data;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, BoxItem.getStack(data));
		int pageSize = 8*5;
		int pageAmount = data.loot.length/pageSize + 1;
		List<List<ItemStack>> drops = new ArrayList<List<ItemStack>>();
		for (int i = 0; i < Math.min(pageSize, data.loot.length); i++) {
			List<ItemStack> inner = new ArrayList<ItemStack>();
			inner.add(data.loot[i].item.getStack());
			if(pageAmount > 1){
				for(int j = 0; j < pageAmount; j++){
					ItemStack stack = null;
					int index = j * pageSize + i;
					if(index < data.loot.length){
						stack = data.loot[index].item.getStack();
						stack.setTagInfo("index", new NBTTagInt(index));
					}
					inner.add(stack);
				}
			}
			drops.add(inner);
		}
		ingredients.setOutputLists(ItemStack.class, drops);
	}
	
	public BoxData getData() {
		return data;
	}
	
	@Override
	public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int x, int y) {
		mc.fontRenderer.drawStringWithShadow("T", 140, 4, 0xffffff);
		mc.fontRenderer.drawStringWithShadow("F", 150, 4, 0xffffff);
		if(x >= 140 && x <= 160 && y > 0 && y < 15){
			mc.fontRenderer.drawStringWithShadow("Test Open", 45, 0, 0xffffff);
			if(x < 150)mc.fontRenderer.drawString("(Reqs: True)", 45, 10, 0x666666);
			else mc.fontRenderer.drawString("(Reqs: False)", 45, 10, 0x666666);
		}else{
			mc.fontRenderer.drawStringWithShadow(data.name + " Box", 45, 4, 0xffffff);
		}
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int x, int y, int mouseButton) {
		if(x >= 140 && x <= 160 && y > 0 && y < 15){
			Minecraft.getMinecraft().player.sendChatMessage("/pb_testroll " + data.id + (x < 150 ? " true" : " false"));
			return true;
		}
		return false;
	}
	
	public static class ProgressiveBoxRecipeWrapperFactory implements IRecipeWrapperFactory<BoxData> {

		@Override
		public IRecipeWrapper getRecipeWrapper(BoxData recipe) {
			return new ProgressiveBoxRecipeWrapper(recipe);
		}

	}

}
