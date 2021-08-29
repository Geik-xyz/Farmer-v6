package xyz.geik.ciftci.Listeners.ApiListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.plotsquared.bukkit.events.PlotChangeOwnerEvent;
import com.plotsquared.bukkit.events.PlotDeleteEvent;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;

public class PlotSquaredListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main plugin;
	public PlotSquaredListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void plotSquaredRemoveEvent(PlotDeleteEvent e) throws InstantiationException, IllegalAccessException
	{
		
		String uuid = String.valueOf(e.getPlot().guessOwner());
		
		Player player = Bukkit.getPlayer(e.getPlot().guessOwner());
		
		if (PlotAPI.class.newInstance().getPlayerPlots(player) != null && PlotAPI.class.newInstance().getPlayerPlots(player).size() > 1)
			return;
			
		if (DatabaseQueries.areaHasFarmer(uuid))
		{
			
			if (FarmerManager.farmerCache.containsKey(uuid))
			{
				
				if (Bukkit.getOfflinePlayer(e.getPlot().owner).isOnline())
					FarmerManager.giveEggToPlayer(Bukkit.getPlayer(e.getPlot().owner), FarmerManager.farmerCache.get(uuid).getStorage().getFarmerLevel());
				
				FarmerManager.leaveHandler(uuid, FarmerManager.farmerCache.get(uuid));
				
			}
			
			DatabaseQueries.removeFarmer(uuid);
			
			FarmerManager.removeFarmerDataYML(uuid);
			
		}
		
	}
	
	@EventHandler
	public void plotOwnerChangeEvent(PlotChangeOwnerEvent e)
	{
		
		String uuid = e.getOldOwner().toString();
		
		String newOwner = e.getNewOwner().toString();
		
		FarmerManager.changeFarmerOwner(uuid, newOwner);
		
	}

}
