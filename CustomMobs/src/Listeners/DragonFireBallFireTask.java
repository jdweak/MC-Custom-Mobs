package Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DragonFireBallFireTask extends BukkitRunnable{
	
	private final JavaPlugin plugin;
	
	Player target;
	
	int arrowAmount;
	LivingEntity shooter;
	
	public DragonFireBallFireTask(JavaPlugin plugin, int arrowAmount, LivingEntity shooter) {
		this.plugin = plugin;
		this.arrowAmount = arrowAmount;
		this.shooter = shooter;
	}
	
	

	@Override
	public void run() {
		if(arrowAmount == 0) {
			return;
		} else {
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(player.getWorld().getName().endsWith("_end")){//launch fireball at all players in the end
//					shooter.launchProjectile(Fireball.class, player.getLocation().toVector().subtract(shooter.getLocation().toVector()));
					Location loc = shooter.getLocation();
					loc.setY(loc.getY() - 5);
					shooter.getWorld().spawn(loc, Fireball.class).setDirection(player.getLocation().toVector().subtract(loc.toVector()));
					
					arrowAmount --;
				}
			}
		}
	}
}