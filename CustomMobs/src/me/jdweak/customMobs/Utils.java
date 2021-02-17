package me.jdweak.customMobs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;


public class Utils {

	public static void multiExplosion(Location location, int power) {
		ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(location);
		
		Location loc2 = location.clone();
		loc2.setY(location.getY() + 17);
		locations.add(loc2);
		Location loc3 = location.clone();
		loc3.setY(location.getY() - 17);
		locations.add(loc3);
		
		Location loc4 = location.clone();
		loc4.setX(location.getX() + 20);
		locations.add(loc4);
		Location loc5 = location.clone();
		loc5.setX(location.getX() - 20);
		locations.add(loc5);
		
		Location loc6 = location.clone();
		loc6.setZ(location.getZ() + 20);
		locations.add(loc6);
		Location loc7 = location.clone();
		loc7.setZ(location.getZ() - 20);
		locations.add(loc7);
		
		for(Location loc : locations) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
		    @Override
		    public void run() {
		    	loc.getWorld().createExplosion(loc, power);
		    }
		}, 20);
	}

	}
}
