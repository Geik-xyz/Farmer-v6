package xyz.geik.ciftci.Utils.NPC.npc;

import lombok.RequiredArgsConstructor;
import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;
import xyz.geik.ciftci.Utils.NPC.util.PacketUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class NPCInventory {

    private static final ItemStack air = new ItemStack(Material.AIR);

    private final NPCImpl npc;
    private final Map<EquipmentSlot, ItemStack> slots = new HashMap<>();

    public NPCInventory(NPCImpl npc) {
        this.npc = npc;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            slots.put(slot, air);
        }
    }

    public void set(EquipmentSlot slot, ItemStack item) {
        item = item == null ? air : item;
        slots.replace(slot, item);
        if(npc.spawned) {
            PacketUtil.equip(npc, slot.getId(EquipmentSlot.HAND), item, npc.rangePlayers);
        }
    }

    public ItemStack get(EquipmentSlot slot) {
        return slots.get(slot);
    }

    public void update() {
        slots.forEach((slot, item) -> {
            if(item.getType() != Material.AIR) {
                PacketUtil.equip(npc, slot.getId(EquipmentSlot.HAND), item, npc.rangePlayers);
            }
        });
    }

    @RequiredArgsConstructor
    public enum EquipmentSlot {
        HAND, BOOTS, LEGGINGS, CHESTPLATE, HELMET;

    	public int getId(EquipmentSlot type)
        {
        	
        	if (type.equals(EquipmentSlot.HAND))
        		return 0;
        	
        	else if (type.equals(EquipmentSlot.BOOTS))
        		return 1;
        	
        	else if (type.equals(EquipmentSlot.LEGGINGS))
        		return 2;
        	
        	else if (type.equals(EquipmentSlot.CHESTPLATE))
        		return 3;
        	
        	else if (type.equals(EquipmentSlot.HELMET))
        		return 4;
        	
        	else return 0;
        	
        }
    }
}
