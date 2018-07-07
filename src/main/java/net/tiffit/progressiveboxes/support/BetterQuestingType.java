package net.tiffit.progressiveboxes.support;

import java.util.UUID;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.questing.IQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.QuestDatabase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.tiffit.progressiveboxes.data.req.ReqType;

public class BetterQuestingType implements ReqType {

	@Override
	public String getDisplayName() {
		return "Better Questing";
	}

	@Override
	public String getID() {
		return "betterquesting";
	}

	@Override
	public boolean meetsReq(EntityPlayerMP player, String value) {
		UUID p = QuestingAPI.getQuestingUUID(player);
		DBEntry<IQuest>[] quests = QuestDatabase.INSTANCE.getEntries();
		for(DBEntry<IQuest> entry : quests){
			IQuest quest = entry.getValue();
			if(value.equals(quest.getUnlocalisedName())){
				return quest.isComplete(p);
			}
		}
		return false;
	}

	@Override
	public String localizeValue(String value) {
		DBEntry<IQuest>[] quests = QuestDatabase.INSTANCE.getEntries();
		for(DBEntry<IQuest> entry : quests){
			IQuest quest = entry.getValue();
			if(value.equals(quest.getUnlocalisedName())){
				return "Requires &c" + quest.getUnlocalisedName() + "&r Quest";
			}
		}
		return "Error";
	}

}
