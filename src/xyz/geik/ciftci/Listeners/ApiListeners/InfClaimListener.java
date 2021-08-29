package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import tr.com.infumia.plugin.infclaim.InfClaimAPI;
import tr.com.infumia.plugin.infclaim.api.event.ClaimDeleteEvent;
import tr.com.infumia.plugin.infclaim.api.event.ClaimTeamJoinEvent;
import tr.com.infumia.plugin.infclaim.api.event.ClaimTeamKickEvent;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;

@SuppressWarnings("unused")
public class InfClaimListener implements Listener {

	public InfClaimListener(Main plugin) {}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onIslandRemoveEvent(ClaimDeleteEvent e)
	{
		
		String uuid = Bukkit.getOfflinePlayer(e.getClaim().getOwner()).getUniqueId().toString();
		
		if (onEnableShortcut.USE_OWNER)
			uuid = String.valueOf(e.getClaim().getId());
		
		if (!onEnableShortcut.USE_OWNER
				&& (!InfClaimAPI.getClaimsByOwner(e.getClaim().getOwner()).isEmpty() &&  InfClaimAPI.getClaimsByOwner(e.getClaim().getOwner()).size() >= 1))
			return;
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getClaim().getOwner()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getClaim().getOwner()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}

	@EventHandler
	public void playerKickEventhClaims(ClaimTeamKickEvent e)
	{
		
		@SuppressWarnings("deprecation")
		String uuid = Bukkit.getOfflinePlayer(e.getClaim().getOwner()).getUniqueId().toString();
		if (onEnableShortcut.USE_OWNER)
			uuid = String.valueOf(e.getClaim().getId());
		
		Player playerName = Bukkit.getPlayer(e.getClaimMember().getMemberName());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler
	public void playerJoinEventhClaims(ClaimTeamJoinEvent e)
	{
		
		@SuppressWarnings("deprecation")
		String uuid = Bukkit.getOfflinePlayer(e.getClaim().getOwner()).getUniqueId().toString();
		if (onEnableShortcut.USE_OWNER)
			uuid = String.valueOf(e.getClaim().getId());
		
		Player playerName = Bukkit.getPlayer(e.getClaimMember().getMemberName());
			
		FarmerManager.addMember(uuid, playerName);
		
	}

}
