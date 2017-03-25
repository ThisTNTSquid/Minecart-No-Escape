package net.corecrafted.mcne;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class McneCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length==0){
			sender.sendMessage(McneMain.prefix+ChatColor.RED+" Please specify an action [lock/unlock/toggle]");
		}
		return false;
	}

}
