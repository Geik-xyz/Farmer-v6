package xyz.geik.ciftci.Listeners.BackEndListeners;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.NPC.listener.MoveListener;

public class onQuitEvent implements Listener 
{

	public Main plugin;
	
	public onQuitEvent(Main plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Quit Event
	 * 
	 * @param e
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerQuitEvent(PlayerQuitEvent e)
	{
		
		leaveEvent(e.getPlayer());
		
	}
	
	@SuppressWarnings("deprecation")
	public static void leaveEvent(Player player)
	{
			
		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
		
			try
			{
				
				File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
				FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
				
				if (data.isSet("players." + player.getName() + ".areas")
						&& !data.getStringList("players." + player.getName() + ".areas").isEmpty()) {
					
					for (String id : data.getStringList("players." + player.getName() + ".areas")) {
						if (data.isSet("data." + id)) {
							
							if (!FarmerManager.farmerCache.containsKey(id))
								continue;
							
							String uuid = id;
							
							if (onEnableShortcut.USE_OWNER)
								uuid = ApiFun.getOwnerViaID(id).getUniqueId().toString();
							
							boolean hasOnline = false;
							
							for (String playerName : Manager.getLore("data", "data." + id))
							{
								if (!playerName.equalsIgnoreCase(player.getName())
										&& Bukkit.getOfflinePlayer(playerName).isOnline())
								{
									hasOnline = true;
									break;
								}
								else continue;
							}
							
							if (!hasOnline
									&& !player.getUniqueId().toString().equalsIgnoreCase(uuid)
									&& Bukkit.getOfflinePlayer(UUID.fromString(uuid)).isOnline())
								hasOnline = true;
							
							if (!hasOnline)
								if (Main.instance.getConfig().getBoolean("Settings.requireOnline"))
									FarmerManager.leaveHandler(id, FarmerManager.farmerCache.get(id));
								else
									DatabaseQueries.autoSaver(id, FarmerManager.farmerCache.get(id));
							
						}
						
						else continue;
						
					}
					
					if (MoveListener.npcCache.containsKey(player.getName()))
						MoveListener.npcCache.remove(player.getName());
					
				}
				
			}
			
			catch (NullPointerException e1) {  }
			
		});
		
	}
	
	
}
