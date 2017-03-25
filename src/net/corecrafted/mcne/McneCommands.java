package net.corecrafted.mcne;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class McneCommands implements CommandExecutor {

	McneMain plugin;

	public McneCommands(McneMain pl) {
		plugin = pl;
	}

	public static ArrayList<Player> lockedPlayer = new ArrayList<Player>();

	double nearRange = plugin.getConfig().getDouble("near-area");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length == 0) {
			sender.sendMessage(McneMain.prefix + ChatColor.RED + " Please specify an action [lock/unlock]");
			return true;
		} else if (args.length == 1) {

			if (args[0].equals("lock") || args[0].equals("unlock") || args[0].equals("toggle")) {
				sender.sendMessage(
						McneMain.prefix + ChatColor.RED + " Please select a target player [playername/-near]");
				return true;
			}
		} else {
			sender.sendMessage(
					McneMain.prefix + ChatColor.RED + " Please specify a proper action [lock/unlock/toggle]");
			return true;
		}
		if (args.length == 2) {
			if (args[0].equals("lock")) {
				if (args[1].equals("-near")) {
					addnearToLock(sender);
				} else {
					Player target = Bukkit.getPlayer(args[1]);
					lockedPlayer.add(target);
				}
				return true;
			}
			if (args[0].equals("unlock")){
				if (args[1].equals("-near")){
					removeFromNear(sender);
				} else {
					Player target = Bukkit.getPlayer(args[1]);
					lockedPlayer.remove(target);
				}
				return true;
			}
			if (args[0].equals("debug")){
				for (Player p: lockedPlayer){
					sender.sendMessage(p.getName()+"\n");
				}
			}
		}

		return true;
	}

	private void addnearToLock(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) player.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player1 = (Player) entity;
					lockedPlayer.add(player1);
				}
			}
		} else if (sender instanceof BlockCommandSender) {
			Block cmdBlock = (Block) sender;
			Location loc = cmdBlock.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) cmdBlock.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player = (Player) entity;
					lockedPlayer.add(player);
				}
			}
		} else {
			sender.sendMessage(McneMain.prefix + ChatColor.RED + " You must be in the game to execute this");
		}
	}
	
	private void removeFromNear(CommandSender sender){
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) player.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player1 = (Player) entity;
					lockedPlayer.remove(player1);
				}
			}
		} else if (sender instanceof BlockCommandSender) {
			Block cmdBlock = (Block) sender;
			Location loc = cmdBlock.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) cmdBlock.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player = (Player) entity;
					lockedPlayer.remove(player);
				}
			}
		} else {
			sender.sendMessage(McneMain.prefix + ChatColor.RED + " You must be in the game to execute this");
		}
	}
}
