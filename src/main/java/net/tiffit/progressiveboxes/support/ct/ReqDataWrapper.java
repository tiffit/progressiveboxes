package net.tiffit.progressiveboxes.support.ct;

import crafttweaker.annotations.ZenRegister;
import net.tiffit.progressiveboxes.data.ReqData;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("mod.pb.ReqData")
public class ReqDataWrapper {
	
	ReqData data;
	
	public ReqDataWrapper(String type, String value){
		data = new ReqData();
		data.type = type;
		data.value = value;
	}
}
