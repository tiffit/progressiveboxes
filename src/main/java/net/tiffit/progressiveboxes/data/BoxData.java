package net.tiffit.progressiveboxes.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.JsonIOException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.ConfigUtil;

public class BoxData {

	private static Random rand = new Random();
	
	public String name = "Unnamed";
	public String id = "mycoolbox";
	public String color = "FFFFFF";
	public String openmessage = "You are unable to open this!";
	public String description = "";
	public int amount = 1;
	public int rarity = -1;
	public boolean unique = true;
	public LootData[] loot = new LootData[0];
	public ReqData[] requirements = new ReqData[0];
	
	public DropData dropchance = new DropData();
	public DropData dropchance_player;
	
	public transient File file;
	
	public int getColor(){
		if(color.isEmpty())return 0;
		return Integer.parseInt(color, 16);
	}

	public List<ItemStack> getLoot(EntityPlayerMP pl, boolean forceRequirements){
		List<ItemStack> loot = new ArrayList<ItemStack>();
		
		List<LootData> available = new ArrayList<LootData>();
		for(LootData d : this.loot){
			if(d.meetsReqs(pl) || forceRequirements){
				available.add(d);
			}
		}
		
		for(int i = 0; i < amount && available.size() > 0; i++){
			LootProb[] probs = LootProb.create(available);
			int val = rand.nextInt(probs[probs.length-1].max);
			for(LootProb p : probs){
				if(p.inRange(val)){
					ItemStack newS = p.data.item.getStack();
					if(!newS.isEmpty())loot.add(newS);
					if(unique){
						available.remove(p.data);
						if(p.data.group >= 0){
							List<LootData> copy = new ArrayList<LootData>(available);
							for(LootData d : copy){
								if(d.group == p.data.group)available.remove(d);
							}
						}
					}
					break;
				}
			}
		}
		
		return loot;
	}
	
	public boolean meetsReqs(EntityPlayerMP p){
		if(requirements.length == 0)return true;
		for(ReqData req : requirements){
			if(!req.meetsReqs(p))return false;
		}
		return true;
	}
	
	public boolean hasRarity(){
		return rarity > 0;
	}
	
	public void save(){
		if(file != null){
			try {
				String json = ConfigUtil.gson.toJson(this);
				FileWriter writer = new FileWriter(file);
				writer.write(json);
				writer.close();
			} catch (JsonIOException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void delete(){
		BoxRegistry.LOADED_BOXES.remove(this);
		if(file != null)file.delete();

	}
	
	private static class LootProb{
		
		int min;
		int max;
		LootData data;
		
		static LootProb[] create(List<LootData> data){
			LootProb[] prob = new LootProb[data.size()];
			int high = 0;
			for(int i = 0; i < data.size(); i++){
				LootData d = data.get(i);
				LootProb p = new LootProb();
				p.data = d;
				prob[i] = p;
				p.min = high;
				high += d.weight;
				p.max = high;
			}
			return prob;
		}
		
		boolean inRange(int val){
			return val >= min && val < max;
		}
		
	}
	
}
