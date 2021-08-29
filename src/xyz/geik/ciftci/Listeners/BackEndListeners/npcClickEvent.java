package xyz.geik.ciftci.Listeners.BackEndListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Gui.FarmerInventoryGUI;
import xyz.geik.ciftci.Utils.Gui.FarmerManagmentGUI;
import xyz.geik.ciftci.Utils.NPC.npc.NPC;

public class npcClickEvent implements Listener 
{

	public Main plugin;
	
	public npcClickEvent(Main plugin)
	{
		this.plugin = plugin;
	}
	
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @apiNote cancel event & open gui
	 * @param e
	 */
	
	public static void NpcClick(NPC npc, Player player)
	{

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () ->
		{
			
			try
			{
				
				String id = String.valueOf(ApiFun.getIslandOwnerUUID(  player.getLocation()  ));
				
				String owner = id;
				
				if (onEnableShortcut.USE_OWNER)
					owner = ApiFun.getOwnerViaID(id).getUniqueId().toString();
				
				if (id == null) return;
				
				else if (FarmerManager.farmerCache.containsKey(id))
				{
					
					if (ApiFun.isPlayerHasPermOnIsland(player, player.getLocation()))
					{
						
						if (player.isSneaking() && player.getUniqueId().toString().equalsIgnoreCase(owner))
							FarmerManagmentGUI.createGui(player, false);
							
						else FarmerInventoryGUI.createGui(player, 1, id);
						
						if (FarmerManager.farmerCache.get(id).getStorage().getNpcLocation() == null)
						{
							
							FarmerManager.farmerCache.get(id).getStorage().setFarmerLocation(npc.getLocation());
							
							DatabaseQueries.updateFarmerLocation(id, npc.getLocation());
							
						}
						
					}
					
					else if (player.isOp())
					{
						
						FarmerInventoryGUI.createGui(player, 1, id);
						
					}
					
				}
			
			}
			
			catch (NullPointerException | NumberFormatException e1) { }
			
		});
		
	}

}
