package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.wasteofplastic.askyblock.events.IslandChangeOwnerEvent;
import com.wasteofplastic.askyblock.events.IslandDeleteEvent;
import com.wasteofplastic.askyblock.events.IslandResetEvent;
import com.wasteofplastic.askyblock.events.TeamJoinEvent;
import com.wasteofplastic.askyblock.events.TeamLeaveEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;

public class AskyblockListener implements Listener
{
	
	@SuppressWarnings("unused")
	private Main plugin;
	public AskyblockListener(Main plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onIslandRemoveEvent(IslandDeleteEvent e)
	{
		
		String uuid = e.getPlayerUUID().toString();
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getPlayerUUID()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getPlayerUUID()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
			
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onIslandRestartEvent(IslandResetEvent e)
	{
		
		String uuid = e.getPlayer().getUniqueId().toString();
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (e.getPlayer().isOnline())
					FarmerManager.giveEggToPlayer(e.getPlayer(), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void islandLeaderChangeEvent(IslandChangeOwnerEvent e)
	{
		FarmerManager.changeFarmerOwner(e.getOldOwner().toString(), e.getNewOwner().toString());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void playerKickEventAsky(TeamLeaveEvent e)
	{
		
		String uuid = String.valueOf(e.getOldTeamLeader());
		
		Player playerName = Bukkit.getPlayer(e.getPlayer());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void playerJoinEventAsky(TeamJoinEvent e)
	{
		
		String uuid = String.valueOf(e.getNewTeamLeader());
		
		Player playerName = Bukkit.getPlayer(e.getPlayer());
			
		FarmerManager.addMember(uuid, playerName);
		
	}

}
