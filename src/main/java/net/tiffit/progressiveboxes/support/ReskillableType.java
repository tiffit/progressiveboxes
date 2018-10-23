package net.tiffit.progressiveboxes.support;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.requirement.Requirement;
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
		Requirement requirement = ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(value);
		return requirement != null && data.requirementAchieved(requirement);
	}

	@Override
	public String localizeValue(String value) {
		Requirement requirement = ReskillableAPI.getInstance().getRequirementRegistry().getRequirement(value);
		return requirement == null ? "Error" : "Requires: " + String.format(requirement.internalToolTip(), TextFormatting.RESET);
	}

}