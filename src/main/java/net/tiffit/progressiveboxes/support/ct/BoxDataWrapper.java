package net.tiffit.progressiveboxes.support.ct;

import crafttweaker.annotations.ZenRegister;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.data.LootData;
import net.tiffit.progressiveboxes.data.ReqData;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@ZenRegister
@ZenClass("mod.pb.BoxData")
public class BoxDataWrapper {

	private final BoxData data;
	
	public BoxDataWrapper(BoxData data){
		this.data = data;
	}
	
	@ZenMethod
	public void addItem(LootDataWrapper ld){
		LootData[] orig = data.loot;
		LootData[] newArr = new LootData[orig.length + 1];
		System.arraycopy(orig, 0, newArr, 0, orig.length);
		newArr[newArr.length - 1] = ld.data;
		data.loot = newArr;
	}
	
	@ZenGetter("name")
	public String getName(){return data.name;}
	
	@ZenSetter("name")
	public void setName(String name){data.name = name;}
	
	@ZenGetter("color")
	public String getColor(){return data.color;}
	
	@ZenSetter("color")
	public void setColor(String color){data.color = color;}
	
	@ZenGetter("openmessage")
	public String getOpenMessage(){return data.openmessage;}
	
	@ZenSetter("openmessage")
	public void setOpenMessage(String openmessage){data.openmessage = openmessage;}
	
	@ZenGetter("description")
	public String getDescription(){return data.description;}
	
	@ZenSetter("description")
	public void setDescription(String description){data.description = description;}
	
	@ZenGetter("amount")
	public int getAmount(){return data.amount;}
	
	@ZenSetter("amount")
	public void setAmount(int amount){data.amount = amount;}
	
	@ZenGetter("rarity")
	public int getRarity(){return data.rarity;}
	
	@ZenSetter("rarity")
	public void setRarity(int rarity){data.rarity = rarity;}
	
	@ZenSetter("reqs")
	public void setReqs(ReqDataWrapper[] data){
		ReqData[] reqs = new ReqData[data.length];
		for(int i = 0; i < data.length; i++)reqs[i] = data[i].data;
		this.data.requirements = reqs;
	}
}
