package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandKickEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandQuitEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandTransferEvent;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.API.ApiFun;

public class SuperiorSkyblockListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;
	public SuperiorSkyblockListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onIslandRemoveEvent(IslandDisbandEvent e) 
	{
		
		String uuid = String.valueOf(ApiFun.getIslandOwnerUUIDViaName(Bukkit.getPlayer(e.getPlayer().getName())));
		
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getPlayer().getUniqueId()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}
	
	@EventHandler
	public void islandLeaderChangeEvent(IslandTransferEvent e) 
	{
		
		String oldOwner = e.getOldOwner().getUniqueId().toString();
		
		String newOwner = e.getNewOwner().getUniqueId().toString();
		
		FarmerManager.changeFarmerOwner(oldOwner, newOwner);
		
	}
	
	@EventHandler
	public void superiorIslandKickEvent(IslandKickEvent e)
	{
		
		String uuid = String.valueOf(e.getIsland().getOwner().getUniqueId());
		
		Player playerName = Bukkit.getPlayer(e.getPlayer().getUniqueId());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler
	public void superiorLeaveEvent(IslandQuitEvent e)
	{
		
		String uuid = String.valueOf(e.getIsland().getOwner().getUniqueId());
		
		Player playerName = Bukkit.getPlayer(e.getPlayer().getUniqueId());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler
	public void superiorIslandJoinEvent(IslandJoinEvent e)
	{
		
		String uuid = String.valueOf(e.getIsland().getOwner().getUniqueId());
		
		Player playerName = Bukkit.getPlayer(e.getPlayer().getUniqueId());
		
		FarmerManager.addMember(uuid, playerName);
		
	}

}
