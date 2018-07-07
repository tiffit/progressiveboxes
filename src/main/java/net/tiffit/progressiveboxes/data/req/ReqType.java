package net.tiffit.progressiveboxes.data.req;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ReqType {

	public String getDisplayName();
	public String getID();
	public boolean meetsReq(EntityPlayerMP p, String value);
	
	@SideOnly(Side.CLIENT)
	public String localizeValue(String value);
	
}
