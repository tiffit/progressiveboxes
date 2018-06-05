package net.tiffit.progressiveboxes.data;

import net.minecraft.entity.player.EntityPlayerMP;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.data.req.ReqType;

public class ReqData {

	public String type = "none";
	public String value = "";
	
	public boolean meetsReqs(EntityPlayerMP p){
		if(type.equalsIgnoreCase("none"))return true;
		ReqType type = BoxRegistry.reqFromID(this.type.toLowerCase());
		if(type != null)return type.meetsReq(p, value);
		return false;
	}
	
}
