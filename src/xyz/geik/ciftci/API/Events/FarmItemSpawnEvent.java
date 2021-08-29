package xyz.geik.ciftci.API.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import xyz.geik.ciftci.Utils.Cache.StorageAndValues;

public class FarmItemSpawnEvent extends Event
{

	private final String farmerOwner;
	
	private final StorageAndValues farmer;
	
	private final ItemStack item;
	
	private boolean isCancelled;
	
	public FarmItemSpawnEvent(String farmerOwner, StorageAndValues farmer, ItemStack item)
	{
		
		this.farmerOwner = farmerOwner;
		
		this.farmer = farmer;
		
		this.item = item;
		
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
	
	public ItemStack getItem()
	{
		return item;
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
