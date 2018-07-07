package net.tiffit.progressiveboxes.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.tiffit.progressiveboxes.ConfigUtil;
import net.tiffit.progressiveboxes.ProgressiveBoxes;
import net.tiffit.progressiveboxes.client.ColorBox;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {



	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		ItemColors colors = Minecraft.getMinecraft().getItemColors();
		colors.registerItemColorHandler(new ColorBox(), ProgressiveBoxes.progressivebox);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		helper.registerModels();
	}
	
	@Override
	public void loadComplete(FMLLoadCompleteEvent e) {
		Minecraft minecraft = Minecraft.getMinecraft();
		IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) minecraft.getResourceManager();
		reloadableResourceManager.registerReloadListener(new IResourceManagerReloadListener() {
			
			@Override
			public void onResourceManagerReload(IResourceManager rm) {
				ConfigUtil.load(ProgressiveBoxes.configFolder.getParentFile());
			}
		});
	}

}
