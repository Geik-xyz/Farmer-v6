package xyz.geik.ciftci.API.Events;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FarmerPlaceEvent extends Event
{
	
	private final String farmerOwner;
	
	private final Location location;
	
	private boolean isCancelled = false;
	
	public FarmerPlaceEvent(String farmerOwner, Location location)
	{
		
		this.farmerOwner = farmerOwner;
		
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
	
	public Location getLocation()
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