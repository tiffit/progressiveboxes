package net.tiffit.progressiveboxes.mobgroups;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CustomMobGroup implements MobGroup{

	public String id;
	public String[] names;
	
	@Override
	public String getID() {
		return id;
	}

	@Override
	public boolean validMob(Entity ent) {
		ResourceLocation name = EntityRegistry.getEntry(ent.getClass()).getRegistryName();
		for(String n : names){
			if(n.equals(name.toString()))return true;
		}
		return false;
	}

}
