package net.tiffit.progressiveboxes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.item.BoxItem;

public class RecipeCombineBoxes extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{

	@Override
	public boolean matches(InventoryCrafting inv, World w) {
		int totalRarity = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty())continue;
			if(!(stack.getItem() instanceof BoxItem))return false;
			BoxData data = BoxItem.getData(stack);
			if(data == null)return false;
			if(!data.hasRarity())return false;
			totalRarity += data.rarity;
		}
		
		return getBoxWithRarity(totalRarity) != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		int totalRarity = 0;
		for(int i = 0; i < inv.getSizeInventory(); i++){
			if((inv.getStackInSlot(i).isEmpty()))continue;
			BoxData data = BoxItem.getData(inv.getStackInSlot(i));
			totalRarity += data.rarity;
		}
		return BoxItem.getStack(getBoxWithRarity(totalRarity));
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	private BoxData getBoxWithRarity(int rarity){
		for(BoxData d : BoxRegistry.LOADED_BOXES){
			if(d.rarity == rarity)return d;
		}
		return null;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
}
