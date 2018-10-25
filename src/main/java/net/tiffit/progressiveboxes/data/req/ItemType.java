package net.tiffit.progressiveboxes.data.req;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemType implements ReqType {

	@Override
	public String getDisplayName() {
		return "Item";
	}

	@Override
	public String getID() {
		return "item";
	}

	@Override
	public boolean meetsReq(EntityPlayerMP p, String value) {
		Item item = Item.REGISTRY.getObject(new ResourceLocation(value));
		if(item == null)return false;
		return p.inventory.hasItemStack(new ItemStack(item));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String localizeValue(String value) {
		Item item = Item.REGISTRY.getObject(new ResourceLocation(value));
		if(item == null)return "Error";
		ItemStack stack = new ItemStack(item);
		return "Requires &c" + stack.getDisplayName() + "&r To Be In The Inventory";
	}

}
