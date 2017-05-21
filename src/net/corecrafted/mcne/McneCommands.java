package net.corecrafted.mcne;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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

	public static ArrayList<Player> lockedPlayer = new ArrayList<Player>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		double range = plugin.getConfig().getDouble("near-area");
		final String unlockMsg = ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("unlock-message"));
		final String lockMsg = ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("lock-message"));
		final String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
		final String noPermMsg = ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("no-permission-message"));

		if (args.length == 0) {
			sender.sendMessage(prefix + ChatColor.RED + " Please specify an action [lock/unlock/list/reload]");
			return true;
		} else if (args.length == 1) {

			if (args[0].equalsIgnoreCase("list")) {
				if (sender.hasPermission("mcne.list")) {
					sender.sendMessage(ChatColor.YELLOW + "Current Minecart Locked Player: \n");
					for (Player p : lockedPlayer) {
						sender.sendMessage(ChatColor.GRAY + "- " + ChatColor.GOLD + p.getName() + "\n");
						return true;
					}
				} else {
					sender.sendMessage(prefix + " " + noPermMsg);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("mcne.reload")) {
					plugin.reloadConfig();
					sender.sendMessage(prefix + ChatColor.GREEN + " Config reloaded !");
					return true;
				} else {
					sender.sendMessage(prefix + " " + noPermMsg);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("list")) {
				return true;
			}
			if (args[0].equalsIgnoreCase("lock") || args[0].equalsIgnoreCase("unlock")) {
				sender.sendMessage(prefix + ChatColor.RED + " Please select a target player [playername/-near]");
				return true;
			} else {
				sender.sendMessage(
						prefix + ChatColor.RED + " Please specify a proper action [lock/unlock/list/reload]");
				return true;

			}

		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("lock")) {
				if (sender.hasPermission("mcne.control")) {
					if (args[1].equalsIgnoreCase("-near")) {
						addnearToLock(sender, range);
						sender.sendMessage(prefix + ChatColor.GREEN + " Add - OK");
					} else {
						Player target = Bukkit.getPlayer(args[1]);
						if (target == null) {
							sender.sendMessage(prefix + ChatColor.RED + " Add - The player is not online");
						} else {
							if (!(lockedPlayer.contains(target))) {
								lockedPlayer.add(target.getPlayer());
								sender.sendMessage(prefix + ChatColor.GREEN + " Add - OK");
								target.sendMessage(prefix + " " + lockMsg);
								target.playSound(target.getLocation(),
										Sound.valueOf(plugin.getConfig().getString("sounds.lock.sound")),
										(float) plugin.getConfig().getDouble("sounds.lock.volume"),
										(float) plugin.getConfig().getDouble("sounds.lock.volume"));
							} else {
								sender.sendMessage(prefix + ChatColor.GOLD + " Add - Player already locked");
							}

						}
					}
				} else {
					sender.sendMessage(prefix + " " + noPermMsg);
					return true;
				}

			}
			if (args[0].equalsIgnoreCase("unlock")) {
				if (sender.hasPermission("mcne.control")) {
					if (args[1].equalsIgnoreCase("-near")) {
						removeFromNear(sender, range);
						sender.sendMessage(prefix + ChatColor.GREEN + " Remove - OK");
					} else {
						Player target = Bukkit.getPlayer(args[1]);
						if (target == null) {
							sender.sendMessage(prefix + ChatColor.RED + " Remove - The player is not online");
						} else {
							lockedPlayer.remove(target.getPlayer());
							sender.sendMessage(prefix + ChatColor.GREEN + " Remove - OK");
							target.sendMessage(prefix + " " + unlockMsg);
							target.playSound(target.getLocation(),
									Sound.valueOf(plugin.getConfig().getString("sounds.unlock.sound")),
									(float) plugin.getConfig().getDouble("sounds.unlock.volume"),
									(float) plugin.getConfig().getDouble("sounds.unlock.volume"));
						}
					}
				} else {
					sender.sendMessage(prefix + " " + noPermMsg);
					return true;
				}

			}

		}

		return true;
	}

	private void addnearToLock(CommandSender sender, double nearRange) {

		final String lockMsg = ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("lock-message"));
		final String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));

		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) player.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player1 = (Player) entity;
					if (!(lockedPlayer.contains(player1))) {
						lockedPlayer.add(player1);
						player1.sendMessage(prefix + " " + lockMsg);
						if (plugin.getConfig().getBoolean("enable-sounds") == true) {
							player1.playSound(player1.getLocation(),
									Sound.valueOf(plugin.getConfig().getString("sounds.lock.sound")),
									(float) plugin.getConfig().getDouble("sounds.lock.volume"),
									(float) plugin.getConfig().getDouble("sounds.lock.volume"));
						}

					}

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
					if (!(lockedPlayer.contains(player))) {
						lockedPlayer.add(player);
						player.sendMessage(prefix + " " + lockMsg);
						if (plugin.getConfig().getBoolean("enable-sounds") == true) {
							player.playSound(player.getLocation(),
									Sound.valueOf(plugin.getConfig().getString("sounds.lock.sound")),
									(float) plugin.getConfig().getDouble("sounds.lock.volume"),
									(float) plugin.getConfig().getDouble("sounds.lock.volume"));
						}

					}

				}
			}
		} else {
			sender.sendMessage(prefix + ChatColor.RED + " You must be in the game to execute this");
		}
	}

	private void removeFromNear(CommandSender sender, double nearRange) {
		final String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
		final String unlockMsg = ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("unlock-message"));
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Location loc = player.getLocation();
			ArrayList<Entity> targetRange = (ArrayList<Entity>) player.getWorld().getNearbyEntities(loc, nearRange,
					nearRange, nearRange);
			for (Entity entity : targetRange) {
				if (entity instanceof Player) {
					Player player1 = (Player) entity;
					lockedPlayer.remove(player1);
					player1.sendMessage(prefix + " " + unlockMsg);
					if (plugin.getConfig().getBoolean("enable-sounds") == true) {
						player1.playSound(player1.getLocation(),
								Sound.valueOf(plugin.getConfig().getString("sounds.unlock.sound")),
								(float) plugin.getConfig().getDouble("sounds.unlock.volume"),
								(float) plugin.getConfig().getDouble("sounds.unlock.volume"));
					}

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
					player.sendMessage(prefix + " " + unlockMsg);
					if (plugin.getConfig().getBoolean("enable-sounds") == true) {
						player.playSound(player.getLocation(),
								Sound.valueOf(plugin.getConfig().getString("sounds.unlock.sound")),
								(float) plugin.getConfig().getDouble("sounds.unlock.volume"),
								(float) plugin.getConfig().getDouble("sounds.unlock.volume"));
					}

				}
			}
		} else {
			sender.sendMessage(prefix + ChatColor.RED + " You must be in the game to execute this");
		}
	}
}
