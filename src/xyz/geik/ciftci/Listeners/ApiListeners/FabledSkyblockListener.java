package xyz.geik.ciftci.Listeners.ApiListeners;

import java.io.File;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.songoda.skyblock.api.event.island.IslandDeleteEvent;
import com.songoda.skyblock.api.event.island.IslandKickEvent;
import com.songoda.skyblock.api.event.island.IslandOwnershipTransferEvent;
import com.songoda.skyblock.api.event.player.PlayerIslandJoinEvent;
import com.songoda.skyblock.api.event.player.PlayerIslandLeaveEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;

@SuppressWarnings("unused")
public class FabledSkyblockListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;
	public FabledSkyblockListener(Main plugin) {
		this.plugin = plugin;
	}
	
	public static HashMap<String, String> awaitForLeader = new HashMap<String, String>();
	
	@EventHandler
	public void onIslandRemoveEvent(IslandDeleteEvent e)
	{
		
		String uuid = String.valueOf(e.getIsland().getOwnerUUID());
		
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getIsland().getOwnerUUID()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getIsland().getOwnerUUID()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}
	
	@EventHandler
	public void islandLeaderChangeEvent(IslandOwnershipTransferEvent e)
	{
		
		try {
			
			if (!onEnableShortcut.USE_OWNER)
				return;
			
			String newOwner = e.getOwner().getUniqueId().toString();
			
			File dataFile = new File("plugins/" + Main.instance.getDescription().getName() + "/data.yml");
			FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
			
			if (!data.contains("players." + e.getOwner().getName() + ".areas"))
				return;
			
			String oldOwner = null;
			
			for (String key : data.getStringList("players." + e.getOwner().getName() + ".areas"))
			{
				
				for (OfflinePlayer members : ApiFun.getLandPlayers(e.getOwner(), null))
					if (members.getUniqueId().toString().equals(key))
					{
						
						oldOwner = key;
						break;
						
					}
				
					else continue;
				
			}
			
			if (oldOwner == null)
				return;
			
			FarmerManager.changeFarmerOwner(oldOwner, newOwner);
			
		}
		
		catch (NullPointerException | IndexOutOfBoundsException e1) {}
		
	}
	
	@EventHandler
	public void fabledKickEvent(IslandKickEvent e)
	{
		
		String uuid = String.valueOf(e.getIsland().getOwnerUUID());
		
		Player playerName = e.getKicked().getPlayer();
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler
	public void fabledLeaveEvent(PlayerIslandLeaveEvent e)
	{
		
		String uuid = String.valueOf(e.getIsland().getOwnerUUID());
		
		Player playerName = e.getPlayer();
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler
	public void fabledJoinEvent(PlayerIslandJoinEvent e)
	{
		
		String uuid = String.valueOf(e.getIsland().getOwnerUUID());
		
		Player playerName = e.getPlayer();
			
		FarmerManager.addMember(uuid, playerName);
		
	}

}
