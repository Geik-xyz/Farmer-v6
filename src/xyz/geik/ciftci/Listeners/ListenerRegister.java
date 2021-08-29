package xyz.geik.ciftci.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Listeners.ApiListeners.AskyblockListener;
import xyz.geik.ciftci.Listeners.ApiListeners.BentoBoxListeners;
import xyz.geik.ciftci.Listeners.ApiListeners.FabledSkyblockListener;
import xyz.geik.ciftci.Listeners.ApiListeners.GriefPreventionListener;
import xyz.geik.ciftci.Listeners.ApiListeners.InfClaimListener;
import xyz.geik.ciftci.Listeners.ApiListeners.IridiumSkyblockListener;
import xyz.geik.ciftci.Listeners.ApiListeners.LandsListener;
import xyz.geik.ciftci.Listeners.ApiListeners.PlotSquaredListener;
import xyz.geik.ciftci.Listeners.ApiListeners.SuperiorSkyblockListener;
import xyz.geik.ciftci.Listeners.ApiListeners.UltimateClaimsListener;
import xyz.geik.ciftci.Listeners.ApiListeners.hClaimsListener;
import xyz.geik.ciftci.Listeners.BackEndListeners.onBlockGrowEvent;
import xyz.geik.ciftci.Listeners.BackEndListeners.onJoinEvent;
import xyz.geik.ciftci.Listeners.BackEndListeners.onQuitEvent;
import xyz.geik.ciftci.Listeners.BackEndListeners.SpawnerEvent.EpicSpawnersListener;
import xyz.geik.ciftci.Listeners.BackEndListeners.SpawnerEvent.spawnerEvent;
import xyz.geik.ciftci.Listeners.FarmerEvents.onFarmItemSpawnEvent;
import xyz.geik.ciftci.Listeners.FarmerEvents.onPlaceEvent;
import xyz.geik.ciftci.Listeners.GuiListeners.AddonsGuiListener;
import xyz.geik.ciftci.Listeners.GuiListeners.BuyGuiListener;
import xyz.geik.ciftci.Listeners.GuiListeners.FarmerGuiListener;
import xyz.geik.ciftci.Listeners.GuiListeners.FarmerManagmentGuiListener;
import xyz.geik.ciftci.Utils.API.ApiType;
import xyz.geik.ciftci.Utils.NPC.listener.JoinQuitListener;
import xyz.geik.ciftci.Utils.NPC.listener.MoveListener;

public class ListenerRegister
{

	public Main plugin;
	
	public ListenerRegister(Main plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Listener registers
	 * 
	 */
	public ListenerRegister()
	{
		
		registerListener(new FarmerGuiListener(Main.instance));

		registerListener(new FarmerManagmentGuiListener(Main.instance));

		registerListener(new AddonsGuiListener(Main.instance));

		registerListener(new onJoinEvent(Main.instance));

		registerListener(new onQuitEvent(Main.instance));

		registerListener(new onFarmItemSpawnEvent(Main.instance));

		registerListener(new onPlaceEvent(Main.instance));
		
		registerListener(new MoveListener(Main.instance));
		
		registerListener(new JoinQuitListener(Main.instance));
		
		registerListener(new BuyGuiListener(Main.instance));
		
		if (Main.API.equals(ApiType.GriefPrevention) || Main.API.equals(ApiType.PlotSquared))
			registerListener(new Listeners(Main.instance));
		
		if (Main.instance.getConfig().getBoolean("AddonSettings.autoCollect.feature") && Main.autoCollector)
			registerListener(new onBlockGrowEvent(Main.instance));
		
		if (Main.spawnerKiller && Bukkit.getPluginManager().getPlugin("EpicSpawners") == null)
			registerListener(new spawnerEvent(Main.instance));
		
		else if (Main.spawnerKiller && Bukkit.getPluginManager().getPlugin("EpicSpawners") != null)
			registerListener(new EpicSpawnersListener(Main.instance));
		
		if (Main.API.equals(ApiType.ASkyBlock))
			registerListener(new AskyblockListener(Main.instance));
		
		else if (Main.API.equals(ApiType.FabledSkyblock))
			registerListener(new FabledSkyblockListener(Main.instance));
		
		else if (Main.API.equals(ApiType.GriefPrevention))
			registerListener(new GriefPreventionListener(Main.instance));
		
		else if (Main.API.equals(ApiType.SuperiorSkyblock))
			registerListener(new SuperiorSkyblockListener(Main.instance));
		
		else if (Main.API.equals(ApiType.IridiumSkyblock))
			registerListener(new IridiumSkyblockListener(Main.instance));
		
		else if (Main.API.equals(ApiType.BentoBox))
			registerListener(new BentoBoxListeners(Main.instance));
			
		else if (Main.API.equals(ApiType.PlotSquared))
			registerListener(new PlotSquaredListener(Main.instance));
		
		else if (Main.API.equals(ApiType.UltimateClaims))
			registerListener(new UltimateClaimsListener(Main.instance));
		
		else if (Main.API.equals(ApiType.hClaims))
			registerListener(new hClaimsListener(Main.instance));
		
		else if (Main.API.equals(ApiType.InfClaim))
			registerListener(new InfClaimListener(Main.instance));
		
		else if (Main.API.equals(ApiType.Lands))
			registerListener(new LandsListener(Main.instance)); 
		
	}
	
	private void registerListener(Listener object)
	{
		Bukkit.getPluginManager().registerEvents(object, Main.instance);
	}
	
}
