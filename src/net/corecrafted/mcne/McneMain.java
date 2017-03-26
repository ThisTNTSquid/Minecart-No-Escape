package net.corecrafted.mcne;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class McneMain extends JavaPlugin {

	PluginDescriptionFile pdf = this.getDescription();
	Logger logger = this.getLogger();
	String plName = pdf.getName();
	String plVersion = pdf.getVersion();
	ConsoleCommandSender console = this.getServer().getConsoleSender();
	final String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
	final String unlockMsg = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("unlock-message"));
	final String lockMsg = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("lock-message"));

	public void onLoad() {
		logger.info("Loading " + plName + " " + plVersion + "....");
	}

	public void onEnable() {
		logger.info(plName + " " + plVersion + " has been enabled");
		console.sendMessage(prefix + ChatColor.DARK_PURPLE + " Thanks for installing, hope it solves your problem :)");
		this.getCommand("mcne").setExecutor(new McneCommands(this));
		this.regConfig();
		this.getServer().getPluginManager().registerEvents(new McneEvents(), this);
	}

	public void onDisable() {
		logger.info(plName + " " + plVersion + " has been unloaded");
	}

	private void regConfig() {
		try {
			if (!(this.getDataFolder().exists())) {
				this.getDataFolder().mkdirs();
			}

			File config = new File(this.getDataFolder(), "config.yml");
			if (!(config.exists())) {
				logger.info("Config not found, creating one for you...");
				this.saveDefaultConfig();
			} else {
				logger.info("Loading config...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.info("Config loaded");
		}
	}
}