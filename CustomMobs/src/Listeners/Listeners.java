package Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.jdweak.customMobs.Main;
import net.md_5.bungee.api.ChatColor;

public class Listeners implements Listener{
	
	JavaPlugin mainClass;
	

	
	
	public Listeners(JavaPlugin plugin) {
		super();
		mainClass = plugin;
	}
	
	
	@EventHandler
	public void morganaAttack(ProjectileHitEvent event) {
		if(event.getEntity().getType() == EntityType.SMALL_FIREBALL && event.getHitEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getHitEntity();
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10));
		}
	}
	
	@EventHandler
	public void rootPlayer(PlayerMoveEvent event) {
		if(event.getPlayer().hasPotionEffect(PotionEffectType.SLOW) && event.getPlayer().getPotionEffect(PotionEffectType.SLOW).getAmplifier() > 9) {// if player has slowness > 9
			if(event.getPlayer().getVelocity().getY() > 0) { //if player jumps
				event.setCancelled(true);
		}
		}
	}
	
	@EventHandler
	public void poisinAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager().getCustomName() != null && event.getDamager().getCustomName().equalsIgnoreCase("poison zombie")) {
			LivingEntity entity = (LivingEntity)event.getEntity();
			int potionDuration = (int) (Math.random() * 20 + 5) * 20;
			entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, potionDuration, 0));
		}
	}
	
	@EventHandler
	public void launchAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager().getCustomName() != null && event.getDamager().getCustomName().equalsIgnoreCase("launcher")) {
			LivingEntity entity = (LivingEntity)event.getEntity();
//			entity.setVelocity(new Vector(entity.getVelocity().getX(), entity.getVelocity().getY() + 100, entity.getVelocity().getZ()));
			entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2, (int) (Math.random() * 40 + 50)));
			
		}
	}
	
	@EventHandler
	public void witherAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager().getType() == EntityType.WITHER_SKELETON && event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity)event.getEntity();
			entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10000000, 1));
		}
	}
	
	@EventHandler
	public void endermanHit(EntityDamageByEntityEvent event) {
		if(event.getEntity().getType() == EntityType.ENDERMAN) {
			LivingEntity enderman = (LivingEntity) event.getEntity();
			enderman.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1));
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent event) {
	   	      if (event.getEntity() instanceof LivingEntity && event.getCause() == DamageCause.PROJECTILE) {
	         Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getJavaPlugin(), ()->{
	            ((LivingEntity) event.getEntity()).setNoDamageTicks(0); // after 100ms you will set the no damage ticks to 0 so arrows can hurt again
	         }, 2L);
	      }
	}
	
	
	@EventHandler
	public void dragonDamaged(EntityDamageEvent event) {
		if(event.getEntityType() == EntityType.ENDER_DRAGON) {
			event.setDamage(event.getDamage()/3);
			if(event.getCause() == DamageCause.BLOCK_EXPLOSION) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void dragonFireEvent(ProjectileLaunchEvent event) {
		if(event.getEntity() instanceof DragonFireball) {
			EnderDragon dragon = event.getEntity().getWorld().getEnderDragonBattle().getEnderDragon();
			BukkitRunnable fireBalls = new DragonFireBallFireTask(mainClass, 10, dragon);
			fireBalls.runTask(mainClass);
		}
	}

	@EventHandler
	public void dragonDeathEvent(EntityDeathEvent event) {
		if(event.getEntityType() == EntityType.ENDER_DRAGON) {
			DragonDeathEventTask dTask = new DragonDeathEventTask(mainClass, (EnderDragon) event.getEntity());
			dTask.runTaskTimer(mainClass, 0, 70);
		}
	}

	@EventHandler
	public void silverFishHit(EntityDamageByEntityEvent event) {
		if(event.getEntityType() == EntityType.SILVERFISH && !event.getEntity().isDead()) {
			for(int i = 0; i < Math.random() * 2 + 1; i ++) {
				event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.SILVERFISH);
			}
		}
	}
	
	@EventHandler
	public void cloudEvent(AreaEffectCloudApplyEvent event) {
		if(event.getEntity().getBasePotionData().getType() == PotionType.UNCRAFTABLE) {
			event.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void endCrystalBreakEvent(EntityDamageEvent event) {
		if(event.getEntityType() == EntityType.ENDER_CRYSTAL) {
			for(int i = 0; i < 12; i ++) {
				Location loc = event.getEntity().getLocation();
				loc.setY(loc.getY() + 10);
				Phantom x = (Phantom) event.getEntity().getWorld().spawnEntity(loc, EntityType.PHANTOM);
				x.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 900000, 8));
				x.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 900000, 3));
				x.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 900000, 1));
				
			}
		}
	}
	
	@EventHandler
	public void playerOnFireEvent(EntityCombustEvent event) {
		if(event.getEntity() instanceof Player) {
			event.setDuration(100);
		}
	}
	
	@EventHandler
	public void durabilityEvent(PlayerItemDamageEvent event) {
		if(event.getDamage() > 8) {
			event.setDamage(8);
		}
	}
	
	@EventHandler
	public void projectileHitEvent(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		int random = (int) (Math.random() * 1 + 4);
		if(projectile.getType() == EntityType.FIREBALL) { // if projectile is fireball create explosion
			if(projectile.getShooter() instanceof Skeleton || projectile.getShooter() instanceof Player) { //if shooter is skeleton or redirected by player make smaller explosion
				return;
			}
			if(event.getHitBlock() != null) { //create explosion at block if block hit
				event.getEntity().getWorld().createExplosion(event.getHitBlock().getLocation(), random);
			} else { //create explosion at entity if entity hit
				Location location = event.getHitEntity().getLocation();
//				Bukkit.broadcastMessage(ChatColor.RED + "original location: " + location.toString());
				Vector vector = event.getEntity().getVelocity();
				vector.multiply(-7);
				location.add(vector);
				event.getEntity().getWorld().createExplosion(location, random);
			}
		}
	}
	
	@EventHandler 
	public void shulkerAttack(EntityPotionEffectEvent event){
		if(event.getCause() != EntityPotionEffectEvent.Cause.PLUGIN) {
			if(event.getModifiedType().toString().equalsIgnoreCase("PotionEffectType[25, LEVITATION]")) {
				LivingEntity entity = (LivingEntity) event.getEntity();
				entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 1200, 1));
			}
		}
	}
	
	@EventHandler
	public void skeletonShoot(EntityShootBowEvent event) {
		if(event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("machine gun")) { //bow named machine gun

				BukkitRunnable fireArrows = new ArrowFireTask(mainClass, 4, event.getEntity());
				fireArrows.runTaskTimer(mainClass, 5, 5);
		} else if(event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("rocket launcher")) {
			event.getEntity().launchProjectile(Fireball.class);
			event.setCancelled(true);
		} else if(event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("liandry's torment")) {
			event.getEntity().launchProjectile(SmallFireball.class);
			event.setCancelled(true);
		}
		}
	
