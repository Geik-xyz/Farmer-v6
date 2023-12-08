package xyz.geik.farmer.modules.spawnerkiller.old;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import mc.rellox.spawnermeta.SpawnerMeta;
import mc.rellox.spawnermeta.api.APIInstance;
import mc.rellox.spawnermeta.api.events.SpawnerPostSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;

import java.util.List;

/**
 * @since b003
 */
public class SpawnerMetaEvent {

    /**
     * SpawnerMeta spawner event
     * @author amownyy
     */
    SpawnerMetaEvent() {
        if (Bukkit.getPluginManager().getPlugin("SpawnerMeta") == null)
            return;

        SpawnerMeta sm = (SpawnerMeta) Bukkit.getPluginManager().getPlugin("SpawnerMeta");

        APIInstance api = sm.getAPI();

        api.register(SpawnerPostSpawnEvent.class, e -> {
            try {
                if (SpawnerKiller.getInstance().isRequireFarmer()) {
                    if (!FarmerAPI.getFarmerManager().hasFarmer(e.getSpawner().center()))
                        return;
                    Farmer farmer = FarmerManager.getFarmers().get(Main.getIntegration().getRegionID(e.getSpawner().center()));
                    if (!farmer.getAttributeStatus("spawnerkiller"))
                        return;
                }

                Entity entity = e.entities.get(0);

                if (entity instanceof Damageable) {
                    EntityType entityType = e.getSpawner().getType().entity();
                    if (!SpawnerKiller.getInstance().getWhitelist().isEmpty()
                            && !SpawnerKiller.getInstance().getWhitelist().contains(entityType.toString()))
                        return;
                    if (!SpawnerKiller.getInstance().getBlacklist().isEmpty()
                            && SpawnerKiller.getInstance().getBlacklist().contains(entityType.toString()))
                        return;

                    if (SpawnerKiller.getInstance().isCookFoods())
                        entity.setFireTicks(20);

                    if (Bukkit.getPluginManager().getPlugin("WildStacker") != null) {
                        if (!entityType.equals(EntityType.BLAZE)) {
                            List<ItemStack> items = WildStackerAPI.getStackedEntity((LivingEntity) e.entities.get(0))
                                    .getDrops(0);
                            for (ItemStack item : items)
                                e.entities.get(0).getWorld().dropItemNaturally(e.getSpawner().center(), item);
                        }
                        SpawnerKillerEvent.killCalculator(entity, WildStackerAPI.getStackedEntity((LivingEntity) e.entities.get(0)).getStackAmount());
                        WildStackerAPI.getStackedEntity((LivingEntity) e.entities.get(0)).remove();
                        e.getSpawner().setDelay(-1);
                        return;
                    }
                    ((Damageable) entity).damage(1000.0);
                    if (SpawnerKiller.getInstance().isRemoveMob())
                        entity.remove();
                    SpawnerKillerEvent.killCalculator(entity, 1);
                }
            }
            catch (Exception ignored) {}
        });
    }
}
