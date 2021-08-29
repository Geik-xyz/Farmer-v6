package xyz.geik.ciftci.API.Events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import xyz.geik.ciftci.Utils.Cache.StorageAndValues;

public class FarmerPlacedEvent extends Event
{
	
	private final String farmerOwner;
	private final StorageAndValues farmer;
	private final Location location;
	
	public FarmerPlacedEvent(String farmerOwner, StorageAndValues farmer, Location location)
	{
		
		this.farmerOwner = farmerOwner;
		
		this.farmer = farmer;
		
		this.location = location;
		
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
	
	/**
	 * @author Geik
	 * @since 1.0.0
	 * @apiNote Farmer Object
	 * @return
	 */
	public StorageAndValues getFarmer()
	{
        return this.farmer;
    }
	
	public Location getLocation()
	{
		return this.location;
	}

}
