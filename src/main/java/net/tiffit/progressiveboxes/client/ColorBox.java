package net.tiffit.progressiveboxes.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.item.BoxItem;

public class ColorBox implements IItemColor {

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		int color = 0xFFFFFF;
		BoxData data = BoxItem.getData(stack);
		if(data != null)color = data.getColor();
		return color;
	}

}
