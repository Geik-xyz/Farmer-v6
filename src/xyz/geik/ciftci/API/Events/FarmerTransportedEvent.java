package xyz.geik.ciftci.API.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import xyz.geik.ciftci.Utils.Cache.StorageAndValues;

public class FarmerTransportedEvent extends Event
{
	
	private final String farmerOwner;
	private final Player player;
	private final StorageAndValues farmer;
	private final Location location;
	private boolean isCancelled;
	
	public FarmerTransportedEvent(String farmerOwner, StorageAndValues farmer, Location location, Player player)
	{
		
		this.farmerOwner = farmerOwner;
		
		this.farmer = farmer;
		
		this.location = location;
		
		this.player = player;
		
	}
	
	private static final HandlerList HANDLERS = new HandlerList();

	@Override
	public HandlerList getHandlers()
	{
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList()
	{
        return HANDLERS;
    }
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @apiNote Farmer Owner UUID
	 * @return
	 */
	public String getFarmerOwner()
	{
        return this.farmerOwner;
    }
	
	public Player getWhoTransported()
	{
		return this.player;
	}
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @apiNote StorageAndValues Object
	 * @return
	 */
	public StorageAndValues getDetails()
	{
        return this.farmer;
    }
	
	public Location getNewLocation()
	{
		return this.location;
	}
	
	public boolean isCancelled()
	{
	    return this.isCancelled;
	}
	 
	public void setCancelled(boolean arg0)
	{
	    this.isCancelled = arg0;
	}

}
