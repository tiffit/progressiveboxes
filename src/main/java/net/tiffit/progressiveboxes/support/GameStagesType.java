package net.tiffit.progressiveboxes.support;

import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.tiffit.progressiveboxes.data.req.ReqType;

public class GameStagesType implements ReqType {

	@Override
	public String getDisplayName() {
		return "Game Stages";
	}

	@Override
	public String getID() {
		return "gamestage";
	}

	@Override
	public boolean meetsReq(EntityPlayerMP player, String value) {
		IStageData data = GameStageHelper.getPlayerData(player);
		return data.hasStage(value);
	}

	@Override
	public String localizeValue(String value) {
		return "Requires &c" + value + "&r Stage";
	}

}
