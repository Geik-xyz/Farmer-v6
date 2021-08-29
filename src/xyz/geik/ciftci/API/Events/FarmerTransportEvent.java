package xyz.geik.ciftci.API.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import xyz.geik.ciftci.Utils.Cache.StorageAndValues;

public class FarmerTransportEvent extends Event
{
	
	private final String farmerOwner;
	private final StorageAndValues farmer;
	private boolean isCancelled;
	
	public FarmerTransportEvent(String farmerOwner, StorageAndValues farmer)
	{
		
		this.farmerOwner = farmerOwner;
		
		this.farmer = farmer;
		
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
	public StorageAndValues getDetails()
	{
        return this.farmer;
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
