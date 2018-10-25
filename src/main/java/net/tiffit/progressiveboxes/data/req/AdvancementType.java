package net.tiffit.progressiveboxes.data.req;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@Override
	@SideOnly(Side.CLIENT)
	public String localizeValue(String value) {
		String[] nameArr = value.split(":");
		String name = nameArr[nameArr.length - 1];
		return "Requires The &c" + I18n.format("advancements." + name.replaceAll("/", ".") + ".title") + "&r Advancement";
	}

}
