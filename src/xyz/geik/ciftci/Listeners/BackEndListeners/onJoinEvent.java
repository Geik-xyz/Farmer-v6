package xyz.geik.ciftci.Listeners.BackEndListeners;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.NPC.listener.MoveListener;

public class onJoinEvent implements Listener {

	public Main plugin;

	public onJoinEvent(Main plugin) {
		this.plugin = plugin;
	}

	/**
	 * Join Event
	 * 
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoinEvent(PlayerJoinEvent e) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {

			loginEvent(e.getPlayer());

		});

	}

	public static void loginEvent(Player player) {

		try {

			File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
			FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

			List<String> farmers = new ArrayList<String>();

			if (data.isSet("players." + player.getName() + ".areas")
					&& !data.getStringList("players." + player.getName() + ".areas").isEmpty()) {

				for (String id : data.getStringList("players." + player.getName() + ".areas")) {

					if (data.isSet("data." + id)) {

						if (Main.instance.getConfig().getBoolean("Settings.requireOnline")) {

							if (FarmerManager.farmerCache.containsKey(id))
								continue;

							FarmerManager.joinHandler(id, player);

						}

						if (!farmers.contains(id))
							farmers.add(id);

					}

					else
						continue;

				}

				if (farmers != null && !farmers.isEmpty())
					MoveListener.npcCache.put(player.getName(), farmers);

			}

		}

		catch (NullPointerException e1) {
		}

	}

}
