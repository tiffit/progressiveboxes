package net.tiffit.progressiveboxes.data.req;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class AdvancementType implements ReqType {

	@Override
	public String getDisplayName() {
		return "Advancement";
	}

	@Override
	public String getID() {
		return "advancement";
	}

	@Override
	public boolean meetsReq(EntityPlayerMP p, String value) {
		Advancement adv = p.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(value));
		if(adv == null)return false;
		return p.getAdvancements().getProgress(adv).isDone();
	}

}
