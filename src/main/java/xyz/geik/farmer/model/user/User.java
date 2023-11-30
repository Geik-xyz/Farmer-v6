package xyz.geik.farmer.model.user;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.Farmer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * User Object which farmer has
 */
@Setter
@Getter
public class User {

    // User belongs to which farmer it attaches by this unique key
    private int farmerId;

    // UUID of player
    private UUID uuid;

    // Permission of user can be FarmerPerm#COOP, FarmerPerm#MEMBER, FarmerPerm#OWNER
    private FarmerPerm perm;

    // Name of player
    private String name;

    /**
     * Constructor of User
     *
     * @param farmerId
     * @param name
     * @param uuid
     * @param perm
     */
    public User(int farmerId, String name, UUID uuid, FarmerPerm perm) {
        this.farmerId = farmerId;
        this.name = name;
        this.uuid = uuid;
        this.perm = perm;
    }

    /**
     * Change role of user coop to member or member to coop.
     * Synchronized method because this can be issue if owner spam role changes.
     *
     * @param user
     * @param farmer
     * @return
     */
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

    /**
     * Updates player role on database created for #updateUserRole but can be required
     * in another class if necessary.
     *
     * @param uuid
     * @param roleId
     * @param farmerId
     */
    public static void updateRole(UUID uuid, int roleId, int farmerId) {
        Main.getInstance().getSql().updateRole(uuid, roleId, farmerId);
    }


    /**
     * How many user can player add to farmer.
     *
     * @param player
     * @return
     */
    public static int getUserAmount(Player player) {
        String permissionPrefix = "farmer.user.";
        int defaultValue =  3;
        try {
            defaultValue = Main.getConfigFile().getSettings().getDefaultMaxFarmerUser();
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
