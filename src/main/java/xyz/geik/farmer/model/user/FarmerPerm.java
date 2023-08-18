package xyz.geik.farmer.model.user;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;

/**
 * Enum class of farmer permission of user
 */
public enum FarmerPerm {
    /**
     * COOP perm of farmer
     */
    COOP,
    /**
     * MEMBER perm of player
     */
    MEMBER,
    /**
     * OWNER perm of farmer
     */
    OWNER;

    /**
     * Gets name of role
     *
     * @return name of role
     */
    public String getName() {
        return Main.getLangFile().getText("roles." + this.name().toLowerCase());
    }

    /**
     * Gets role of farmer by int id
     *
     * @param id of role
     * @return FarmerPerm object
     */
    public static FarmerPerm getRole(int id) {
        switch (id) {
            case 1:
                return FarmerPerm.MEMBER;
            case 2:
                return FarmerPerm.OWNER;
            default:
                return FarmerPerm.COOP;
        }
    }

    /**
     * Gets int id of role
     * @param perm of farmer
     * @return int of FarmerPerm object
     */
    @Contract(pure = true)
    public static int getRoleId(@NotNull FarmerPerm perm) {
        switch (perm) {
            case MEMBER:
                return 1;
            case OWNER:
                return 2;
            default:
                return 0;
        }
    }
}
