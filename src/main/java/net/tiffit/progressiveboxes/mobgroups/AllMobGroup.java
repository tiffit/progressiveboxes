package net.tiffit.progressiveboxes.mobgroups;

import net.minecraft.entity.Entity;

public class AllMobGroup implements MobGroup{

	@Override
	public String getID() {
		return "all";
	}

	@Override
	public boolean validMob(Entity ent) {
		return true;
	}

}
