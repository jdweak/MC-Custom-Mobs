package Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.Vex;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
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
			entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2, (int) (Math.random() * 20 + 50)));
			
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
	public void cloudEvent(AreaEffectCloudApplyEvent event) {
		Bukkit.broadcastMessage(event.getEntity().getBasePotionData().getType().toString());
		if(event.getEntity().getBasePotionData().getType() == PotionType.UNCRAFTABLE) {
			event.setCancelled(true);
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
		int random = (int) (Math.random() * 5 + 5);
		if(projectile.getType() == EntityType.FIREBALL) { // if projectile is fireball create explosion
			if(projectile.getShooter() instanceof Skeleton || projectile.getShooter() instanceof Player) { //if shooter is skeleton or redirected by player make smaller explosion
				random = (int) (Math.random() * 3 + 1);
			}
			if(event.getHitBlock() != null) { //create explosion at block if block hit
				event.getEntity().getWorld().createExplosion(event.getHitBlock().getLocation(), random);
			} else { //create explosion at entity if entity hit
				Location location = event.getHitEntity().getLocation();
//				Bukkit.broadcastMessage(ChatColor.RED + "original location: " + location.toString());
				Vector vector = event.getEntity().getVelocity();
				vector.multiply(-3);
				location.add(vector);
				event.getEntity().getWorld().createExplosion(location, random);
			}
		}
	}
	
	
	@EventHandler
	public void skeletonShoot(EntityShootBowEvent event) {
		if(event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("machine gun")) { //bow named machine gun

				BukkitRunnable fireArrows = new ArrowFireTask(mainClass, 5, event.getEntity());
				fireArrows.runTaskTimer(mainClass, 8, 8);
			}
		
		if(event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("rocket launcher")) {
			event.getEntity().launchProjectile(Fireball.class);
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
			Zombie x = (Zombie) event.getEntity();
			x.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
			customizeZombie(x);
		}  else if(event.getEntityType() == EntityType.BLAZE) {
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
			PigZombie x = (PigZombie) event.getEntity();
			x.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 900000, 2));
			Zombie y = (Zombie) x.getWorld().spawnEntity(x.getLocation(), EntityType.ZOMBIE);
			y.setBaby(true);
			y.addPassenger(x);
		} else if(event.getEntityType() == EntityType.ENDERMAN) {
			Enderman x = (Enderman) event.getEntity();
		} else if(event.getEntityType() == EntityType.BEE) {
			Bee bee = (Bee)event.getEntity();
			bee.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999999, 12));
		}
		
	
	}
	}
}
