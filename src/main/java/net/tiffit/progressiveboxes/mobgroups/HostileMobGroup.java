package net.tiffit.progressiveboxes.mobgroups;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;

public class HostileMobGroup implements MobGroup{

	@Override
	public String getID() {
		return "hostile";
	}

	@Override
	public boolean validMob(Entity ent) {
		return ent instanceof IMob;
	}

}
