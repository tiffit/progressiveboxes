package net.tiffit.progressiveboxes.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.ProgressiveBoxes;
import net.tiffit.progressiveboxes.RecipeCombineBoxes;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.data.DropData;
import net.tiffit.progressiveboxes.item.BoxItem;
import net.tiffit.tiffitlib.RegistryHelper;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public static RegistryHelper helper = new RegistryHelper(ProgressiveBoxes.MODID);
	
    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void blockRegistry(RegistryEvent.Register<Block> e) {
    	helper.setBlockRegistry(e.getRegistry());
    	
    }

    @SubscribeEvent
    public static void itemRegistry(RegistryEvent.Register<Item> e) {
    	helper.setItemRegistry(e.getRegistry());
    	
    	helper.registerItemBlocks();
    	
    	helper.register(new BoxItem());
    	
    }
    
    @SubscribeEvent
    public static void recipeRegistry(RegistryEvent.Register<IRecipe> e) {
    	e.getRegistry().register(new RecipeCombineBoxes().setRegistryName(ProgressiveBoxes.MODID, "boxcombine"));
    }
    
	@SubscribeEvent
	public static void onKillEntity(LivingDropsEvent e) {
		EntityLivingBase ent = e.getEntityLiving();
		boolean player = e.getSource().getTrueSource() instanceof EntityPlayer;
		for(BoxData box : BoxRegistry.LOADED_BOXES){
			DropData chance = box.dropchance;
			if(player && box.dropchance_player != null)chance = box.dropchance_player;
			if(!player && box.dropchance_nonplayer != null)chance = box.dropchance_nonplayer;
			if(chance == null)continue;
			double realChance = chance.getChance(ent);
			System.out.println(box.name + "->" + realChance);
			if(realChance > 0 && Math.random() <= realChance){
				EntityItem item = new EntityItem(ent.world, ent.posX, ent.posY, ent.posZ, BoxItem.getStack(box));
				item.setDefaultPickupDelay();	
				e.getDrops().add(item);
			}
		}
	}
    
}

