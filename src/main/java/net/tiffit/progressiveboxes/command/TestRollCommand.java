package net.tiffit.progressiveboxes.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.tiffit.progressiveboxes.BoxRegistry;
import net.tiffit.progressiveboxes.data.BoxData;
import net.tiffit.progressiveboxes.network.PacketOpenBox;
import net.tiffit.tiffitlib.network.NetworkManager;

public class TestRollCommand extends CommandBase {

	@Override
	public String getName() {
		return "pb_testroll";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.pb_testroll.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayerMP){
			EntityPlayerMP player = (EntityPlayerMP) sender;
			if(args.length != 2)throw new WrongUsageException("commands.pb_testroll.usage", new Object[0]);
			BoxData data = BoxRegistry.fromID(args[0]);
			if(data == null)throw new CommandException("commands.pb_testroll.unknownbox", new Object[]{args[0]});
			boolean forceRequirements = Boolean.parseBoolean(args[1]);
			List<ItemStack> loot = data.getLoot(player, forceRequirements);
			NetworkManager.NETWORK.sendTo(new PacketOpenBox(data.getColor(), loot), player);
		}else{
			throw new CommandException("commands.pb_testroll.playeronly", new Object[0]);
		}
	}
	
}
