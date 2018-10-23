package net.tiffit.progressiveboxes.support;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import net.tiffit.progressiveboxes.data.req.ReqType;

public class ReskillableType implements ReqType {

	@Override
	public String getDisplayName() {
		return "Reskillable";
	}

	@Override
	public String getID() {
		return "reskillable";
	}

	@Override
	public boolean meetsReq(EntityPlayerMP p, String value) {
		PlayerData data = PlayerDataHandler.get(p);
		return data.requirementAchieved(ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(value));
	}

	@Override
	public String localizeValue(String value) {
		return "Requires: " + String.format(ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(value).internalToolTip(), TextFormatting.RESET);
	}

}
