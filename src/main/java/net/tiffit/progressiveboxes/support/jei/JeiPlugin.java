package net.tiffit.progressiveboxes.support.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.ProgressiveBoxes;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.item.BoxItem;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new ProgressiveBoxRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	public void register(IModRegistry registry) {
		registry.addRecipes(BoxRegistry.LOADED_BOXES, ProgressiveBoxRecipeCategory.uid);
		registry.handleRecipes(BoxData.class, new ProgressiveBoxRecipeWrapper.ProgressiveBoxRecipeWrapperFactory(), ProgressiveBoxRecipeCategory.uid);
		registry.addRecipeCatalyst(BoxItem.getCleanStack(), ProgressiveBoxRecipeCategory.uid);
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry registry) {
		registry.registerSubtypeInterpreter(ProgressiveBoxes.progressivebox, new ISubtypeInterpreter() {
			
			@Override
			public String apply(ItemStack itemStack) {
				BoxData data = BoxItem.getData(itemStack);
				if(data == null)return "NONE";
				return data.id;
			}
		});
	}
}
