package xyz.geik.ciftci.Utils.Cache;

import java.util.HashMap;

import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;

public class StorageAndValues {
	
	Farmer farmer;
	HashMap<ConfigItems, Integer> values;
	NPCImpl npc;
	
	public StorageAndValues(Farmer farmer, HashMap<ConfigItems, Integer> values, NPCImpl npc) {
		this.farmer = farmer;
		this.values = values;
	}
	
	public Farmer getStorage() {
		return farmer;
	}
	public HashMap<ConfigItems, Integer> getItemValues() {
		return values;
	}
	public NPCImpl getNPC() {
		return npc;
	}
	public void setItemValues(HashMap<ConfigItems, Integer> values) {
		this.values = values;
	}
	public void updateFarmerStorage(Farmer farmer) {
		this.farmer = farmer;
	}
	
	public void setNPC(NPCImpl npc) {
		this.npc = npc;
	}

}
