package net.tiffit.progressiveboxes.data.req;

import net.minecraft.entity.player.EntityPlayerMP;

public interface ReqType {

	public String getDisplayName();
	public String getID();
	public boolean meetsReq(EntityPlayerMP p, String value);
	
}
