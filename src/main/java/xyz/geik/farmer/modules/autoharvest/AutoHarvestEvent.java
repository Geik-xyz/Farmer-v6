package xyz.geik.farmer.modules.autoharvest;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.inventory.FarmerItem;

public class AutoHarvestEvent implements Listener {

    @EventHandler
    public void onHarvestGrowEvent(BlockGrowEvent event) {
        Block block = event.getNewState().getBlock();
        // Checks world suitable for farmer
        if (!Settings.allowedWorlds.contains(block.getLocation().getWorld().getName()))
            return;

        // Checks auto harvest, harvests this block.
        if (!AutoHarvest.checkCrop(event.getNewState()))
            return;

        if (!AutoHarvest.getInstance().isWithoutFarmer()) {
            // Checks item dropped in region of a player
            // And checks region owner has a farmer
            String regionID = Main.getIntegration().getRegionID(block.getLocation());
            if (regionID == null || !FarmerAPI.getFarmerManager().getFarmers().containsKey(regionID))
                return;

            Farmer farmer = FarmerAPI.getFarmerManager().getFarmers().get(regionID);

            // Checks farmer can auto harvest
            if (!FarmerAPI.getModuleManager().getAttributeStatus("autoharvest", farmer))
                return;

            if (!hasStock(farmer, XMaterial.matchXMaterial(event.getNewState().getType()))) {
                event.setCancelled(true);
                return;
            }
        }

        // Checks piston
        if (AutoHarvest.getInstance().isRequirePiston()) {
            if (!pistonCheck(event.getBlock().getLocation()))
                return;
        }

        XMaterial material = XMaterial.matchXMaterial(event.getNewState().getType());
        if (harvestCrops(event.getNewState(), material))
            return;

        else if (harvestCocoa(event.getNewState(), material))
            return;

        else if (harvestBlocks(event.getNewState(), material))
            return;

        else return;
    }

    private boolean hasStock(Farmer farmer, XMaterial material) {
        if (AutoHarvest.getInstance().isCheckStock()) {
            long capacity = farmer.getInv().getCapacity();
            material = material == XMaterial.valueOf("COCOA") ? XMaterial.valueOf("COCOA_BEANS")
                    : material == XMaterial.valueOf("MELON") ? XMaterial.valueOf("MELON_SLICE") : material;
            FarmerItem item = farmer.getInv()
                    .getStockedItem(material == XMaterial.valueOf("COCOA") ? XMaterial.valueOf("COCOA_BEANS") : material);
            if (item.getAmount() == capacity)
                return false;
        }
        return true;
    }

    /**
     * TODO Description
     *
     * @param state
     * @param material
     * @return
     */
    private boolean harvestBlocks(BlockState state, @NotNull XMaterial material) {
        if (isBlockHarvestable(material)) {
            ItemStack item = material.parseItem();
            if (material.equals(XMaterial.valueOf("MELON"))) {
                item = XMaterial.valueOf("MELON_SLICE").parseItem();
                item.setAmount(4);
            }
            state.getWorld().dropItemNaturally(state.getLocation(), item);
            state.setType(Material.AIR);
            return true;
        }
        else return false;
    }

    private boolean isBlockHarvestable(XMaterial material) {
        return material.equals(XMaterial.valueOf("SUGAR_CANE"))
                || material.equals(XMaterial.valueOf("MELON"))
                || material.equals(XMaterial.valueOf("PUMPKIN"))
                || material.equals(XMaterial.valueOf("CHORUS_FLOWER"))
                || material.equals(XMaterial.valueOf("CHORUS_PLANT"));
    }

    private boolean isCropsHarvestable(XMaterial material) {
        return material.equals(XMaterial.valueOf("WHEAT"))
                || material.equals(XMaterial.valueOf("CARROTS"))
                || material.equals(XMaterial.valueOf("POTATOES"))
                || material.equals(XMaterial.valueOf("BEETROOTS"))
                || material.equals(XMaterial.valueOf("SWEET_BERRY_BUSH"))
                || material.equals(XMaterial.valueOf("NETHER_WART"));
    }

    /**
     * TODO Description
     *
     * @param state
     * @param material
     * @return
     */
    private boolean harvestCrops(@NotNull BlockState state, @NotNull XMaterial material) {
        if (isCropsHarvestable(material)) {
            MaterialData data = state.getData();
            // Other crops
            if (data.getData() == 7
                    || (material.equals(XMaterial.valueOf("NETHER_WART")) && data.getData() == 3)) {
                ItemStack item = material.parseItem();
                // Checks if stock is not full then drops item
                // item of crop
                state.getWorld().dropItemNaturally(state.getLocation(), item);
                // Item of seed
                state.getBlock().getDrops().forEach(seed -> state.getWorld().dropItemNaturally(state.getLocation(), seed));
                // Makes crop to be zero age
                data.setData((byte) 0);
                state.setData(data);
            }
            return true;
        }
        else return false;
    }

    /**
     * Used in harvestCrops for item drops and age of crop
     * @param state
     */
    private boolean harvestCocoa(BlockState state, XMaterial material) {
        if (material.equals(XMaterial.valueOf("COCOA"))) {
            CocoaPlant data = (CocoaPlant) state.getData();
            if (data.getSize().equals(CocoaPlant.CocoaPlantSize.LARGE)) {
                ItemStack item = XMaterial.valueOf("COCOA_BEANS").parseItem();
                item.setAmount(3);
                state.getWorld().dropItemNaturally(state.getLocation(), item);

                data.setSize(CocoaPlant.CocoaPlantSize.SMALL);
                state.setRawData(data.getData());
            }
            return true;
        }
        return false;
    }

    /**
     * TODO Description
     *
     * @param location
     * @return
     */
    private boolean pistonCheck(@NotNull Location location) {
        if (AutoHarvest.getInstance().isCheckAllDirections()) {
            Location loc1 = location.clone().add(-1, 0, 0);
            Location loc2 = location.clone().add(1, 0, 0);
            Location loc3 = location.clone().add(0, 0, -1);
            Location loc4 = location.clone().add(0, 0, 1);
            if (loc1.getBlock().getType().name().contains("PISTON"))
                return true;
            else if (loc2.getBlock().getType().name().contains("PISTON"))
                return true;
            else if (loc3.getBlock().getType().name().contains("PISTON"))
                return true;
            else if (loc4.getBlock().getType().name().contains("PISTON"))
                return true;
        }
        Location loc = location.clone().add(0, 1, 0);
        if (loc.getBlock().getType().name().contains("PISTON"))
            return true;
        else
            return false;
    }
}
