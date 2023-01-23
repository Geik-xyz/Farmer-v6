package xyz.geik.farmer.model.user;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.database.DBConnection;
import xyz.geik.farmer.model.Farmer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

@Setter
@Getter
public class User {

    private int farmerId;
    private UUID uuid;
    private FarmerPerm perm;

    private String name;

    public User(int farmerId, String name, UUID uuid, FarmerPerm perm) {
        this.farmerId = farmerId;
        this.name = name;
        this.uuid = uuid;
        this.perm = perm;
    }

    public static synchronized boolean updateUserRole(User user, Farmer farmer) {
        if (user.getPerm().equals(FarmerPerm.COOP)) {
            user.setPerm(FarmerPerm.MEMBER);
            updateRole(user.getUuid(), 1, farmer.getId());
            return true;
        }
        else if (user.getPerm().equals(FarmerPerm.MEMBER)) {
            user.setPerm(FarmerPerm.COOP);
            updateRole(user.getUuid(), 0, farmer.getId());
            return true;
        }
        else return false;
    }

    public static void updateRole(UUID uuid, int roleId, int farmerId) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            final String QUERY = "UPDATE FarmerUsers SET role = ? WHERE uuid = ? AND farmerId = ?";
            try (Connection con = DBConnection.connect()) {
                PreparedStatement statement = con.prepareStatement(QUERY);
                statement.setInt(1, roleId);
                statement.setString(2, uuid.toString());
                statement.setInt(3, farmerId);
                statement.executeUpdate();
                statement.close();
            }
            catch (Exception e) {}
        });
    }

    public static int getUserAmount(Player player) {
        String permissionPrefix = "farmer.user.";
        int defaultValue =  3;
        try {
            defaultValue = Main.getConfigFile().getInt("settings.defaultMaxFarmerUser");
            if (player == null)
                return defaultValue;
            else {
                List<Integer> lists = new ArrayList<Integer>();
                for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
                    if (attachmentInfo.getPermission().startsWith(permissionPrefix))
                        lists.add(Integer.parseInt(attachmentInfo.getPermission().substring(attachmentInfo.getPermission().lastIndexOf(".") +1)));
                }
                if (!lists.isEmpty())
                    defaultValue = lists.stream()
                            .filter(User::isInteger)
                            .reduce(1, Integer::max);
            }
            return defaultValue;
        }

        catch(Exception e1) {
            return defaultValue;
        }
    }

    private static boolean isInteger(int input) {
        try {
            return input > 0;
        } catch(NumberFormatException exception) {
            return false;
        }
    }
}
