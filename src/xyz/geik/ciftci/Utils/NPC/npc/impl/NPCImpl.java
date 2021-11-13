package xyz.geik.ciftci.Utils.NPC.npc.impl;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import lombok.Getter;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.NPC.npc.*;
import xyz.geik.ciftci.Utils.NPC.skin.NPCTextures;
import xyz.geik.ciftci.Utils.NPC.skin.SkinLayer;
import xyz.geik.ciftci.Utils.NPC.skin.SkinLayerHandler;
import xyz.geik.ciftci.Utils.NPC.util.DistanceUtil;
import xyz.geik.ciftci.Utils.NPC.util.PacketUtil;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCImpl implements NPC {

    public final Main plugin;
    public final ProtocolManager manager;

    @Getter
    public String name;
    @Getter
    public UUID uuid;
    @Getter
    public int entityId;
    @Getter
    public Location location;
    @Getter
    public NPCTextures textures;
    public final SkinLayerHandler skinLayers;
    public final NPCStateHandler state;
    public final NPCInventory inventory;
    public String skin;
    @Getter
    public int particleEffectColor;
    @Getter
    public NPCMode mode;
    @Getter
    public boolean spawned;
    public boolean removed;
    public WrappedGameProfile profile;
    public WrappedSignedProperty property;
    public PlayerInfoData infoData;
    @Getter
    public List<Player> players;
    @Getter
    public List<Player> rangePlayers;
    @Getter
    public List<UUID> offlinePlayers;
    @Getter
    public List<NPCEventHandler> eventHandlers;

    public NPCImpl(Main plugin) {

        this.plugin = plugin;
        this.manager = Main.instance.manager;
        this.skinLayers = new SkinLayerHandler();
        this.state = new NPCStateHandler();
        this.inventory = new NPCInventory(this);
        this.mode = NPCMode.NORMAL;
        this.players = new ArrayList<>();
        this.rangePlayers = new ArrayList<>();
        this.offlinePlayers = new ArrayList<>();
        this.eventHandlers = new ArrayList<>();
        this.skin = Manager.getText("lang", "FarmerSkin");
        
    }

    public void rangePlayersUpdated(Player player) {
    	List<Player> playerList = new ArrayList<>();
    	playerList.add(player);
        if(rangePlayers.contains(player)) {
            PacketUtil.addNPC(this, playerList);
            PacketUtil.showNPC(this, playerList);
        } else {
            PacketUtil.removeNPC(this, playerList);
        }
    }
    
    public void updateProperty()
    {
    	this.property = NPCTextures.toProperty(this.skin);
    }
    public WrappedSignedProperty getProperty()
    {
    	return this.property;
    }

    public void setSkin(String name)
    {
    		
		this.skin = name;
		
    	updateProperty();
    	
    	PacketUtil.removeNPC(this, players);
    	
    	Bukkit.getScheduler().runTaskLater(Main.instance, () ->
    	{
    		
    		for (Player player : rangePlayers)
        		rangePlayersUpdated(player);
    		
    	}, 5L);
    	
    }
    
    public void refreshNPC() {
        refreshNPC(rangePlayers);
    }

    public void refreshNPC(List<Player> players) {
        if(spawned) {
            PacketUtil.removeNPC(this, players);
            PacketUtil.addNPC(this, players);
            PacketUtil.showNPC(this, players);
        }
    }

    public void remove() {
        if(!removed) {
            setSpawned(false);
          //  Main.getNPCs().remove(this);
            removed = true;
        }
    }

    public void checkRange(Player player) {
        if(DistanceUtil.isInRange(player, location) && spawned && !rangePlayers.contains(player)) {
            rangePlayers.add(player);
            rangePlayersUpdated(player);
        }
    }

    public void addEventHandler(NPCEventHandler eventHandler) {
        if(!eventHandlers.contains(eventHandler)) {
            eventHandlers.add(eventHandler);
        }
    }

    public void removeEventHandler(NPCEventHandler eventHandler) {
        eventHandlers.remove(eventHandler);
    }

    public void addPlayer(Player player) {
        if(!players.contains(player)) {
            players.add(player);
            checkRange(player);
        }
    }
    
    public void addOfflinePlayer(UUID uuid) {
        if(!offlinePlayers.contains(uuid)) {
        	offlinePlayers.add(uuid);
        }
    }

    public void removePlayer(Player player) {
        if(players.contains(player)) {
            players.remove(player);
            rangePlayers.remove(player);
            offlinePlayers.remove(player.getUniqueId());
            if(player.isOnline()) {
                rangePlayersUpdated(player);
            }
        }
    }

    public void playAnimation(AnimationType type) {
        if(spawned) {
            PacketUtil.animation(this, type, rangePlayers);
        }
    }

    public void setParticleEffectColor(int hex) {
        particleEffectColor = hex;
        if(spawned) {
            PacketUtil.updateMetadata(this, rangePlayers);
        }
    }

    public boolean isSkinLayerVisible(SkinLayer layer) {
        return skinLayers.isVisible(layer);
    }

    public void setSkinLayerVisible(SkinLayer layer, boolean visible) {
        skinLayers.setLayer(layer, visible);
        if(spawned) {
            PacketUtil.updateMetadata(this, rangePlayers);
        }
    }

    public ItemStack getEquipmentSlot(NPCInventory.EquipmentSlot slot) {
        return inventory.get(slot);
    }

    public void setEquipmentSlot(NPCInventory.EquipmentSlot slot, ItemStack item) {
        inventory.set(slot, item);
    }

    public boolean isSneaking() {
        return state.isSneaking();
    }

    public void setSneaking(boolean sneaking) {
        state.setSneaking(sneaking);
        if(spawned) {
            PacketUtil.updateMetadata(this, rangePlayers);
        }
    }

    public boolean isOnFire() {
        return state.isOnFire();
    }

    public void setOnFire(boolean fire) {
        state.setOnFire(fire);
        if(spawned) {
            PacketUtil.updateMetadata(this, rangePlayers);
        }
    }

    public void setMode(NPCMode mode) {
        this.mode = mode;
        if(spawned) {
            refreshNPC();
        }
    }

    public void setSpawned(boolean spawned) {
        if(removed) {
            throw new IllegalStateException("NPC is removed");
        }
        if(spawned && !this.spawned) {
        	rangePlayers.clear();
            for(Player player : players)
                if(DistanceUtil.isInRange(player, location))
                    rangePlayers.add(player);
                else
                    rangePlayers.remove(player);
            
            PacketUtil.addNPC(this, rangePlayers);
            PacketUtil.showNPC(this, rangePlayers);
            this.spawned = true;
        } else if(!spawned && this.spawned) {
            PacketUtil.removeNPC(this, rangePlayers);
            this.spawned = false;
        }
    }

    public void setName(String name) {
        if(name.equals(this.name)) {
            return;
        }
        if(name.length() > 16) {
            throw new IllegalArgumentException("NPC name cannot be longer than 16 characters");
        }
        this.name = name;
        if(spawned) {
            refreshNPC();
        }
    }

    public void setUuid(UUID uuid) {
        if(spawned) {
            setSpawned(false);
            this.uuid = uuid;
            setSpawned(true);
        } else {
            this.uuid = uuid;
        }
    }

    public void setEntityId(int entityId) {
        if(spawned) {
            setSpawned(false);
            this.entityId = entityId;
            setSpawned(true);
        } else {
            this.entityId = entityId;
        }
    }

    public void setLocation(Location location) {
        this.location = location;
        if(spawned) {
            PacketUtil.teleport(this, location, rangePlayers);
            List<Player> list = new ArrayList<>(rangePlayers);
            for (Player player : list) {
                if(!DistanceUtil.isInRange(player, location)) {
                    rangePlayers.remove(player);
                    rangePlayersUpdated(player);
                }
            }
        }
    }

    public void setTextures(NPCTextures textures) {
        this.textures = textures;
        if(spawned) {
            refreshNPC();
        }
    }
    
    public String getSkin() {
    	return skin;
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public UUID getUuid() {
		// TODO Auto-generated method stub
		return uuid;
	}

	@Override
	public int getEntityId() {
		// TODO Auto-generated method stub
		return entityId;
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public NPCTextures getTextures() {
		// TODO Auto-generated method stub
		return textures;
	}

	@Override
	public NPCMode getMode() {
		// TODO Auto-generated method stub
		return mode;
	}

	@Override
	public int getParticleEffectColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSpawned() {
		// TODO Auto-generated method stub
		return spawned;
	}

	@Override
	public List<Player> getPlayers() {
		// TODO Auto-generated method stub
		return players;
	}

	@Override
	public List<Player> getRangePlayers() {
		// TODO Auto-generated method stub
		return rangePlayers;
	}

	@Override
	public List<UUID> getOfflinePlayers() {
		// TODO Auto-generated method stub
		return offlinePlayers;
	}

	@Override
	public List<NPCEventHandler> getEventHandlers() {
		// TODO Auto-generated method stub
		return eventHandlers;
	}
}
