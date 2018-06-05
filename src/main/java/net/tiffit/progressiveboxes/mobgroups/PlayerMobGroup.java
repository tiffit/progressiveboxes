package net.tiffit.progressiveboxes.mobgroups;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerMobGroup implements MobGroup{

	@Override
	public boolean validMob(Entity ent) {
		return ent instanceof EntityPlayer;
	}

	@Override
	public String getID() {
		return "player";
	}

}
