package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;

public class GriefPreventionListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;
	public GriefPreventionListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void claimRemoveEvent(ClaimDeletedEvent e)
	{
		try {
			
			String owner = Bukkit.getOfflinePlayer(e.getClaim().getOwnerName()).getUniqueId().toString();
			
			String uuid = owner;
			
			if (onEnableShortcut.USE_OWNER)
				uuid = e.getClaim().getID().toString();
			
			if (!onEnableShortcut.USE_OWNER
					&& GriefPrevention.instance.dataStore.getPlayerData(Bukkit.getPlayer(e.getClaim().getOwnerName()).getUniqueId()).getClaims().size() > 0)
				return;
				
			if (DatabaseQueries.areaHasFarmer(uuid))
			{
				
				if (FarmerManager.farmerCache.containsKey(uuid))
				{
					
					if (Bukkit.getPlayer(e.getClaim().getOwnerName()).isOnline())
						FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getClaim().getOwnerName()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
					
					FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
					
				}
				
				DatabaseQueries.removeFarmer(uuid);
				
				FarmerManager.removeFarmerDataYML(uuid);
				
			}
			
		}
		catch (NullPointerException e1) {}
		
	}

}
