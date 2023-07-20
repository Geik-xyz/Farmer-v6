package xyz.geik.farmer.integrations.townyadvanced;

import com.palmergames.bukkit.towny.TownyAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.geik.farmer.integrations.Integrations;

import java.util.UUID;

public class TownyAdvanced extends Integrations {
    public TownyAdvanced() {
        super(new TownyListener());
    }

    @Override
    public UUID getOwnerUUID(String regionID) {
        return TownyAPI.getInstance().getTown(UUID.fromString(regionID)).getMayor().getUUID();
    }

    @Override
    public UUID getOwnerUUID(Location location) {
        return TownyAPI.getInstance().getTown(location).getMayor().getUUID();
    }

    @Override
    public String getRegionID(Location location) {
        if (TownyAPI.getInstance().getTown(location) == null)
            return null;
        return TownyAPI.getInstance().getTown(location).getUUID().toString();
    }

    @Override
    public String getRegionIDWithPlayer(Player player) {
        return TownyAPI.getInstance().getTown(player).getUUID().toString();
    }
}
