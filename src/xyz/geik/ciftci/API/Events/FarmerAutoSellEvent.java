package xyz.geik.ciftci.API.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import xyz.geik.ciftci.Utils.Cache.ConfigItems;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;

public class FarmerAutoSellEvent  extends Event
{
	
	private final String farmerOwner;
	
	private final ConfigItems item;
	
	private final StorageAndValues playerValues;
	
	private final double tax;
	
	private final double money;
	
	private final double withoutTaxMoney;
	
	private boolean isCancelled = false;
	
	public FarmerAutoSellEvent(String farmerOwner, ConfigItems item, double tax, double money, double withoutTaxMoney, StorageAndValues playerValues)
	{
		
		this.farmerOwner = farmerOwner;
		
		this.item = item;
		
		this.tax = tax;
		
		this.money = money;
		
		this.withoutTaxMoney = withoutTaxMoney;
		
		this.playerValues = playerValues;
		
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
	public ConfigItems getItem()
	{
        return this.item;
    }
	
	public StorageAndValues getPlayerValues()
	{
		return this.playerValues;
	}
	
	public double getTaxMoney()
	{
		return this.tax;
	}
	
	public double getDepositMoney()
	{
		return this.money;
	}
	
	public double getMoneyWithoutTax()
	{
		return this.withoutTaxMoney;
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
