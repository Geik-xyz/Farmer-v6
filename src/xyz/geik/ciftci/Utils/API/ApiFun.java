package xyz.geik.ciftci.Utils.API;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.eclipse.jdt.annotation.Nullable;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.hakan.claimsystem.api.ClaimAPI;
import com.hakan.claimsystem.claim.HClaim;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.User;
import com.songoda.skyblock.api.SkyBlockAPI;
import com.songoda.skyblock.api.island.Island;
import com.songoda.skyblock.api.island.IslandRole;
import com.songoda.skyblock.playerdata.PlayerData;
import com.songoda.ultimateclaims.UltimateClaims;
import com.songoda.ultimateclaims.member.ClaimMember;
import com.wasteofplastic.askyblock.ASkyBlockAPI;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Messages;
import tr.com.infumia.plugin.infclaim.InfClaimAPI;
import tr.com.infumia.plugin.infclaim.api.IClaim;
import tr.com.infumia.plugin.infclaim.api.IClaimMember;
import tr.com.infumia.plugin.infclaim.claim.BasicClaim;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;

public class ApiFun {
	
	public static OfflinePlayer getOwnerViaID(String id) {
		
		if (onEnableShortcut.USE_OWNER) {
			
			if (Main.API.equals(ApiType.FabledSkyblock))
				return Bukkit.getOfflinePlayer(SkyBlockAPI.getIslandManager().getIslandByUUID(UUID.fromString(id)).getOwnerUUID());
			
			else if (Main.API.equals(ApiType.GriefPrevention)) 
				return Bukkit.getOfflinePlayer(GriefPrevention.instance.dataStore.getClaim(Long.valueOf(id)).ownerID);
			
			else if (Main.API.equals(ApiType.hClaims)) {
				for (HClaim claim : ClaimAPI.getInstance().getClaimList()) {
					if (id.equals(claim.getMainChunkData().toId()))
						return Bukkit.getOfflinePlayer(claim.getOwnerUUID());
					else continue;
				}
				return null;
			}
			
			else if (Main.API.equals(ApiType.InfClaim))
				return Bukkit.getOfflinePlayer(BasicClaim.getClaimByID(Integer.valueOf(id)).getOwnerUUID());
			
			else if (Main.API.equals(ApiType.Lands))
				return Bukkit.getOfflinePlayer(onEnableShortcut.lands.getLand(id).getOwnerUID());
			
			else return null;
			
		}
		
		else return null;
		
	}
	
	@SuppressWarnings("deprecation")
	public static String getIslandOwnerUUID(Location location)
	{
		
		try
		{
		
			if (onEnableShortcut.USE_OWNER) {
				
				if (Main.API.equals(ApiType.FabledSkyblock))
					return SkyBlockAPI.getIslandManager().getIslandAtLocation(location).getIslandUUID().toString();
				
				else if (Main.API.equals(ApiType.GriefPrevention)) 
					return GriefPrevention.instance.dataStore.getClaimAt(location, true, null).getID().toString();
				
				else if (Main.API.equals(ApiType.hClaims))
					return ClaimAPI.getInstance().getClaim(location).getMainChunkData().toId();
				
				else if (Main.API.equals(ApiType.InfClaim))
					return String.valueOf(InfClaimAPI.getClaimFromLocation(location).getId());
				
				else if (Main.API.equals(ApiType.Lands))
					return String.valueOf(onEnableShortcut.lands.getLand(location).getId());
				
			}
			
			if (Main.API.equals(ApiType.ASkyBlock)) return ASkyBlockAPI.getInstance().getIslandAt(location).getOwner().toString();
			
			else if (Main.API.equals(ApiType.FabledSkyblock)) return SkyBlockAPI.getIslandManager().getIslandAtLocation(location).getOwnerUUID().toString();
			
			else if (Main.API.equals(ApiType.GriefPrevention)) return GriefPrevention.instance.dataStore.getClaimAt(location, true, null).ownerID.toString();
			
			else if (Main.API.equals(ApiType.SuperiorSkyblock)) return SuperiorSkyblockAPI.getIslandAt(location).getOwner().getUniqueId().toString();
			
		    else if (Main.API.equals(ApiType.IridiumSkyblock)) return IridiumSkyblockAPI.getInstance().getIslandViaLocation(location).get().getOwner().getUuid().toString();
			
			else if (Main.API.equals(ApiType.BentoBox)) {
				if (BentoBox.getInstance().getIslands().getIslandAt(location) != null
						&& BentoBox.getInstance().getIslands().getIslandAt(location).isPresent())
					return BentoBox.getInstance().getIslands().getIslandAt(location).get().getOwner().toString();
				else return null;
			}
			
			else if (Main.API.equals(ApiType.PlotSquared))try {return PlotAPI.class.newInstance().getPlot(location).guessOwner().toString();}
			catch (InstantiationException | IllegalAccessException e) {return null;}
			
			else if (Main.API.equals(ApiType.UltimateClaims)) return UltimateClaims.getInstance().getClaimManager().getClaim(location.getChunk()).getOwner().getUniqueId().toString();
			
		    else if (Main.API.equals(ApiType.hClaims)) return ClaimAPI.getInstance().getClaim(location).getOwnerUUID().toString();
			
			else if (Main.API.equals(ApiType.InfClaim))
				return Bukkit.getOfflinePlayer(InfClaimAPI.getClaimFromLocation(location).getOwner()).getUniqueId().toString();
			
			else if (Main.API.equals(ApiType.Lands))
				return onEnableShortcut.lands.getLand(location).getOwnerUID().toString();
			
			else return null;
			
		} catch (NullPointerException e1) {return null;}
		
	}
	
