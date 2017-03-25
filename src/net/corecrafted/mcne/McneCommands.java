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

	public McneCommands(McneMain pl) {
		plugin = pl;
	}

	McneMain plugin;

	String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
	String unlockMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("unlock-message"));
	String lockMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("lock-message"));
	public static ArrayList<Player> lockedPlayer = new ArrayList<Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		double range = plugin.getConfig().getDouble("near-area");

		if (args.length == 0) {
			sender.sendMessage(prefix + ChatColor.RED + " Please specify an action [lock/unlock]");
			return true;
		} else if (args.length == 1) {

			if (args[0].equals("list")) {
				sender.sendMessage(ChatColor.YELLOW + "Current Minecart Locked Player: \n");
				for (Player p : lockedPlayer) {
					sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + p.getName() + "\n");
				}
			}

			if (args[0].equals("lock") || args[0].equals("unlock") || args[0].equals("toggle")) {
				sender.sendMessage(
						prefix + ChatColor.RED + " Please select a target player [playername/-near]");
				return true;
			} else {
				if (!(args[0].equals("list"))) {
					sender.sendMessage(
							prefix + ChatColor.RED + " Please specify a proper action [lock/unlock/toggle]");
					return true;
				}
			}

		}
		if (args.length == 2) {
			if (args[0].equals("lock")) {
				if (args[1].equals("-near")) {
					addnearToLock(sender, range);
					sender.sendMessage(prefix + ChatColor.GREEN + " Add - OK");
				} else {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(prefix + ChatColor.RED + " Add - The player is not online");
					} else {
						lockedPlayer.add(target.getPlayer());
						sender.sendMessage(prefix + ChatColor.GREEN + " Add - OK");
						target.sendMessage(prefix + " "+lockMsg);
					}
				}
				return true;
			}
			if (args[0].equals("unlock")) {
				if (args[1].equals("-near")) {
					removeFromNear(sender, range);
					sender.sendMessage(prefix + ChatColor.GREEN + " Remove - OK");
				} else {
					Player target = Bukkit.getPlayer(args[1]);
					if (target == null) {
						sender.sendMessage(prefix + ChatColor.RED + " Remove - The player is not online");
					} else {
						lockedPlayer.remove(target.getPlayer());
						sender.sendMessage(prefix + ChatColor.GREEN + " Remove - OK");
						target.sendMessage(prefix + " "+unlockMsg);
					}
				}
				return true;
			}

		}

		return true;
	}

	private void addnearToLock(CommandSender sender, double nearRange) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) player.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player1 = (Player) entity;
					lockedPlayer.add(player1);
					player1.sendMessage(prefix + " "+lockMsg);
				}
			}
		} else if (sender instanceof BlockCommandSender) {
			Block cmdBlock = ((BlockCommandSender) sender).getBlock();
			Location loc = cmdBlock.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) cmdBlock.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player = (Player) entity;
					lockedPlayer.add(player);
					player.sendMessage(prefix + " "+lockMsg);
				}
			}
		} else {
			sender.sendMessage(prefix + ChatColor.RED + " You must be in the game to execute this");
		}
	}

	private void removeFromNear(CommandSender sender, double nearRange) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) player.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player1 = (Player) entity;
					lockedPlayer.remove(player1);
					player1.sendMessage(prefix + " "+unlockMsg);
				}
			}
		} else if (sender instanceof BlockCommandSender) {
			Block cmdBlock = ((BlockCommandSender) sender).getBlock();
			Location loc = cmdBlock.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) cmdBlock.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player = (Player) entity;
					lockedPlayer.remove(player);
					player.sendMessage(prefix + " "+unlockMsg);
				}
			}
		} else {
			sender.sendMessage(prefix + ChatColor.RED + " You must be in the game to execute this");
		}
	}
}
