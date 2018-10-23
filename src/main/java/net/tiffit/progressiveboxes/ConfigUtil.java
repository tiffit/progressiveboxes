package net.tiffit.progressiveboxes;

import static net.tiffit.progressiveboxes.ProgressiveBoxes.configFolder;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.minecraftforge.fml.common.Loader;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.data.req.AdvancementType;
import net.tiffit.progressiveboxes.data.req.ItemType;
import net.tiffit.progressiveboxes.data.req.WorldAgeType;
import net.tiffit.progressiveboxes.mobgroups.AllMobGroup;
import net.tiffit.progressiveboxes.mobgroups.BossMobGroup;
import net.tiffit.progressiveboxes.mobgroups.CustomMobGroup;
import net.tiffit.progressiveboxes.mobgroups.HostileMobGroup;
import net.tiffit.progressiveboxes.mobgroups.PassiveMobGroup;
import net.tiffit.progressiveboxes.mobgroups.PlayerMobGroup;
import net.tiffit.progressiveboxes.support.BetterQuestingType;
import net.tiffit.progressiveboxes.support.GameStagesType;
import net.tiffit.progressiveboxes.support.ReskillableType;
import net.tiffit.progressiveboxes.support.ct.CTSupport;

public class ConfigUtil {

	public static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
	public static Gson gsonSingleLine = new GsonBuilder().setLenient().create();

	
	public static void load(File f){
		BoxRegistry.LOADED_BOXES.clear();
		BoxRegistry.LOADED_MOBGROUPS.clear();
		BoxRegistry.LOADED_REQTYPES.clear();
		
		configFolder = new File(f, "progressiveboxes");
		if (!configFolder.exists())configFolder.mkdir();
		
		File boxFolder = new File(configFolder, "boxes");
		if (!boxFolder.exists())boxFolder.mkdir();
		
		File mobgroupsFolder = new File(configFolder, "mobgroups");
		if (!mobgroupsFolder.exists())mobgroupsFolder.mkdir();
		
		FileFilter jsonFilter = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".json");
			}
		};

		loadBoxes(boxFolder.listFiles(jsonFilter));
		loadMobGroups(mobgroupsFolder.listFiles(jsonFilter));

		
		BoxRegistry.LOADED_REQTYPES.add(new AdvancementType());
		BoxRegistry.LOADED_REQTYPES.add(new ItemType());
		BoxRegistry.LOADED_REQTYPES.add(new WorldAgeType());
		if(Loader.isModLoaded("betterquesting"))BoxRegistry.LOADED_REQTYPES.add(new BetterQuestingType());
		if(Loader.isModLoaded("gamestages"))BoxRegistry.LOADED_REQTYPES.add(new GameStagesType());
		if(Loader.isModLoaded("reskillable"))BoxRegistry.LOADED_REQTYPES.add(new ReskillableType());

		if(Loader.isModLoaded("crafttweaker"))CTSupport.registerBoxes();
	}
	
	private static void loadBoxes(File[] files){
		for(File f : files){
			try {
				BoxData data = gson.fromJson(new FileReader(f), BoxData.class);
				data.file = f;
				BoxRegistry.LOADED_BOXES.add(data);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				ProgressiveBoxes.logger.error("Error loading box: " + e.getMessage());
			}
		}
	}
	
	private static void loadMobGroups(File[] files){
		BoxRegistry.LOADED_MOBGROUPS.add(new AllMobGroup());
		BoxRegistry.LOADED_MOBGROUPS.add(new BossMobGroup());
		BoxRegistry.LOADED_MOBGROUPS.add(new HostileMobGroup());
		BoxRegistry.LOADED_MOBGROUPS.add(new PassiveMobGroup());
		BoxRegistry.LOADED_MOBGROUPS.add(new PlayerMobGroup());
		for(File f : files){
			try {
				CustomMobGroup data = gson.fromJson(new FileReader(f), CustomMobGroup.class);
				BoxRegistry.LOADED_MOBGROUPS.add(data);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				ProgressiveBoxes.logger.error("Error loading mobgroup: " + e.getMessage());
			}
		}
	}
	
}