	@SuppressWarnings("deprecation")
	public static UUID getIslandOwnerUUIDViaName(Player player)
	{
		try
		{
			
			if (Main.API.equals(ApiType.ASkyBlock))
				return ASkyBlockAPI.getInstance().getIslandAt(ASkyBlockAPI.getInstance().getIslandLocation(player.getUniqueId())).getOwner();
			
			else if (Main.API.equals(ApiType.FabledSkyblock))
			{
				
				Island island = null;
				
				if (SkyBlockAPI.getImplementation().getPlayerDataManager().hasPlayerData(player))
				{
					
					PlayerData playerData = SkyBlockAPI.getImplementation().getPlayerDataManager().getPlayerData(player);
					
					if (playerData.getOwner() != null)
					{
						
						island = SkyBlockAPI.getIslandManager().getIsland(player);
						
					}
					
				}
				
				if (island != null)
					return SkyBlockAPI.getIslandManager().getIsland(player).getOwnerUUID();
				
				else return player.getUniqueId();
				
			}
			
			else if (Main.API.equals(ApiType.GriefPrevention))
				return GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).getClaims().get(0).ownerID;
			
			
			else if (Main.API.equals(ApiType.SuperiorSkyblock)) return SuperiorSkyblockAPI.getPlayer(player).getIslandLeader().getUniqueId();
			
			
			else if (Main.API.equals(ApiType.IridiumSkyblock)) return IridiumSkyblockAPI.getInstance().getUser(player).getIsland().get().getOwner().getUuid();
			
			else if (Main.API.equals(ApiType.BentoBox))
			{
				
				BentoBox plugin = BentoBox.getInstance();
				
				world.bentobox.bentobox.api.user.User user = plugin.getPlayers().getUser(player.getUniqueId());
				
		        GameModeAddon gameModeAddon = plugin.getAddonsManager().getGameModeAddons().get(0);
		        
		        if(gameModeAddon != null)
		        {
		        	
		        	world.bentobox.bentobox.database.objects.@Nullable Island island = plugin.getIslands().getIsland(gameModeAddon.getOverWorld(), user);
		        	
			        if (island != null)
			            return island.getOwner();
			        
			        else return null;
			        
		        }
		        
		        else return null;
				
			}
			
			else if (Main.API.equals(ApiType.PlotSquared)) 
				try
				{
					return PlotAPI.class.newInstance().getPlot(player).owner;
				}
			
			catch (InstantiationException | IllegalAccessException e) {}
			
			else if (Main.API.equals(ApiType.UltimateClaims)) return UltimateClaims.getInstance().getClaimManager().getClaim(player).getOwner().getUniqueId();
			
			else if (Main.API.equals(ApiType.hClaims)) return player.getUniqueId();
			
			else if (Main.API.equals(ApiType.InfClaim))
				return player.getUniqueId();
			
			else if (Main.API.equals(ApiType.Lands))
				return player.getUniqueId();
			
		} catch (NullPointerException | ArrayIndexOutOfBoundsException e1) {}
		
