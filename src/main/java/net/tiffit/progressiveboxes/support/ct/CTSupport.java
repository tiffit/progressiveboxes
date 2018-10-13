package net.tiffit.progressiveboxes.support.ct;

import crafttweaker.CraftTweakerAPI;

public class CTSupport {

	public static void registerBoxes(){
		CraftTweakerAPI.tweaker.loadScript(false, "progressiveboxes");
	}
	
}
