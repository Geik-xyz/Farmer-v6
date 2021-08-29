package xyz.geik.ciftci.Listeners.BackEndListeners.SpawnerEvent;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.wildstacker.api.WildStackerAPI;/*
import com.songoda.epicspawners.api.events.SpawnerSpawnEvent;
*/
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;

@SuppressWarnings("unused")
public class EpicSpawnersListener implements Listener {

	public EpicSpawnersListener(Main instance) {
	}

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
	 * Epic spawner event
	 * 
	 * @param e
	 */
	@EventHandler
	public void epicSpawnerEvent(SpawnerSpawnEvent e) {

		try {

			if (!Main.spawnerKiller)
				return;

			if (!onEnableShortcut.spawnerKillerWithoutFarmer || Main.isShutdowned)
				return;

			Entity en = e.getEntity();

			if (en instanceof Damageable) {

				if (!onEnableShortcut.spawnerKillerWithoutFarmer) {

					String uuid = ApiFun.getIslandOwnerUUID(e.getEntity().getLocation()).toString();

					if (uuid == null)
						return;

					if (!FarmerManager.farmerCache.containsKey(uuid))
						return;

					if (!FarmerManager.farmerCache.get(uuid).getStorage().getSpawnerKill())
						return;

				}

				EntityType entype = e.getEntityType();

				if (!spawnerEvent.allowedMobs.isEmpty() && !spawnerEvent.allowedMobs.contains(entype.toString()))
					return;

				if (!spawnerEvent.blockedMobs.isEmpty() && spawnerEvent.blockedMobs.contains(entype.toString()))
					return;

				if (Main.instance.getConfig().isSet("AddonSettings.spawnerKiller.cookFoods")
						&& Main.instance.getConfig().getBoolean("AddonSettings.spawnerKiller.cookFoods"))
					en.setFireTicks(20);

				if (Bukkit.getPluginManager().getPlugin("WildStacker") != null) {

					if (!entype.equals(EntityType.BLAZE)) {

						List<ItemStack> items = WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity())
								.getDrops(0);
						for (ItemStack item : items)
							e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), item);

					}

					spawnerEvent.killer(en,
							WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity()).getStackAmount());

					WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity()).remove();

					e.getSpawner().getCreatureSpawner().setDelay(-1);

					return;

				}

				((Damageable) en).damage(1000.0);

				if (Main.instance.getConfig().isSet("DetailedSettings.removeMob")) {

					if (Main.instance.getConfig().getBoolean("DetailedSettings.removeMob"))
						en.remove();

				}

				else
					en.remove();

				spawnerEvent.killer(en, 1);
			}

		}

		catch (NullPointerException | NumberFormatException e1) {
		}

	}

}