		return null;
		
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isPlayerHasPermOnIsland(Player player, Location location)
	{
		
		try
		{
			
			if (FarmerManager.WORLDS.contains(location.getWorld().getName()))
			{
			
				String uuid = String.valueOf(getIslandOwnerUUID(location));
				if (uuid == null) return false;
				
				if (Main.instance.getConfig().isSet("Settings.onlyLeader")
						&& Main.instance.getConfig().getBoolean("Settings.onlyLeader")
						&& !player.getUniqueId().toString().equalsIgnoreCase(uuid))
					return false;
				
				if (Main.API.equals(ApiType.ASkyBlock))
				{
					
					if(ASkyBlockAPI.getInstance().getIslandAt(location).getMembers().contains(player.getUniqueId() )  
						|| ASkyBlockAPI.getInstance().getIslandAt(location).getOwner() == player.getUniqueId() ) return true;
					
					else return false;
					
				}
				
				else if (Main.API.equals(ApiType.FabledSkyblock))
				{
					
					if (SkyBlockAPI.getIslandManager().getIslandAtLocation(player.getLocation()).hasRole(player.getUniqueId(), IslandRole.MEMBER)
							|| SkyBlockAPI.getIslandManager().getIslandAtLocation(player.getLocation()).hasRole(player.getUniqueId(), IslandRole.OPERATOR)
							|| SkyBlockAPI.getIslandManager().getIslandAtLocation(player.getLocation()).hasRole(player.getUniqueId(), IslandRole.OWNER)) return true;
					
					else return false;
					
				}
				
				else if (Main.API.equals(ApiType.GriefPrevention))
				{
					
					String reason = null;
					
					try {reason = GriefPrevention.instance.dataStore.getClaimAt(location, false, null).allowAccess(player);} catch(NullPointerException e) {}
					
					if (uuid.equals(player.getUniqueId().toString())
							|| isMemberGrief(player, reason))return true;
					
					else return false;
					
				}
				
				else if (Main.API.equals(ApiType.SuperiorSkyblock))
				{
					
					if (SuperiorSkyblockAPI.getIslandAt(player.getLocation()).getIslandMembers(true).contains(SuperiorSkyblockAPI.getPlayer(player))) return true;
					else return false;
					
				} 
				
				else if (Main.API.equals(ApiType.IridiumSkyblock)) {
					if (getIslandOwnerUUID(player.getLocation()).equals(player.getUniqueId().toString()))
							return true;
					
					for (User user : IridiumSkyblockAPI.getInstance().getIslandViaLocation(player.getLocation()).get().getMembers()) {
						if (user.getName().equalsIgnoreCase(player.getName()))
							return true;
						else continue;
					}
					return false;
					
				} 
				
				else if (Main.API.equals(ApiType.BentoBox))
				{
					
					if (BentoBox.getInstance().getIslands().getIslandAt(player.getLocation()).get().getMemberSet().contains(player.getUniqueId())
							|| player.getUniqueId().toString().equalsIgnoreCase(uuid))
						return true;
					
					else return false;
					
				}
				
				else if (Main.API.equals(ApiType.PlotSquared))
				{
					
					if (PlotAPI.class.newInstance().getPlot(location).getMembers().contains(player.getUniqueId()) ||
									player.getUniqueId().toString().equalsIgnoreCase(uuid) ) return true;
					else return false;
							
				}
				
				else if (Main.API.equals(ApiType.UltimateClaims))
				{
					
					if (UltimateClaims.getInstance().getClaimManager().getClaim(location.getChunk()).isOwnerOrMember(player)) return true;
					else return false;
					
				}
				
				else if (Main.API.equals(ApiType.hClaims))
				{
					return (ClaimAPI.getInstance().getClaim(location).getFriendList().stream().anyMatch(claimFriend -> (claimFriend.getUUID().equals(player.getUniqueId()))) ||
							ClaimAPI.getInstance().getClaim(location).getOwnerUUID().equals(player.getUniqueId()));
				}
				
				else if (Main.API.equals(ApiType.InfClaim))
				{
					IClaim claim = InfClaimAPI.getClaimFromLocation(location);
					if (claim.getOwner().equalsIgnoreCase(player.getName()))
						return true;
					
					else {
						for (IClaimMember member : claim.getMembers()) {
							if (member.getMemberName().equalsIgnoreCase(player.getName()))
								return true;
							else continue;}
					}
					
					return false;
				}
				
				else if (Main.API.equals(ApiType.Lands))
				{
					
					return onEnableShortcut.lands.getLand(location).isTrusted(player.getUniqueId());
					
				}
				
				else return false;
				
			}
			
			else return false;
			
		} catch(NullPointerException | InstantiationException | IllegalAccessException e) {return false;}
		
	}
	
