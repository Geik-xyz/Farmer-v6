package xyz.geik.ciftci.Utils.Cache;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import xyz.geik.ciftci.Main;

public class Farmer {
	
	String islandID;
	
	int farmerLevel;
	
	int farmerID;
	
	int sellingStatus;
	
	Location npcLoc;
	
	List<OfflinePlayer> player;
	
	String skinName;
	
	boolean autoSell;
	
	boolean autoCollect;
	
	boolean spawnerKill;
	
	public Farmer(String islandID, int farmerLevel, int farmerID, int sellingStatus, Location npcLoc, List<OfflinePlayer> players, String skinName, boolean autoSell, boolean autoCollect, boolean spawnerKill)
	{
		
		this.islandID = islandID;
		
		this.farmerLevel = farmerLevel;
		
		this.farmerID = farmerID;
		
		this.sellingStatus = sellingStatus;
		
		this.npcLoc = npcLoc;
		
		this.player = players;
		
		this.skinName = skinName;
		
		this.autoSell = autoSell;
		
		this.autoCollect = autoCollect;
		
		this.spawnerKill = spawnerKill;
		
	}
	
	public String getOwnerUUID()
	{
		return islandID;
	}
	
	public int getFarmerLevel()
	{
		return farmerLevel;
	}
	
	public int getFarmerID()
	{
		return farmerID;
	}
	
	public int getSellingStatus()
	{
		return sellingStatus;
	}
	
	public Location getNpcLocation()
	{
		return npcLoc;
	}
	
	public List<OfflinePlayer> getMemberList()
	{
		return player;
	}
	
	public String getSkinName()
	{
		return skinName;
	}
	
	public boolean getAutoSell()
	{
		return autoSell;
	}
	
	public boolean getAutoCollect()
	{
		return autoCollect;
	}
	
	public boolean getSpawnerKill()
	{
		return spawnerKill;
	}
	
	
	
	public void setSpawnerKill(boolean status)
	{
		if (Main.spawnerKiller)
			this.spawnerKill = status;
	}
	
	public void setAutoSell(boolean status)
	{
		if (Main.autoSell)
			this.autoSell = status;
	}
	
	public void setAutoCollect(boolean status)
	{
		if (Main.autoCollector)
			this.autoCollect = status;
	}
	
	public void setOwnerUUID(String islandID)
	{
		this.islandID = islandID;
	}
	
	public void setFarmerLevel(int farmerLevel)
	{
		this.farmerLevel = farmerLevel;
	}
	
	public void setFarmerID(int farmerID) 
	{
		this.farmerID = farmerID;
	}
	
	public void setFarmerLocation(Location loc)
	{
		this.npcLoc = loc;
	}
	
	public void setSellingStatus(int sellingStatus)
	{
		this.sellingStatus = sellingStatus;
	}
	
	public void setPlayers(List<OfflinePlayer> player)
	{
		this.player = player;
	}
	
	public void setSkinName(String skinName)
	{
		this.skinName = skinName;
	}

}