//	@EventHandler
//	public void arrowHit(ProjectileHitEvent event) {
//		if(!(event.getEntity().getShooter() instanceof Player)) {
//			if(event.getEntity() instanceof Arrow) {
//				if(Math.random() * 100 > 50) {
//					event.getEntity().getShooter().launchProjectile(SpectralArrow.class);
//				} else {
//					event.getEntity().getShooter().launchProjectile(Fireball.class);
//				}
//			}
//			
//		}
//	}
	
	
	public void customizeSkeleton(Skeleton x) {
		int random = (int) (Math.random() * 100);
		ItemStack y = new ItemStack(Material.BOW);
		ItemMeta itemMeta = y.getItemMeta();
		if(random > 80) { //machine gun skeleton
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
			helmetMeta.setColor(Color.fromRGB(0, 204, 0));
			helmet.setItemMeta(helmetMeta);
			x.getEquipment().setHelmet(helmet);
			itemMeta.setDisplayName("machine gun");
			y.setItemMeta(itemMeta);
			x.getEquipment().setItemInHand(y);
			x.getEquipment().setItemInHandDropChance(0);
		} else if(random > 60){ //rocket launcher skeleton
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
			helmetMeta.setColor(Color.fromRGB(204, 0, 0));
			helmet.setItemMeta(helmetMeta);
			x.getEquipment().setHelmet(helmet);
			itemMeta.setDisplayName("rocket launcher");
			y.setItemMeta(itemMeta);
			x.getEquipment().setItemInHand(y);
			x.getEquipment().setItemInHandDropChance(0);
		} else if(random > 40) {
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
			helmetMeta.setColor(Color.fromRGB(102, 0, 153));
			helmet.setItemMeta(helmetMeta);
			x.getEquipment().setHelmet(helmet);
			itemMeta.setDisplayName("liandry's torment");
			y.setItemMeta(itemMeta);
			x.getEquipment().setItemInHand(y);
			x.getEquipment().setItemInHandDropChance(0);
			x.setCustomName("Morgana");
		} else { //normal skeleton
			y.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
			y.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
			x.getEquipment().setItemInHand(y);
			x.getEquipment().setItemInHandDropChance(0);
		}
	}
	
	public void customizeZombie(Zombie x) {
		int random = (int) (Math.random() * 100);
		if(random > 50) {
			x.setCustomName("poison zombie");
		} else if(random < 50) {
			x.setCustomName("launcher");
			x.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 2));
		}
	}
	
	public void customizeHusk(Husk x) {
		int random = (int) (Math.random() * 100);
		if(random > 50) {
			x.setCustomName("poison zombie");
			x.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 1));
		} else if(random < 50) {
			x.setCustomName("launcher");
			x.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 2));
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(CreatureSpawnEvent event) {
		if(event.getSpawnReason() != SpawnReason.CUSTOM) {
			
		if(event.getEntityType() == EntityType.CREEPER) {
			Creeper creeper = (Creeper) event.getEntity();
			creeper.setExplosionRadius((int) (Math.random() * 7 + 8));
			creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1));
		} else if(event.getEntityType() == EntityType.SKELETON) {
			customizeSkeleton((Skeleton) event.getEntity());
		} else if(event.getEntityType() == EntityType.ZOMBIE) {
			int random = (int) (Math.random() * 100);
			if(random > 92) {
				event.getEntity().getWorld().spawn(event.getLocation(), ZombieVillager.class);
			}
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			Zombie x = (Zombie) event.getEntity();
			x.getEquipment().setHelmet(helmet);
			customizeZombie(x);
		} else if(event.getEntityType() == EntityType.HUSK) {
			Husk x = (Husk) event.getEntity();
			customizeHusk(x);
		}else if(event.getEntityType() == EntityType.BLAZE) {
			Blaze x = (Blaze) event.getEntity();
			if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
				Blaze y = (Blaze) event.getEntity().getWorld().spawnEntity(x.getLocation(), EntityType.BLAZE);
				x.addPassenger(y);
				for(int i = 0; i < 5; i ++) {
				Blaze z = (Blaze) event.getEntity().getWorld().spawnEntity(x.getLocation(), EntityType.BLAZE);
				y.addPassenger(z);
				y = z;
				}
			}
		} else if(event.getEntityType() == EntityType.SPIDER) {
			event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000, (int) (Math.random() * 3 + 4)));
		} else if(event.getEntityType() == EntityType.WITHER_SKELETON) {
			WitherSkeleton x = (WitherSkeleton) event.getEntity();
			
		} else if(event.getEntityType() == EntityType.PIGLIN) {
			Piglin x = (Piglin) event.getEntity();
			x.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 900000, 2));
		} else if(event.getEntityType() == EntityType.HOGLIN) {
			event.getEntity().setCustomName("launcher");
		} else if(event.getEntityType() == EntityType.ENDERMAN) {
			Enderman x = (Enderman) event.getEntity();
		} else if(event.getEntityType() == EntityType.PHANTOM) {
			if(event.getSpawnReason() != SpawnReason.CUSTOM) {
				Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "A swarm is descending...");
				for(int i = 0; i < Math.random() * 5 + 3; i ++) {
					Location loc = event.getEntity().getLocation();
					event.getEntity().getWorld().spawnEntity(loc, EntityType.PHANTOM);
				}
			}
		} else if(event.getEntityType() == EntityType.BEE) {
			Bee bee = (Bee)event.getEntity();
			bee.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 12));
		} else if(event.getEntityType() == EntityType.PHANTOM) {
			Phantom phantom = (Phantom) event.getEntity();
			phantom.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 50));
		}
		
	
	}
	}
}