	@SuppressWarnings("deprecation")
	public static List<OfflinePlayer> getLandPlayers(OfflinePlayer player, String claimID)
	{
		
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		
		try
		{
			
			if (Main.API.equals(ApiType.ASkyBlock))
			{
				for (UUID s : ASkyBlockAPI.getInstance().getIslandOwnedBy(ASkyBlockAPI.getInstance().getTeamLeader(  player.getUniqueId()  )).getMembers()) players.add(Bukkit.getOfflinePlayer(s));	
			}
			
			else if (Main.API.equals(ApiType.FabledSkyblock))
			{
					for (UUID s : SkyBlockAPI.getIslandManager().getMembersOnline(SkyBlockAPI.getIslandManager().getIsland( player )))
					{
						
						if (s.toString().equalsIgnoreCase(player.getUniqueId().toString()))
							continue;
						
						players.add(Bukkit.getOfflinePlayer(s));
						
						
					}
			}
			
			else if (Main.API.equals(ApiType.GriefPrevention))
			{
					players.add(player);
			}
			
			else if (Main.API.equals(ApiType.SuperiorSkyblock))
			{
				for (SuperiorPlayer s : SuperiorSkyblockAPI.getPlayer(  player.getUniqueId()  ).getIsland().getIslandMembers(true)) players.add(Bukkit.getOfflinePlayer(s.getUniqueId()));
			}
			
			else if (Main.API.equals(ApiType.IridiumSkyblock)) {
				if (player != null && IridiumSkyblockAPI.getInstance().getUser(player).getIsland().get() != null) {
					for (User s : IridiumSkyblockAPI.getInstance().getUser(player).getIsland().get().getMembers()) players.add(Bukkit.getOfflinePlayer(s.getUuid()));}
				
			} 
			
			else if (Main.API.equals(ApiType.BentoBox))
			{
				for (UUID s : BentoBox.getInstance().getIslands().getIsland(
						BentoBox.getInstance().getAddonsManager().getGameModeAddons().get(0).getOverWorld(), player.getUniqueId()).getMemberSet()) players.add(Bukkit.getOfflinePlayer(s));
			}
			
			else if (Main.API.equals(ApiType.PlotSquared))
			{
				
				for (Plot plot : PlotAPI.class.newInstance().getPlayerPlots(Bukkit.getPlayer(player.getName())))
				{
					for (UUID member : plot.getMembers()) players.add(Bukkit.getOfflinePlayer(member));
				}
				
			}
			
			else if (Main.API.equals(ApiType.UltimateClaims))
			{
				for (ClaimMember member : UltimateClaims.getInstance().getClaimManager().getClaim(player.getUniqueId()).getMembers())
					players.add(member.getPlayer());
			}
			
			else if (Main.API.equals(ApiType.hClaims))
			{
				
				if (onEnableShortcut.USE_OWNER) {
				//	ClaimAPI.getInstance().claim
				}
				///	for (ClaimFriend friend : ClaimAPI.getInstance().getClaim(claimID).getFriendList())
				//		players.add(Bukkit.getOfflinePlayer(friend.getFriendName()));
				
				else {
					ClaimAPI.getInstance().getClaims(player).stream().forEach(claim -> {
						claim.getFriendList().stream().forEach(member -> {
							players.add(Bukkit.getOfflinePlayer(member.getUUID()));
						});
					});
				}
			}
			
			else if (Main.API.equals(ApiType.InfClaim))
			{
				if (onEnableShortcut.USE_OWNER)
					for (IClaimMember friend : BasicClaim.getClaimByID(Integer.valueOf(claimID)).getMembers())
						players.add(Bukkit.getOfflinePlayer(friend.getMemberName()));
				
				else
					for (IClaim claim : InfClaimAPI.getClaimsByOwner(player.getName()))
					{
						for (IClaimMember friend : claim.getMembers())
							players.add(Bukkit.getOfflinePlayer(friend.getMemberName()));
					}
				
			}
			
			else if (Main.API.equals(ApiType.Lands))
			{
				
				if (onEnableShortcut.USE_OWNER)
				{
					
					for (UUID p : onEnableShortcut.lands.getLand(claimID).getTrustedPlayers())
						players.add(Bukkit.getOfflinePlayer(p));
					
				}
				
				else
				{
					
					for (UUID p : onEnableShortcut.lands.getLandPlayer(player.getUniqueId()).getOwningLand().getTrustedPlayers())
						players.add(Bukkit.getOfflinePlayer(p));
					
				}
				
			}
			
			else return null;
			
		}
		
		catch(NullPointerException | InstantiationException | IllegalAccessException e) { return new ArrayList<OfflinePlayer>(); }
		
		return players;
		
		
	}
	
	
	private static boolean isMemberGrief(Player player, String reason)
	{
		
		if (reason != null && reason.contains(GriefPrevention.instance.dataStore.getMessage(Messages.NoAccessPermission,
				GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), false, null).getOwnerName()))) return false;
		
		else return true;
		
	}
	

}
