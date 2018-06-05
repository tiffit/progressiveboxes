package net.tiffit.progressiveboxes.mobgroups;

import net.minecraft.entity.Entity;

public class BossMobGroup implements MobGroup{

	@Override
	public String getID() {
		return "boss";
	}

	@Override
	public boolean validMob(Entity ent) {
		return !ent.isNonBoss();
	}

}
