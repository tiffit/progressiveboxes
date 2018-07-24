package net.tiffit.progressiveboxes.support;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import net.minecraft.entity.player.EntityPlayerMP;
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
		String[] keyval = value.split(":");
		String name = keyval[0];
		int level = Integer.valueOf(keyval[1]);
		for(PlayerSkillInfo info : data.getAllSkillInfo()){
			if(info.skill.getName().equalsIgnoreCase(name)){
				return info.getLevel() >= level;
			}
		}
		return false;
	}

	@Override
	public String localizeValue(String value) {
		return "";
	}

}
