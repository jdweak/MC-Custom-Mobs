package me.jdweak.customMobs;

import org.bukkit.plugin.java.JavaPlugin;
import Listeners.Listeners;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
	private static Main instance;
	
	public static Main getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nCusom Mobs enabled with updates\n\n");
	}
	
	public static JavaPlugin getJavaPlugin() {
		return instance;
	}
	
	
}