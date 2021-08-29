package xyz.geik.ciftci.Utils.NPC.listener;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;
import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinQuitListener implements Listener {
	
	public JoinQuitListener(Main plugin) {}

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (StorageAndValues value : FarmerManager.farmerCache.values()) {
        	if(value.getNPC() == null)
        		return;
        	NPCImpl npc = value.getNPC();
            if(npc.getOfflinePlayers().contains(p.getUniqueId())) {
                npc.getOfflinePlayers().remove(p.getUniqueId());
                npc.getPlayers().add(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        npc.checkRange(p);
                    }
                }.runTaskLater(npc.plugin, 3);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        for (StorageAndValues value : FarmerManager.farmerCache.values()) {
        	if(value.getNPC() == null)
        		return;
        	NPCImpl npc = value.getNPC();
            if(npc.getPlayers().contains(p)) {
                npc.getPlayers().remove(p);
                npc.getRangePlayers().remove(p);
                npc.getOfflinePlayers().add(p.getUniqueId());
            }
        }
    }
}
