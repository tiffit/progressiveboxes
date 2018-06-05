package net.tiffit.progressiveboxes.data;

import net.minecraft.entity.Entity;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.mobgroups.MobGroup;

public class DropData {

	public double chance = 0;
	public MobGroupOverrideData[] overrides = new MobGroupOverrideData[0];
	
	public double getChance(Entity ent){
		for(MobGroupOverrideData group : overrides){
			MobGroup g = BoxRegistry.groupFromID(group.mobgroup);
			if(g != null){
				if(g.validMob(ent))return group.chance;
			}
		}
		return chance;
	}
	
}
