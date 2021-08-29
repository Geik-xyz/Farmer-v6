package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.island.IslandResetEvent;
import world.bentobox.bentobox.api.events.team.TeamJoinEvent;
import world.bentobox.bentobox.api.events.team.TeamKickEvent;
import world.bentobox.bentobox.api.events.team.TeamLeaveEvent;
import world.bentobox.bentobox.api.events.team.TeamSetownerEvent;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;

public class BentoBoxListeners implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;
	public BentoBoxListeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onIslandDeleteEvent(IslandDeleteEvent e)
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
	public void onIslandResetEvent(IslandResetEvent e)
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
	public void islandLeaderChangeEvent(TeamSetownerEvent e)
	{
		
		String uuid = String.valueOf(e.getOldOwner());
		
		String newOwner = e.getNewOwner().toString();
		
		FarmerManager.changeFarmerOwner(uuid, newOwner);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void bentoTeamLeaveEvent(TeamLeaveEvent e)
	{
		
		String uuid = String.valueOf(e.getOwner());
		
		Player playerName = Bukkit.getPlayer(e.getPlayerUUID());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void bentoTeamLeaveEvent(TeamKickEvent e)
	{
		
		String uuid = String.valueOf(e.getOwner());
		
		Player playerName = Bukkit.getPlayer(e.getPlayerUUID());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void bentoTeamJoinEvent(TeamJoinEvent e)
	{
		
		String uuid = String.valueOf(e.getOwner());
		
		Player playerName = Bukkit.getPlayer(e.getPlayerUUID());
		
		FarmerManager.addMember(uuid, playerName);
		
	}

}
