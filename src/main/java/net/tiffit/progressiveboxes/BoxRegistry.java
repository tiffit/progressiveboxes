package net.tiffit.progressiveboxes;

import java.util.ArrayList;
import java.util.List;

import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.data.req.ReqType;
import net.tiffit.progressiveboxes.mobgroups.MobGroup;

public class BoxRegistry {

	public static List<BoxData> LOADED_BOXES = new ArrayList<BoxData>();
	public static List<MobGroup> LOADED_MOBGROUPS = new ArrayList<MobGroup>();
	public static List<ReqType> LOADED_REQTYPES = new ArrayList<ReqType>();

	public static BoxData fromID(String id){
		for(BoxData data : LOADED_BOXES){
			if(data.id.equals(id)){
				return data;
			}
		}
		return null;
	}
	
	public static MobGroup groupFromID(String id){
		for(MobGroup data : LOADED_MOBGROUPS){
			if(data.getID().equals(id)){
				return data;
			}
		}
		return null;
	}
	
	public static ReqType reqFromID(String id){
		for(ReqType data : LOADED_REQTYPES){
			if(data.getID().equals(id)){
				return data;
			}
		}
		return null;
	}
}
