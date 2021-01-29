package creeperBombEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.jdweak.customMobs.Main;

public class CreeperBombManager implements Listener{
	
	public static Creeper creeperBomb;
	static CreeperBombTaskScheduler taskScheduler = new CreeperBombTaskScheduler(Main.getJavaPlugin());
	
	public static void createCreeperBomb(Creeper creeper) {
		if(creeperBomb == null) {
			creeperBomb = creeper;
			creeperBomb.setPowered(true);
			creeperBomb.setExplosionRadius((int) (Math.random() * 40 + 30));
			Bukkit.getServer().broadcastMessage(ChatColor.RED + "WARNING: A NUCLEAR CREEPER IS IN THE VICINITY AND WILL DETONATE IN " + 100 + " SECONDS. TO PREVENT THIS FIND THE CREEPER USING A MINESWEEPER AND ELIMINATE IT");
			taskScheduler.setCreeper(creeperBomb);
			taskScheduler.runTaskLaterAsynchronously(Main.getJavaPlugin(), 100);
//			taskScheduler.runTaskLater(Main.getJavaPlugin(), 100);
		}
	}
	
	public static Location getCreeperBombLocation() {
		return creeperBomb.getLocation();
	}
	
	public static void resetCreeperBomb() {
		creeperBomb = null;
	}
	
	@EventHandler
	public void entityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity() == creeperBomb) {
			taskScheduler.cancel();
			CreeperBombManager.resetCreeperBomb();
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "nuke sucessfully defused");
		}
	}
	
	@EventHandler
	public void defuseFail(EntityExplodeEvent event) {
		if(event.getEntity() == creeperBomb) {
			Bukkit.broadcastMessage(ChatColor.YELLOW + "your hamfisted methods ended up detonating the nuke... well done");
			CreeperBombManager.resetCreeperBomb();
		}
	}
}
