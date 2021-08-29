package xyz.geik.ciftci.Listeners.BackEndListeners.SpawnerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;

public class spawnerEvent implements Listener {

	public spawnerEvent(Main instance) {
	}

	public static List<String> allowedMobs = new ArrayList<String>();

	public static List<String> blockedMobs = new ArrayList<String>();

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCreatureEvent(CreatureSpawnEvent e) {

		if (!Main.spawnerKiller)
			return;

		Entity en = e.getEntity();

		if (en instanceof Damageable) {

			if (e.getSpawnReason().equals(SpawnReason.MOUNT) || e.getSpawnReason().equals(SpawnReason.JOCKEY))
				e.setCancelled(true);

		}

	}

	/**
	 * Spawner event
	 * 
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSpawnerSpawnEvent(SpawnerSpawnEvent e) {

		try {

			if (!Main.spawnerKiller)
				return;

			if (!onEnableShortcut.spawnerKillerWithoutFarmer && Main.isShutdowned)
				return;

			Entity en = e.getEntity();

			if (en instanceof Damageable) {

				if (!onEnableShortcut.spawnerKillerWithoutFarmer) {

					String uuid = ApiFun.getIslandOwnerUUID(e.getLocation()).toString();

					if (uuid == null)
						return;

					if (!FarmerManager.farmerCache.containsKey(uuid))
						return;

					if (!FarmerManager.farmerCache.get(uuid).getStorage().getSpawnerKill())
						return;

				}

				EntityType entype = e.getEntityType();

				if (!allowedMobs.isEmpty() && !allowedMobs.contains(entype.toString()))
					return;

				if (!blockedMobs.isEmpty() && blockedMobs.contains(entype.toString()))
					return;

				if (Main.instance.getConfig().isSet("AddonSettings.spawnerKiller.cookFoods")
						&& Main.instance.getConfig().getBoolean("AddonSettings.spawnerKiller.cookFoods"))
					en.setFireTicks(20);

				if (Bukkit.getPluginManager().getPlugin("WildStacker") != null) {

					if (!entype.equals(EntityType.BLAZE)) {

						List<ItemStack> items = WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity())
								.getDrops(0);
						for (ItemStack item : items)
							e.getEntity().getWorld().dropItemNaturally(e.getLocation(), item);

					}

					killer(en, WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity()).getStackAmount());

					WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity()).remove();

					e.getSpawner().setDelay(-1);

					return;

				}

				((Damageable) en).damage(1000.0);

				if (Main.instance.getConfig().isSet("DetailedSettings.removeMob")) {

					if (Main.instance.getConfig().getBoolean("DetailedSettings.removeMob"))
						en.remove();

				}

				else
					en.remove();

				killer(en, 1);

			}

		}

		catch (NullPointerException | NumberFormatException e1) {
		}

	}

	@SuppressWarnings("deprecation")
	public static void killer(Entity en, int amount) {
		EntityType entype = en.getType();

		if (en.getPassenger() != null) {

			((Damageable) en.getPassenger()).damage(1000.0);

			en.getPassenger().remove();

		}

		else if (entype == EntityType.OCELOT || entype == EntityType.CHICKEN || entype == EntityType.COW
				|| entype == EntityType.HORSE || entype == EntityType.MUSHROOM_COW || entype == EntityType.PIG
				|| entype == EntityType.RABBIT || entype == EntityType.SHEEP || entype == EntityType.SQUID
				|| entype == EntityType.WOLF || entype == EntityType.BAT || entype.toString().contains("DONKEY")
				|| entype.toString().contains("MULE") || entype.toString().contains("LLAMA")
				|| entype.toString().contains("PARROT") || entype.toString().contains("POLAR_BEAR")
				|| entype.toString().contains("BEE") || entype.toString().contains("STRIDER")
				|| entype.toString().contains("COD") || entype.toString().contains("FOX")
				|| entype.toString().contains("PIGLIN") || entype.toString().contains("SALMON")
				|| entype.toString().contains("TROPICALFISH") || entype.toString().contains("SKELETONHORSE")
				|| entype.toString().contains("TURTLE") || entype.toString().contains("PANDA")
				|| entype.toString().contains("DOLPHIN")) {

			int randomNum = 1 + (int) (Math.random() * 3.0);

			((ExperienceOrb) en.getWorld().spawn(en.getLocation(), ExperienceOrb.class))
					.setExperience(randomNum * amount);

		}

		else if (entype == EntityType.GUARDIAN || entype == EntityType.BLAZE) {

			((ExperienceOrb) en.getWorld().spawn(en.getLocation(), ExperienceOrb.class)).setExperience(10 * amount);

			for (int i = 0; i < amount; i++)
				if (entype == EntityType.BLAZE && ((new Random()).nextInt(100)) <= 50)
					en.getWorld().dropItemNaturally(en.getLocation(), new ItemStack(Material.BLAZE_ROD, 1));

		}

		else if (entype == EntityType.IRON_GOLEM || entype == EntityType.SNOWMAN || entype.equals(EntityType.VILLAGER)
				|| entype.toString().contains("WANDERINGTRADER"))
			return;

		else if (entype == EntityType.SLIME || entype == EntityType.MAGMA_CUBE) {
			((ExperienceOrb) en.getWorld().spawn(en.getLocation(), ExperienceOrb.class)).setExperience(3 * amount);
		}

		else if (entype.toString().contains("PHANTOM")) {

			for (int i = 0; i < amount; i++)
				if (((new Random()).nextInt(100)) <= 50)
					en.getWorld().dropItemNaturally(en.getLocation(), new ItemStack(Material.PHANTOM_MEMBRANE, 1));

			((ExperienceOrb) en.getWorld().spawn(en.getLocation(), ExperienceOrb.class)).setExperience(5 * amount);

		}

		else {

			((ExperienceOrb) en.getWorld().spawn(en.getLocation(), ExperienceOrb.class)).setExperience(5 * amount);

		}
	}

}
