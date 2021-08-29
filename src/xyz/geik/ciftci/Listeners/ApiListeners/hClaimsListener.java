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

@SuppressWarnings("unused")
public class hClaimsListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;
	public hClaimsListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onIslandRemoveEvent(ClaimDeleteEvent e)
	{
		
		String uuid = Bukkit.getOfflinePlayer(e.getClaim().getOwner()).getUniqueId().toString();
		
		if (onEnableShortcut.USE_OWNER)
			uuid = e.getClaim().getMainChunkId();
		
		if (!onEnableShortcut.USE_OWNER
				&& (!ClaimAPI.getInstance().getClaims(e.getClaim().getOwner()).isEmpty() &&  ClaimAPI.getInstance().getClaims(e.getClaim().getOwner()).size() <= 1))
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
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void expireEvent(ClaimTimeExpireEvent e)
	{
		
		String uuid = Bukkit.getOfflinePlayer(e.getClaim().getOwner()).getUniqueId().toString();
		
		if (onEnableShortcut.USE_OWNER)
			uuid = e.getClaim().getMainChunkId();
		
		if (!onEnableShortcut.USE_OWNER
				&& (!ClaimAPI.getInstance().getClaims(e.getClaim().getOwner()).isEmpty() &&  ClaimAPI.getInstance().getClaims(e.getClaim().getOwner()).size() >= 1))
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
	public void playerKickEventhClaims(ClaimFriendRemoveEvent e)
	{
		
		String uuid = String.valueOf(e.getClaim().getOwner());
		if (onEnableShortcut.USE_OWNER)
			uuid = e.getClaim().getMainChunkId();
		
		Player playerName = Bukkit.getPlayer(e.getClaimFriend().getFriendName());
			
		FarmerManager.removeMember(uuid, playerName);
		
	}
	
	@EventHandler
	public void playerJoinEventhClaims(ClaimFriendAddEvent e)
	{
		
		String uuid = String.valueOf(e.getClaim().getOwner());
		if (onEnableShortcut.USE_OWNER)
			uuid = e.getClaim().getMainChunkId();
		
		Player playerName = Bukkit.getPlayer(e.getClaimFriend().getFriendName());
			
		FarmerManager.addMember(uuid, playerName);
		
	}


}
