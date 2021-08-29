package xyz.geik.ciftci.Utils.NPC.util;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.NPC.npc.AnimationType;
import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;

public class PacketUtil {
    private PacketUtil() {}

    public static void showNPC(NPCImpl npc, List<Player> players)
    {
    	
    	String nms = Manager.getNMSVersion();
    	
    	PacketContainer packet2 = npc.manager.createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
    	
    	final WrapperPlayServerNamedEntitySpawn wrapperPlayServerSpawnEntity = new WrapperPlayServerNamedEntitySpawn();
    	
    	packet2.getIntegers().write(0, npc.getEntityId());
    	
    	if (!nms.contains("1_8")) 
    		packet2 = npc.manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
    	
    	final PacketContainer packet = packet2;
    	
    	if (nms.contains("1_8")) {
    		
    		packet.getUUIDs().write(0, npc.uuid);
    		
    		packet.getIntegers().write(1, CoordinateUtil.getFixedNumber(npc.location.getX()));
    		packet.getIntegers().write(2, CoordinateUtil.getFixedNumber(npc.location.getY()));
    		packet.getIntegers().write(3, CoordinateUtil.getFixedNumber(npc.location.getZ()));
    		packet.getBytes().write(0, CoordinateUtil.getByteAngle(npc.location.getYaw()));
            packet.getBytes().write(1, CoordinateUtil.getByteAngle(npc.location.getPitch()));
            
            packet.getDataWatcherModifier().write(0, createWatcher1_8(npc));
    		
    	}
    	
    	else {
    		
        	wrapperPlayServerSpawnEntity.setEntityID(npc.getEntityId());
        	wrapperPlayServerSpawnEntity.setPlayerUUID(npc.getUuid());
        	
        	wrapperPlayServerSpawnEntity.setX(npc.getLocation().getX());
            wrapperPlayServerSpawnEntity.setY(npc.getLocation().getY());
            wrapperPlayServerSpawnEntity.setZ(npc.getLocation().getZ());
            wrapperPlayServerSpawnEntity.setYaw(CoordinateUtil.getByteAngle(npc.location.getYaw()));
            wrapperPlayServerSpawnEntity.setPitch(CoordinateUtil.getByteAngle(npc.location.getPitch()));
            Bukkit.getScheduler().runTask(Main.instance, () -> {
            	final WrappedDataWatcher watcher = new WrappedDataWatcher();
                final WrappedDataWatcher.Serializer serializerInt = WrappedDataWatcher.Registry.get(Byte.class);
                final Entity entity = wrapperPlayServerSpawnEntity.getEntity(npc.getLocation().getWorld());
                watcher.setEntity(entity);
                watcher.setObject(0, serializerInt, (byte) npc.state.getId());
              //  watcher.setObject(16, serializerInt, npc.skinLayers.getFlags());
                
                packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
            });
    		
    	}
        
        PacketContainer rotation = npc.manager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        rotation.getIntegers().write(0, npc.entityId);
        rotation.getBytes().write(0, CoordinateUtil.getByteAngle(npc.location.getYaw()));	

        PacketContainer remove = npc.manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        remove.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        remove.getPlayerInfoDataLists().write(0, Collections.singletonList(npc.infoData));
        
        try {
            for(Player p : players) {
            	if (!nms.contains("1_8"))
            		wrapperPlayServerSpawnEntity.sendPacket(p);
            	
            	npc.manager.sendServerPacket(p, packet);
                npc.manager.sendServerPacket(p, rotation);
                npc.inventory.update();
                
            }
        } catch(InvocationTargetException e) {
            npc.plugin.getLogger().log(Level.WARNING, "Could not send npc packets", e);
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for(Player p : players) {
                        if(p.isOnline())
                            npc.manager.sendServerPacket(p, remove);
                    }
                } catch(InvocationTargetException e) {
                    npc.plugin.getLogger().log(Level.WARNING, "Could not send remove packet", e);
                }
            }
        }.runTaskLater(npc.plugin, 20);
    }

    public static void addNPC(NPCImpl npc, List<Player> players) {
        PacketContainer info = npc.manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        
        info.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        WrappedGameProfile gameProfile = new WrappedGameProfile(npc.uuid, npc.name);
        if(npc.getTextures() != null && npc.getProperty() != null) {
            gameProfile.getProperties().put("textures", npc.getProperty());
        }
        npc.profile = gameProfile;

        npc.infoData = new PlayerInfoData(gameProfile, 0, npc.mode.getNativeGameMode(), null);
        info.getPlayerInfoDataLists().write(0, Collections.singletonList(npc.infoData));

        try {
            for(Player p : players) {
                npc.manager.sendServerPacket(p, info);
            }
        } catch(InvocationTargetException e) {
            npc.plugin.getLogger().log(Level.WARNING, "Could not send add player packet", e);
        }
        
    }

    public static void removeNPC(NPCImpl npc, List<Player> players) {
    	
    	
    	try {
    		
    		if (players == null || players.isEmpty())
        		return;
        	
            PacketContainer destroyPacket = npc.manager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            if (Manager.getNMSVersion().contains("1_17"))
            	destroyPacket.getIntLists().write(0, Arrays.asList(npc.getEntityId()));
            else
            	destroyPacket.getIntegerArrays().write(0, new int[] {npc.entityId});

            try {
                for(Player p : players) {
                	if (p != null && p.isOnline())
                		npc.manager.sendServerPacket(p, destroyPacket);
                }
            } catch(InvocationTargetException e) {
                npc.plugin.getLogger().log(Level.WARNING, "Could not send destroy npc packet", e);
            }
            
    	}
    	
    	catch (NullPointerException | NoSuchElementException e1) {}
    	
    }

    public static void updateMetadata(NPCImpl npc, List<Player> players) {
        PacketContainer metadataPacket = npc.manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metadataPacket.getIntegers().write(0, npc.entityId);
        Bukkit.getScheduler().runTask(Main.instance, () ->
        {
        	
        	metadataPacket.getWatchableCollectionModifier().write(0, createWatcher1_8(npc).getWatchableObjects());
        	
        });

        try {
            for(Player p : players) {
                npc.manager.sendServerPacket(p, metadataPacket);
            }
        } catch(InvocationTargetException e) {
            npc.plugin.getLogger().log(Level.WARNING, "Could not send metadata packet", e);
        }
    }

    public static void teleport(NPCImpl npc, Location location, List<Player> players) {
        PacketContainer teleport = npc.manager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        teleport.getIntegers().write(0, npc.entityId);
        teleport.getIntegers().write(1, CoordinateUtil.getFixedNumber(location.getX())); // X
        teleport.getIntegers().write(2, CoordinateUtil.getFixedNumber(location.getY())); // Y
        teleport.getIntegers().write(3, CoordinateUtil.getFixedNumber(location.getZ())); // Z
        teleport.getBytes().write(0, CoordinateUtil.getByteAngle(location.getYaw())); // Yaw
        teleport.getBytes().write(1, CoordinateUtil.getByteAngle(location.getPitch())); // Pitch
        teleport.getBooleans().write(0, true);

        PacketContainer rotation = npc.manager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        rotation.getIntegers().write(0, npc.entityId);
        rotation.getBytes().write(0, CoordinateUtil.getByteAngle(npc.location.getYaw()));

        try {
            for(Player p : players) {
                npc.manager.sendServerPacket(p, teleport);
                npc.manager.sendServerPacket(p, rotation);
            }
        } catch(InvocationTargetException e) {
            npc.plugin.getLogger().log(Level.WARNING, "Could not send teleport packet", e);
        }
    }

    public static void equip(NPCImpl npc, int slot, ItemStack item, List<Player> players) {
        PacketContainer equip = npc.manager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        
        String nms = Manager.getNMSVersion();
        
        equip.getIntegers().write(0, npc.entityId);
        
        if (nms.contains("1_8"))
        {
        	
        	equip.getIntegers().write(1, slot);
        	
            equip.getItemModifier().write(0, item);
        	
        }
        
        else if (nms.contains("1_12"))
        {
        	
        	equip.getItemSlots().write(0, ItemSlot.MAINHAND);
        	
        	equip.getItemModifier().write(0, item);
        	
        }
        
        else
        	equip.getSlotStackPairLists().write(0, Collections.singletonList(new Pair<ItemSlot, ItemStack>(ItemSlot.MAINHAND, item)));

        try {
            for(Player p : players) {
                npc.manager.sendServerPacket(p, equip);
            }
        } catch(InvocationTargetException e) {
            npc.plugin.getLogger().log(Level.WARNING, "Could not send equipment packet", e);
        }
        
    }

    public static void animation(NPCImpl npc, AnimationType type, List<Player> players) {
        PacketContainer animation = npc.manager.createPacket(PacketType.Play.Server.ANIMATION);
        animation.getIntegers().write(0, npc.entityId);
        animation.getIntegers().write(1, type.getId(type));

        try {
            for(Player p : players) {
                npc.manager.sendServerPacket(p, animation);
            }
        } catch(InvocationTargetException e) {
            npc.plugin.getLogger().log(Level.WARNING, "Could not send animation packet", e);
        }
    }

    private static WrappedDataWatcher createWatcher1_8(NPCImpl npc)
    {
    	
    	WrappedDataWatcher watcher = new WrappedDataWatcher();

        watcher.setObject(0, (byte) npc.state.getId());
        if(npc.particleEffectColor != -1) {
            watcher.setObject(7, npc.particleEffectColor);
        }
        watcher.setObject(10, npc.skinLayers.getFlags()); // Skin flags

        return watcher;
    }
    
}
