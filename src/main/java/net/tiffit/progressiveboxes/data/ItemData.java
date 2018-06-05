package net.tiffit.progressiveboxes.data;

import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;
import net.tiffit.progressiveboxes.ProgressiveBoxes;

public class ItemData {

	public String item = "minecraft:stone";
	public int amount = 1;
	public int meta = 0;
	public JsonObject nbt = new JsonObject();
	
	public ItemStack getStack(){
        Item item = Item.REGISTRY.getObject(new ResourceLocation(this.item));
        if(item == null)ProgressiveBoxes.logger.error("Invalid Item: " + item);
        ItemStack stack = new ItemStack(item, amount, meta);
        if(nbt.size() > 0){
        	try {
				stack.setTagCompound(JsonToNBT.getTagFromJson(nbt.toString()));
			} catch (NBTException e) {
				ProgressiveBoxes.logger.error(e.getMessage());
			}
        }
        return stack;
	}
	
}
