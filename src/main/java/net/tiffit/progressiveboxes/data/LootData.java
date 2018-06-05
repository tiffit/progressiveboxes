package net.tiffit.progressiveboxes.data;

import net.minecraft.entity.player.EntityPlayerMP;

public class LootData {

	public ItemData item = new ItemData();
	public int weight = 1;
	public ReqData[] requirements = new ReqData[0];
	
	public boolean meetsReqs(EntityPlayerMP p){
		if(requirements.length == 0)return true;
		for(ReqData req : requirements){
			if(!req.meetsReqs(p))return false;
		}
		return true;
	}
	
}
