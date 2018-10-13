package net.tiffit.progressiveboxes.support.ct;

import crafttweaker.annotations.ZenRegister;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.data.BoxData;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mod.pb.BoxFactory")
public class BoxFactory {

	@ZenMethod
	public static BoxDataWrapper createBox(String id) {
		BoxData data = new BoxData();
		data.id = id;
		BoxRegistry.LOADED_BOXES.add(data);
		return new BoxDataWrapper(data);
	}
	
	@ZenMethod
	public static LootDataWrapper createLootData() {
		return new LootDataWrapper();
	}
	
	@ZenMethod
	public static ReqDataWrapper getRequirement(String type, String value) {
		return new ReqDataWrapper(type, value);
	}
	
}
