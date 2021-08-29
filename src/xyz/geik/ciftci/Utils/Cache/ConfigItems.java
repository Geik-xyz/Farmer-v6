package xyz.geik.ciftci.Utils.Cache;

import xyz.geik.ciftci.Utils.API.FarmItemType;

public class ConfigItems {
	
	String dataName;
	
	String itemMaterial;
	
	int damage;
	
	double price;
	
	boolean autoCollect;
	
	FarmItemType type;
	
	public ConfigItems(String dataName, String itemMaterial, int damage, double price, boolean autoCollect, FarmItemType type)
	{
		
		this.dataName = dataName;
		
		this.itemMaterial = itemMaterial;
		
		this.damage = damage;
		
		this.price = price;
		
		this.autoCollect = autoCollect;
		
		this.type = type;
		
	}
	
	public String getDataName()
	{
		return this.dataName;
	}
	
	public String getItemMaterial()
	{
		return this.itemMaterial;
	}
	
	public int getDamage()
	{
		return this.damage;
	}
	
	public double getPrice()
	{
		return this.price;
	}
	
	public FarmItemType getType()
	{
		return type;
	}

}
