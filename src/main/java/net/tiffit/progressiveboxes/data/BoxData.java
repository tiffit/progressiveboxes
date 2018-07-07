package net.tiffit.progressiveboxes.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class BoxData {

	private static Random rand = new Random();
	
	public String name = "Unnamed";
	public String id = "mycoolbox";
	public String color = "FFFFFF";
	public String openmessage = "You are unable to open this!";
	public int amount = 1;
	public int rarity = -1;
	public boolean unique = true;
	public LootData[] loot = new LootData[0];
	public ReqData[] requirements = new ReqData[0];
	
	public DropData dropchance = new DropData();
	public DropData dropchance_player;
	public DropData dropchance_nonplayer;
	
	public int getColor(){
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
					if(unique)available.remove(p.data);
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
