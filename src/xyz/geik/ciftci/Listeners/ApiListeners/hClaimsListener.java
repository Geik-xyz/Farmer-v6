package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hakan.claimsystem.api.ClaimAPI;
import com.hakan.claimsystem.api.customevents.ClaimDeleteEvent;
import com.hakan.claimsystem.api.customevents.ClaimFriendAddEvent;
import com.hakan.claimsystem.api.customevents.ClaimFriendRemoveEvent;
import com.hakan.claimsystem.api.customevents.ClaimTimeExpireEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;

public class hClaimsListener implements Listener {
	
	public hClaimsListener(Main plugin) {}
	
	@EventHandler
	public void onIslandRemoveEvent(ClaimDeleteEvent e)
	{
		
		String uuid = e.getClaim().getOwnerUUID().toString();
		
		if (onEnableShortcut.USE_OWNER)
			uuid = e.getClaim().getMainChunkData().toId();
		
		if (!onEnableShortcut.USE_OWNER
				&& (!ClaimAPI.getInstance().getClaims(e.getClaim().getOwnerUUID()).isEmpty() &&  ClaimAPI.getInstance().getClaims(e.getClaim().getOwnerUUID()).size() <= 1))
			return;
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getClaim().getOwnerUUID()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getClaim().getOwnerUUID()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}
	
	@EventHandler
	public void expireEvent(ClaimTimeExpireEvent e)
	{
		
		String uuid = Bukkit.getOfflinePlayer(e.getClaim().getOwnerUUID()).getUniqueId().toString();
		
		if (onEnableShortcut.USE_OWNER)
			uuid = e.getClaim().getMainChunkData().toId();
		
		if (!onEnableShortcut.USE_OWNER
				&& (!ClaimAPI.getInstance().getClaims(e.getClaim().getOwnerUUID()).isEmpty() &&  ClaimAPI.getInstance().getClaims(e.getClaim().getOwnerUUID()).size() >= 1))
			return;
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getClaim().getOwnerUUID()).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getClaim().getOwnerUUID()), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}

	@EventHandler
	public void playerKickEventhClaims(ClaimFriendRemoveEvent e)
	{
		
		String uuid = String.valueOf(e.getClaim().getOwnerUUID());
		if (onEnableShortcut.USE_OWNER)
			uuid = e.getClaim().getMainChunkData().toId();
		
		Player playerName = Bukkit.getPlayer(e.getClaimFriend().getUUID());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler
	public void playerJoinEventhClaims(ClaimFriendAddEvent e)
	{
		
		String uuid = String.valueOf(e.getClaim().getOwnerUUID());
		if (onEnableShortcut.USE_OWNER)
			uuid = e.getClaim().getMainChunkData().toId();
		
		Player playerName = Bukkit.getPlayer(e.getClaimFriend().getUUID());
			
		FarmerManager.addMember(uuid, playerName);
		
	}


}
