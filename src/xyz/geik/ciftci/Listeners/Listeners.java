package xyz.geik.ciftci.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Listeners.ApiListeners.FabledSkyblockListener;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.API.ApiType;

public class Listeners implements Listener {
	
	public Main plugin;
	
	public Listeners(Main plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Grief & Plot overrider for trust commands
	 * 
	 * @param e
	 */
	@EventHandler
	public void commandEvent(PlayerCommandPreprocessEvent e)
	{
		
		try
		{
			
			String uuid = ApiFun.getIslandOwnerUUID(e.getPlayer().getLocation());
			
			String owner = uuid;
			
			if (onEnableShortcut.USE_OWNER) {

				owner = ApiFun.getOwnerViaID(uuid).toString();
			}
			
			if (!owner.equalsIgnoreCase(e.getPlayer().getUniqueId().toString()))
				return;
			
			String[] cmd = e.getMessage().substring(1).split(" ");
			
			if (Main.API.equals(ApiType.GriefPrevention) || Main.API.equals(ApiType.PlotSquared))
			{
				
				if (Main.API.equals(ApiType.GriefPrevention) && cmd[0].equalsIgnoreCase("untrust"))
				{
					
					if (cmd.length != 2)
						return;
					
					String target = cmd[1];
					
					FarmerManager.removeMember(uuid, Bukkit.getPlayer(target));
					
				}
				
				if (Main.API.equals(ApiType.GriefPrevention) && cmd[0].equalsIgnoreCase("trust"))
				{
					
					if (cmd.length != 2)
						return;
					
					String target = cmd[1];
					
					FarmerManager.addMember(uuid, Bukkit.getPlayer(target));
					
				}
				
				if (Main.API.equals(ApiType.PlotSquared) && cmd[0].equalsIgnoreCase("p") || cmd[0].contains("plot"))
				{
					
					if (cmd[1].equalsIgnoreCase("kick") || cmd[1].equalsIgnoreCase("remove"))
					{
						
						if (cmd.length != 3)
							return;
						
						String target = cmd[2];
						
						FarmerManager.removeMember(uuid, Bukkit.getPlayer(target));
						
					}
					
					else if (cmd[1].equalsIgnoreCase("add") || cmd[1].equalsIgnoreCase("trust"))
					{
						
						if (cmd.length != 3)
							return;
						
						String target = cmd[2];
						
						FarmerManager.addMember(uuid, Bukkit.getPlayer(target));
						
					}
					
					else if (cmd[1].equalsIgnoreCase("setowner"))
					{
						
						if (cmd.length != 3)
							return;
						
						@SuppressWarnings("deprecation")
						String targetUUID = Bukkit.getOfflinePlayer(cmd[2]).getUniqueId().toString();
						
						FarmerManager.changeFarmerOwner(owner, targetUUID);
						
					}
					
				}
				
				if ((Main.API.equals(ApiType.FabledSkyblock) && cmd[0].equalsIgnoreCase("is") && cmd[1].equalsIgnoreCase("makeleader"))
						&& !onEnableShortcut.USE_OWNER)
				{
					
					if (cmd.length != 3)
						return;
					
					if (FabledSkyblockListener.awaitForLeader.containsKey(cmd[2]))
						FabledSkyblockListener.awaitForLeader.remove(cmd[2]);
					
					FabledSkyblockListener.awaitForLeader.put(cmd[2], owner);
					
					Bukkit.getScheduler().runTaskLaterAsynchronously(Main.instance, () -> {
						if (FabledSkyblockListener.awaitForLeader.containsKey(cmd[2]))
							FabledSkyblockListener.awaitForLeader.remove(cmd[2]);
					}, 13*20);
					
				}
				
			}
			
		}
		
		catch(ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException e1) {}
		
	}

}
