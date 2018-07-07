package net.tiffit.progressiveboxes.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.tiffit.progressiveboxes.ConfigUtil;
import net.tiffit.progressiveboxes.ProgressiveBoxes;

public class ReloadCommand extends CommandBase{

	@Override
	public String getName() {
		return "pb_reload";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.pb_reload.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		ConfigUtil.load(ProgressiveBoxes.configFolder.getParentFile());
	}

}
