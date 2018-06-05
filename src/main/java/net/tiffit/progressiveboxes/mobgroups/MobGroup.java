package net.tiffit.progressiveboxes.mobgroups;

import net.minecraft.entity.Entity;

public interface MobGroup {

	public String getID();
	
	public boolean validMob(Entity ent);
	
}
