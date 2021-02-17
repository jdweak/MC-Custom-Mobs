package Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.jdweak.customMobs.Utils;

public class DragonDeathEventTask extends BukkitRunnable{
	
	private final JavaPlugin plugin;
	EnderDragon dragon;
	int counter = 4;

	public DragonDeathEventTask(JavaPlugin plugin, EnderDragon dragon) {
		this.plugin = plugin;
		this.dragon = dragon;
	}
	
	@Override
	public void run(){
		
		
		switch(counter) {
			case 4:
				Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "You may have killed me, but don't celebrate yet...\n");
				break;
			case 3:
				Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "The only ones who should kill are those who are prepared to be killed.\n");
				break;
			case 2:
				Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "So now I am become death, the destroyer of worlds\n");
				break;
			case 1:
				Bukkit.broadcastMessage(ChatColor.RED + "OMAE WA MOU SHINDEIRU");
				break;
			case 0:
				this.cancel();
				Location location = dragon.getDragonBattle().getEndPortalLocation();
				location.setY(location.getY() + 6);
				Utils.multiExplosion(location, 180);
				location.setY(location.getY() - 20);
				Utils.multiExplosion(location, 180);
				return;
			default:
				return;
		}
		counter --;
		
		
		
		
		
//		try {
//		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "You may have killed me, but don't celebrate yet...\n");
//		Thread.sleep(2000);
//		
//		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "The only ones who should kill are those who are prepared to be killed.\n");
//		Thread.sleep(2000);
//		
//		Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "So now I am become death, the destroyer of worlds\n");
//		Thread.sleep(2000);
//		
//		Bukkit.broadcastMessage(ChatColor.RED + "OMAE WA MOU SHINDEIRU");
//		Thread.sleep(3000);
//		
//		Utils.multiExplosion(dragon.getDragonBattle().getEndPortalLocation(), 150);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}

}
