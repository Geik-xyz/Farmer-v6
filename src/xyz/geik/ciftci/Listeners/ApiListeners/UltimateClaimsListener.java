package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.songoda.ultimateclaims.api.events.ClaimDeleteEvent;
import com.songoda.ultimateclaims.api.events.ClaimMemberAddEvent;
import com.songoda.ultimateclaims.api.events.ClaimMemberLeaveEvent;
import com.songoda.ultimateclaims.api.events.ClaimPlayerKickEvent;
import com.songoda.ultimateclaims.api.events.ClaimTransferOwnershipEvent;
import com.songoda.ultimateclaims.api.events.ClaimChunkUnclaimEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;

public class UltimateClaimsListener implements Listener {
	
	public Main plugin;
	public UltimateClaimsListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void ultimateClaimsRemove(ClaimDeleteEvent e)
	{
		
		String uuid = e.getClaim().getOwner().getPlayer().getUniqueId().toString();
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getClaim().getOwner().getUniqueId()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getClaim().getOwner().getUniqueId()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void ultimateClaimsUnclaim(ClaimChunkUnclaimEvent e)
	{
		String uuid = e.getClaim().getOwner().getPlayer().getUniqueId().toString();
		
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getClaim().getOwner().getUniqueId()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getClaim().getOwner().getUniqueId()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
	
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void transferOwnership(ClaimTransferOwnershipEvent e) {
		FarmerManager.changeFarmerOwner(e.getOldOwner().toString(), e.getNewOwner().toString());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void ultimateLeaveEvent(ClaimMemberLeaveEvent e)
	{

		String uuid = String.valueOf(e.getClaim().getOwner().getUniqueId());
		
		Player playerName = e.getPlayer().getPlayer();
			
		FarmerManager.removeMember(uuid, playerName);
			
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void ultimateJoinEvent(ClaimMemberAddEvent e)
	{
		
		String uuid = String.valueOf(e.getClaim().getOwner().getUniqueId());
		
		Player playerName = e.getPlayer().getPlayer();
		
		FarmerManager.addMember(uuid, playerName);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void ultimatePlayerKick(ClaimPlayerKickEvent e)
	{
		
		String uuid = String.valueOf(e.getClaim().getOwner().getUniqueId());
		
		Player playerName = e.getPlayer().getPlayer();
			
		FarmerManager.removeMember(uuid, playerName);
			
	}
	
}
