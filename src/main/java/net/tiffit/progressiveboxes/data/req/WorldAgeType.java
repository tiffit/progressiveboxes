package net.tiffit.progressiveboxes.data.req;

import net.minecraft.entity.player.EntityPlayerMP;

public class WorldAgeType implements ReqType {

	@Override
	public String getDisplayName() {
		return "World Age";
	}

	@Override
	public String getID() {
		return "worldage";
	}

	@Override
	public boolean meetsReq(EntityPlayerMP p, String value) {
		try {
			long ival = Long.valueOf(value);
			return p.getServerWorld().getWorldTime() / 24000L >= ival;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
