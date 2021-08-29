package xyz.geik.ciftci.Utils.NPC.npc;

import xyz.geik.ciftci.Utils.NPC.skin.NPCTextures;
import xyz.geik.ciftci.Utils.NPC.skin.SkinLayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface NPC {

    String getName();
    void setName(String name);

    UUID getUuid();
    void setUuid(UUID uuid);

    int getEntityId();
    void setEntityId(int entityId);

    Location getLocation();
    void setLocation(Location location);

    NPCTextures getTextures();
    void setTextures(NPCTextures textures);

    ItemStack getEquipmentSlot(NPCInventory.EquipmentSlot slot);
    void setEquipmentSlot(NPCInventory.EquipmentSlot slot, ItemStack item);

    NPCMode getMode();
    void setMode(NPCMode mode);

    int getParticleEffectColor();
    void setParticleEffectColor(int hex);

    boolean isSpawned();
    void setSpawned(boolean spawned);

    List<Player> getPlayers();
    List<Player> getRangePlayers();
    List<UUID> getOfflinePlayers();

    List<NPCEventHandler> getEventHandlers();
    void addEventHandler(NPCEventHandler eventHandler);
    void removeEventHandler(NPCEventHandler eventHandler);

    void addPlayer(Player player);
    void addOfflinePlayer(UUID uuid);
    void removePlayer(Player player);

    boolean isSkinLayerVisible(SkinLayer layer);
    void setSkinLayerVisible(SkinLayer layer, boolean visible);
    boolean isSneaking();
    void setSneaking(boolean sneaking);
    boolean isOnFire();
    void setOnFire(boolean fire);

    void remove();
    void playAnimation(AnimationType type);
}
