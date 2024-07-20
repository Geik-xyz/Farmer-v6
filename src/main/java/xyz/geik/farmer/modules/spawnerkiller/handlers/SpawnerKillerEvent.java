package xyz.geik.farmer.modules.spawnerkiller.handlers;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.modules.spawnerkiller.SpawnerKiller;

import java.util.List;
import java.util.Random;

/**
 * Spawner Killer Listener
 * @author poyraz
 * @since 1.0.0
 */
public class SpawnerKillerEvent implements Listener {

    /**
     * Constructor of class
     */
    public SpawnerKillerEvent() {
    }

    /**
     * Cancel spawn event if the entity is a jockey or mounted
     * Because jockey and mounted entities can't kill after spawning
     *
     * @param e of creature spawn event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureEvent(@NotNull CreatureSpawnEvent e) {
        Entity en = e.getEntity();
        if (en instanceof Damageable) {
            if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.MOUNT)
                    || e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.JOCKEY))
                e.setCancelled(true);

        }
    }

    /**
     * Spawner spawn event
     * This event is called when a mob is spawned from a spawner
     * @param e of spawner spawn event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawnerSpawnEvent(SpawnerSpawnEvent e) {
        try {
            if (SpawnerKiller.getInstance().isRequireFarmer()) {
                if (!FarmerAPI.getFarmerManager().hasFarmer(e.getLocation()))
                    return;
                Farmer farmer = FarmerManager.getFarmers().get(Main.getIntegration().getRegionID(e.getLocation()));
                if (!farmer.getAttributeStatus("spawnerkiller"))
                    return;
            }

            Entity entity = e.getEntity();

            if (entity instanceof Damageable) {
                EntityType entityType = e.getEntityType();
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
                        List<ItemStack> items = WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity())
                                .getDrops(0);
                        for (ItemStack item : items)
                            e.getEntity().getWorld().dropItemNaturally(e.getLocation(), item);
                    }
                    killCalculator(entity, WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity()).getStackAmount());
                    WildStackerAPI.getStackedEntity((LivingEntity) e.getEntity()).remove();
                    e.getSpawner().setDelay(-1);
                    return;
                }
                ((Damageable) entity).damage(1000.0);
                if (SpawnerKiller.getInstance().isRemoveMob())
                    entity.remove();
                killCalculator(entity, 1);
            }
        }
        catch (Exception ignored) {}

    }

    /**
     * Kill calculator for experience and drops minecraft calculates the experience and drops
     * for mobs and animals when they die with a cause. This is a custom method to calculate
     * because there is no cause to kill them.
     *
     * @param entity of entity
     * @param amount of spawn amount
     */
    public static void killCalculator(@NotNull Entity entity, int amount) {
        EntityType entityType = entity.getType();

        if (entityType == EntityType.OCELOT || entityType == EntityType.CHICKEN || entityType == EntityType.COW
                || entityType == EntityType.HORSE || entityType == EntityType.PIG
                || entityType == EntityType.RABBIT || entityType == EntityType.SHEEP || entityType == EntityType.SQUID
                || entityType == EntityType.WOLF || entityType == EntityType.BAT || entityType.toString().contains("DONKEY")
                || entityType.toString().contains("MULE") || entityType.toString().contains("LLAMA")
                || entityType.toString().contains("MUSHROOM") || entityType.toString().contains("MOOSHROOM")
                || entityType.toString().contains("PARROT") || entityType.toString().contains("POLAR_BEAR")
                || entityType.toString().contains("BEE") || entityType.toString().contains("STRIDER")
                || entityType.toString().contains("COD") || entityType.toString().contains("FOX")
                || entityType.toString().contains("PIGLIN") || entityType.toString().contains("SALMON")
                || entityType.toString().contains("TROPICALFISH") || entityType.toString().contains("SKELETONHORSE")
                || entityType.toString().contains("TURTLE") || entityType.toString().contains("PANDA")
                || entityType.toString().contains("DOLPHIN")) {
            int randomNum = 1 + (int) (Math.random() * 3.0);
            entity.getWorld().spawn(entity.getLocation(), ExperienceOrb.class)
                    .setExperience(randomNum * amount);

        }

        else if (entityType == EntityType.GUARDIAN || entityType == EntityType.BLAZE) {
            entity.getWorld().spawn(entity.getLocation(), ExperienceOrb.class).setExperience(10 * amount);
            for (int i = 0; i < amount; i++)
                if (entityType == EntityType.BLAZE && ((new Random()).nextInt(100)) <= 50)
                    entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.BLAZE_ROD, 1));
        }

        else if (entityType == EntityType.IRON_GOLEM || entityType.toString().contains("SNOW") || entityType.toString().contains("SNOW_GOLEM") || entityType.equals(EntityType.VILLAGER)
                || entityType.toString().contains("WANDERINGTRADER"))
            return;

        else if (entityType == EntityType.SLIME || entityType == EntityType.MAGMA_CUBE)
            entity.getWorld().spawn(entity.getLocation(), ExperienceOrb.class).setExperience(3 * amount);

        else if (entityType.toString().contains("PHANTOM")) {
            for (int i = 0; i < amount; i++)
                if (((new Random()).nextInt(100)) <= 50)
                    entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.PHANTOM_MEMBRANE, 1));
            entity.getWorld().spawn(entity.getLocation(), ExperienceOrb.class).setExperience(5 * amount);
        }
        else if (entityType.toString().contains("SPIDER") || entityType.toString().contains("CAVE")) {
            int nextRand = new Random().nextInt(100);
            for (int i = 0; i < amount; i++) {
                if (nextRand <= 33)
                    entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.STRING, 2));
                else if (nextRand <= 66)
                    entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.STRING, 1));

                if ((new Random()).nextInt(100) <= 20)
                    entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.SPIDER_EYE, 1));
            }
            entity.getWorld().spawn(entity.getLocation(), ExperienceOrb.class).setExperience(5 * amount);
        }
        else
            entity.getWorld().spawn(entity.getLocation(), ExperienceOrb.class).setExperience(5 * amount);
    }
}
