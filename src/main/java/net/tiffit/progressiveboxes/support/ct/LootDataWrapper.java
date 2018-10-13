package net.tiffit.progressiveboxes.support.ct;

import com.google.gson.JsonObject;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.WeightedItemStack;
import net.minecraft.item.ItemStack;
import net.tiffit.progressiveboxes.ConfigUtil;
import net.tiffit.progressiveboxes.data.ItemData;
import net.tiffit.progressiveboxes.data.LootData;
import net.tiffit.progressiveboxes.data.ReqData;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mod.pb.LootData")
public class LootDataWrapper {

	public final LootData data;

	public LootDataWrapper() {
		this.data = new LootData();
	}

	@ZenMethod
	public void setItem(IIngredient stackWrapper) {
		ItemData data = new ItemData();
		ItemStack stack = (ItemStack) stackWrapper.getInternal();
		this.data.item = data;
		data.item = stack.getItem().getRegistryName().toString();
		data.meta = stack.getMetadata();
		data.amount = stack.getCount();
		if (stack.hasTagCompound()) {
			data.nbt = ConfigUtil.gson.fromJson(stack.getTagCompound().toString(), JsonObject.class);
		}
	}
	
	@ZenMethod
	public void setItemWeighted(WeightedItemStack stackWrapper) {
		ItemData data = new ItemData();
		System.out.println(stackWrapper.getChance());
		this.data.weight = (int)stackWrapper.getChance();
		ItemStack stack = (ItemStack) stackWrapper.getStack().getInternal();
		this.data.item = data;
		data.item = stack.getItem().getRegistryName().toString();
		data.meta = stack.getMetadata();
		data.amount = stack.getCount();
		if (stack.hasTagCompound()) {
			data.nbt = ConfigUtil.gson.fromJson(stack.getTagCompound().toString(), JsonObject.class);
		}
	}
	
	@ZenMethod
	public void setWeight(int weight){
		this.data.weight = weight;
	}
	
	@ZenMethod
	public void setGroup(int group){
		this.data.group = group;
	}
	
	@ZenMethod
	public void setReqs(ReqDataWrapper[] data){
		ReqData[] reqs = new ReqData[data.length];
		for(int i = 0; i < data.length; i++)reqs[i] = data[i].data;
		this.data.requirements = reqs;
	}
}
