package net.tiffit.progressiveboxes;

import java.io.File;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.tiffit.progressiveboxes.item.BoxItem;
import net.tiffit.progressiveboxes.proxy.CommonProxy;

@Mod(modid = ProgressiveBoxes.MODID, version = ProgressiveBoxes.VERSION, name = ProgressiveBoxes.NAME, useMetadata = true, dependencies = ProgressiveBoxes.DEPENDENCIES)
@Mod.EventBusSubscriber
public class ProgressiveBoxes {
	public static final String MODID = "progressiveboxes";
	public static final String NAME = "Progressive Boxes";
	public static final String VERSION = "1.0.0";
	public static final String DEPENDENCIES = "required-after:tiffitlib;";
	
	@Instance(MODID)
	public static ProgressiveBoxes INSTANCE;
	public static Logger logger;
	public static File configFolder;
	
	@SidedProxy(clientSide = "net.tiffit.progressiveboxes.proxy.ClientProxy", serverSide = "net.tiffit.progressiveboxes.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	@GameRegistry.ObjectHolder(MODID + ":progressivebox")
	public static BoxItem progressivebox;
	
	
	public static CreativeTabs CTAB = new CreativeTabs("progressiveboxes") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(progressivebox);
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		configFolder = event.getSuggestedConfigurationFile().getParentFile();
		ConfigUtil.load(configFolder);
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}
	
	
	
}
