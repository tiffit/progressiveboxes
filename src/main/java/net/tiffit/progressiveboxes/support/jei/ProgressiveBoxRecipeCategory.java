package net.tiffit.progressiveboxes.support.jei;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.data.LootData;
import net.tiffit.progressiveboxes.data.ReqData;
import net.tiffit.progressiveboxes.data.req.ReqType;

public class ProgressiveBoxRecipeCategory implements IRecipeCategory<ProgressiveBoxRecipeWrapper> {

	public static final String uid = "progressiveboxes";
	
	private final IDrawable background;
	
	public ProgressiveBoxRecipeCategory(IGuiHelper guiHelper){
		ResourceLocation location = new ResourceLocation("progressiveboxes", "textures/gui/jei.png");
		background = guiHelper.createDrawable(location, 0, 0, 8*20, 5*20 + 20);
	}
	
	@Override
	public String getUid() {
		return uid;
	}

	@Override
	public String getTitle() {
		return "Progressive Boxes";
	}

	@Override
	public String getModName() {
		return "Progressive Boxes";
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void drawExtras(Minecraft mc) {
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ProgressiveBoxRecipeWrapper recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		BoxData data = recipe.getData();
		int size = data.loot.length;
		float total_weight = 0;
		
		stacks.init(0, true, 0, 0);
		
		for(int i = 0; i < size; i++){
			total_weight += data.loot[i].weight;
			stacks.init(i + 1, false, (i%8)*20, (i/8)*20 + 20);
		}
		final float FIN_TW = total_weight;
		recipeLayout.getIngredientsGroup(ItemStack.class).addTooltipCallback(new ITooltipCallback<ItemStack>() {
			
			@Override
			public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
				if(!input){
					LootData ld = data.loot[slotIndex - 1];
					float chance = (ld.weight/FIN_TW)*100;
					tooltip.add(TextFormatting.GRAY + "Chance: " + ItemStack.DECIMALFORMAT.format(chance) + "%");
					for(ReqData rd : ld.requirements){
						String type = rd.type;
						if(type.equalsIgnoreCase("none"))continue;
						ReqType rt = BoxRegistry.reqFromID(type.toLowerCase());
						tooltip.add(TextFormatting.RED + rt.localizeValue(rd.value).replace("&c", TextFormatting.DARK_RED.toString()).replace("&r", TextFormatting.RED.toString()));
					}
				}else{
					tooltip.add(TextFormatting.GRAY + "All Items Unique: " + data.unique);
					tooltip.add(TextFormatting.GRAY + "Items: " + data.amount);
					for(ReqData rd : data.requirements){
						String type = rd.type;
						if(type.equalsIgnoreCase("none"))continue;
						ReqType rt = BoxRegistry.reqFromID(type.toLowerCase());
						tooltip.add(TextFormatting.RED + rt.localizeValue(rd.value).replace("&c", TextFormatting.DARK_RED.toString()).replace("&r", TextFormatting.RED.toString()));
					}
				}
			}
		});
		stacks.set(ingredients);
	}

}
