package xyz.geik.ciftci.Utils.NPC.listener;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;
import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
	
	// INSTANCE
	public MoveListener(Main plugin) {}
	
	// CACHE
	public static HashMap<String, List<String>> npcCache = new HashMap<String, List<String>>();

	/**
	 * Move Updater
	 * 
	 * @param e
	 */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void move(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        
        if (!FarmerManager.WORLDS.contains(to.getWorld().getName()))
        	return;
        
        if(to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ())
            return;
        
        final Player player = e.getPlayer();
        
        if (!npcCache.containsKey(player.getName()) && !player.isOp())
        	return;
        
        if (Main.instance.getConfig().isSet("DetailedSettings.asyncNPC")
        		&& Main.instance.getConfig().getBoolean("DetailedSettings.asyncNPC"))
	        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
	        	npcUpdater(e.getPlayer(), to);
	        });
        
        else
        	npcUpdater(e.getPlayer(), to);
        
    }
    
    /**
     * World change updater
     * 
     * @param e
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void worldChange(PlayerChangedWorldEvent e)
    {
    	
    	String world = e.getPlayer().getWorld().getName();
    	
    	if (Main.instance.getConfig().isSet("DetailedSettings.asyncNPC")
        		&& Main.instance.getConfig().getBoolean("DetailedSettings.asyncNPC"))
	    	Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
	    		teleportUpdater(world, e.getFrom().getName(), e.getPlayer());
	    	});
    	
    	else
    		teleportUpdater(world, e.getFrom().getName(), e.getPlayer());
    	
    }
    
    private void teleportUpdater(String world, String from, Player player) {
    	try {
			
    		if (!onEnableShortcut.scanOnlyPlayer || player.hasPermission("ciftci.seeall")) 
    		{
				
				if (FarmerManager.WORLDS.contains(world) && !FarmerManager.WORLDS.contains(from))
		    		return;
				
				for(StorageAndValues value : FarmerManager.farmerCache.values()) {
        			try {
        				
        				NPCImpl npc = value.getNPC();
        				
                    	if(npc.location == null)
                            continue;
                    	
                    	if (!player.hasPermission("ciftci.seeall")
            					&& !npc.getPlayers().contains(player))
            				continue;
                    	if(npc.rangePlayers.contains(player)) {
                            npc.rangePlayers.remove(player);
                            if(npc.spawned) {
                                npc.rangePlayersUpdated(player);
                            }
                            else
                            	npc.setSpawned(true);
                    	}
                    	
        			}
        			catch (ArrayIndexOutOfBoundsException e1) {continue;}
                	
            	}
            	
    		}
    		
    		else {
    			
    			if (!npcCache.containsKey(player.getName())) 
    				return;
    			
            	for (String cache : npcCache.get(player.getName()))
            	{
            		
            		try {
                		NPCImpl npc = FarmerManager.farmerCache.get(cache).getNPC();
                		
                		if(npc.location == null || !npc.players.contains(player))
                            continue;
                		
                    	if(npc.rangePlayers.contains(player)
                    			&& !npc.getLocation().getWorld().getName().equalsIgnoreCase(world)) {
                    		npc.rangePlayers.remove(player);
                            if(npc.spawned) {
                                npc.rangePlayersUpdated(player);
                            }
                            continue;
                        }
                    	
                    	else if (FarmerManager.WORLDS.contains(world))
            			{
            				Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
            					npc.checkRange(player);
            				}, 1L);
            			}
            			
            		}
            		catch (ArrayIndexOutOfBoundsException | NullPointerException e1) { continue; }
            	}
    			
    		}
			
		}
		catch (NullPointerException e1) { }
    }
    
    private void npcUpdater(Player player, Location to)
    {
    		
    	try
		{
    		
    		if (!onEnableShortcut.scanOnlyPlayer || player.hasPermission("ciftci.seeall")) 
    		{
    			
    			for(StorageAndValues value : FarmerManager.farmerCache.values())
            	{
            		
            		try
            		{
            			
            			NPCImpl npc = value.getNPC();
            			
                    	if (npc.getPlayers() == null || npc.getPlayers().isEmpty())
                    		continue;
                        if(npc.location == null || to.getWorld() != npc.location.getWorld())
                            continue;
                        
                        if (!player.hasPermission("ciftci.seeall")
            					&& !npc.getPlayers().contains(player))
            				continue;
                        
                        if(to.distance(npc.getLocation()) > Main.NPC_RADIUS) {
                            if(npc.rangePlayers.contains(player)) {
                                npc.rangePlayers.remove(player);
                                if(npc.spawned) {
                                    npc.rangePlayersUpdated(player);
                                }
                            }
                        } else {
                            if(!npc.rangePlayers.contains(player)) {
                                npc.rangePlayers.add(player);
                                if(npc.spawned) {
                                    npc.rangePlayersUpdated(player);
                                }
                                else
                                	npc.setSpawned(true);
                            }
                        }
            			
            		}
            		
            		catch (ArrayIndexOutOfBoundsException | NullPointerException e1) { continue; }
            		
                }
    			
    		}
    		
        	else
        	{
        		
        		if (!npcCache.containsKey(player.getName())) 
    				return;
        		
            	for (String cache : npcCache.get(player.getName()))
            	{
            		
            		try
            		{
            			
            			final NPCImpl npc = FarmerManager.farmerCache.get(cache).getNPC();
                    	if (npc.getPlayers() == null || npc.getPlayers().isEmpty())
                    		continue;
                        if(npc.location == null || to.getWorld() != npc.location.getWorld() || !npc.players.contains(player))
                            continue;
                        
                        if((int) to.distance(npc.getLocation()) >= Main.NPC_RADIUS) {
                            if(npc.rangePlayers.contains(player)) {
                            	
                            	npc.rangePlayers.remove(player);
                                if(npc.spawned)
                                	npc.rangePlayersUpdated(player);
                                
                                continue;
                                
                            }
                        }
                        
                        else {
                        	if(!npc.rangePlayers.contains(player))
                        	{
                                npc.rangePlayers.add(player);
                                if(npc.spawned) {
                                	npc.rangePlayersUpdated(player);
                                }
                                else
                                	npc.setSpawned(true);
                                
                                continue;
                                
                            }
                        }
            			
            		}
            		
            		catch (ArrayIndexOutOfBoundsException | NullPointerException e1) { continue; }
            		
            	}
            	
        	}
    		
		}
    	
    	catch (ArrayIndexOutOfBoundsException | NullPointerException e1) { e1.printStackTrace(); }
    	
    }
    
}