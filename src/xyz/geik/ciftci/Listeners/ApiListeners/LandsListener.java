package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.angeschossen.lands.api.events.LandDeleteEvent;
import me.angeschossen.lands.api.events.LandTrustPlayerEvent;
import me.angeschossen.lands.api.events.LandUntrustPlayerEvent;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;

public class LandsListener implements Listener {
	
	public LandsListener(Main instance) {}
	
	@EventHandler
	public void onIslandRemoveEvent(LandDeleteEvent e)
	{
		
		String uuid = e.getLand().getOwnerUID().toString();
		
		if (onEnableShortcut.USE_OWNER)
			uuid = String.valueOf(e.getLand().getId());
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getLand().getOwnerUID()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getLand().getOwnerUID()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}

	@EventHandler
	public void playerKickEventhClaims(LandUntrustPlayerEvent e)
	{
		
		String uuid = e.getLand().getOwnerUID().toString();
		if (onEnableShortcut.USE_OWNER)
			uuid = String.valueOf(e.getLand().getId());
		
		Player playerName = Bukkit.getPlayer(e.getTarget());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler
	public void playerJoinEventhClaims(LandTrustPlayerEvent e)
	{
		
		String uuid = e.getLand().getOwnerUID().toString();
		if (onEnableShortcut.USE_OWNER)
			uuid = String.valueOf(e.getLand().getId());
		
		Player playerName = Bukkit.getPlayer(e.getTarget());
			
		FarmerManager.addMember(uuid, playerName);
		
	}

}
