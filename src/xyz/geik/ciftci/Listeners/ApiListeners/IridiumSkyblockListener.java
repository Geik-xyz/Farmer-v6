package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.IslandDeleteEvent;
import com.iridium.iridiumskyblock.api.IslandRegenEvent;
import com.iridium.iridiumskyblock.api.UserKickEvent;
import com.iridium.iridiumskyblock.api.UserLeaveEvent;
import com.iridium.iridiumskyblock.api.UserPromoteEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;

public class IridiumSkyblockListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;
	public IridiumSkyblockListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onIslandRemoveEvent(IslandDeleteEvent e)
	{
		
		String uuid = e.getIsland().getOwner().getUuid().toString();
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getUser().getUuid()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getUser().getUuid()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
			
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onIslandRestartEvent(IslandRegenEvent e)
	{
		
		String uuid = e.getIsland().getOwner().getUuid().toString();
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getUser().getUuid()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getUser().getUuid()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void islandLeaderChangeEvent(UserPromoteEvent e)
	{
		if (e.getNewRank().equals(IslandRank.OWNER))
			FarmerManager.changeFarmerOwner(e.getIsland().getOwner().getUuid().toString(), e.getUser().getUuid().toString());
		
		else if (e.getNewRank().equals(IslandRank.MEMBER)) {
			String uuid = String.valueOf(e.getIsland().getOwner().getUuid().toString());
			Player playerName = Bukkit.getPlayer(e.getUser().getName());
			FarmerManager.addMember(uuid, playerName);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void playerKickEventAsky(UserLeaveEvent e)
	{
		
		//String uuid = String.valueOf(e.);
		
	//	Player playerName = Bukkit.getPlayer(e.getPlayer());
			
	//	FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void playerKickEventAsky(UserKickEvent e)
	{
		
		String uuid = String.valueOf(e.getIsland().getOwner().getUuid().toString());
		
		Player playerName = Bukkit.getPlayer(e.getUser().getUuid().toString());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}

}
