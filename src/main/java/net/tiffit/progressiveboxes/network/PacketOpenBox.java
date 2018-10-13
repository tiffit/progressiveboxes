package net.tiffit.progressiveboxes.network;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tiffit.progressiveboxes.client.gui.GuiLootBox;

public class PacketOpenBox implements IMessage {

	private List<ItemStack> items;
	private int color;

	public PacketOpenBox() {
	}

	public PacketOpenBox(int color, List<ItemStack> stack) {
		this.items = stack;
		this.color = color;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		color = buf.readInt();
		int size = buf.readInt();
		items = new ArrayList<ItemStack>();
		for(int i = 0; i < size; i++){
			items.add(ByteBufUtils.readItemStack(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(color);
		buf.writeInt(items.size());
		for(ItemStack s : items)ByteBufUtils.writeItemStack(buf, s);
	}

	public static class Handler implements IMessageHandler<PacketOpenBox, IMessage> {
		@Override
		public IMessage onMessage(PacketOpenBox message, MessageContext ctx) {
			run(message);
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		private void run(PacketOpenBox message){
			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.player.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5f, 1);
			minecraft.addScheduledTask(() -> minecraft.displayGuiScreen(new GuiLootBox(message.color, message.items)));
		}
	}
}