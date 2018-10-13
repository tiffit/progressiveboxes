package net.tiffit.progressiveboxes.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.ProgressiveBoxes;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.network.PacketOpenBox;
import net.tiffit.tiffitlib.network.NetworkManager;

public class BoxItem extends Item{

	public BoxItem(){
		setUnlocalizedName(ProgressiveBoxes.MODID + ".progressivebox");
		setRegistryName("progressivebox");
		setCreativeTab(ProgressiveBoxes.CTAB);
		setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(isInCreativeTab(tab)){
			for(BoxData data : BoxRegistry.LOADED_BOXES){
				items.add(getStack(data));
			}
		}
	}
	
	public static ItemStack getCleanStack(){
		ItemStack s= new ItemStack(ProgressiveBoxes.progressivebox);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("clean", true);
		s.setTagCompound(tag);
		return s;
	}
	
	public static ItemStack getStack(BoxData data){
		ItemStack stack = new ItemStack(ProgressiveBoxes.progressivebox, 1);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("box", data.id);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public static BoxData getData(ItemStack stack){
		if(stack.hasTagCompound()){
			NBTTagCompound tag = stack.getTagCompound();
			if(tag.hasKey("box")){
				return BoxRegistry.fromID(tag.getString("box"));
			}
		}
		return null;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		BoxData data = getData(stack);
		if(data != null){
			tooltip.add(TextFormatting.GRAY + data.name + " Box");
			if(data.hasRarity())tooltip.add(TextFormatting.GRAY + "Rarity: " + data.rarity);
			if(!data.description.isEmpty()){
				String[] descLines = data.description.split("\\n");
				for(String l : descLines){
					tooltip.add(TextFormatting.DARK_GRAY + l);
				}
			}
		}else if(!stack.hasTagCompound() || !stack.getTagCompound().getBoolean("clean")){
			tooltip.add(TextFormatting.RED + "Error: Unknown Box");
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h) {
		ItemStack held = p.getHeldItem(h);
		BoxData data = getData(held);
        if (!p.capabilities.isCreativeMode)
        {
    		held.shrink(1);
        }
		if(p.isSneaking())new ActionResult<ItemStack>(EnumActionResult.PASS, held);
		if(!p.world.isRemote){
			if(data == null){
				p.sendMessage(new TextComponentString(TextFormatting.DARK_RED + "The box was empty..? (It contains no/unknown box data)"));
				return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
			}else{
				if(!data.meetsReqs((EntityPlayerMP) p)){
					held.grow(1);
					p.sendMessage(new TextComponentString(TextFormatting.DARK_RED + data.openmessage));
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, held);
				}
				List<ItemStack> loot = data.getLoot((EntityPlayerMP) p, false);
				NetworkManager.NETWORK.sendTo(new PacketOpenBox(data.getColor(), loot), (EntityPlayerMP) p);
				for(ItemStack stack : loot){
					EntityItem ent = new EntityItem(w, p.posX, p.posY, p.posZ, stack);
					ent.setPickupDelay(0);
					w.spawnEntity(ent);
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, held);
	}
}
