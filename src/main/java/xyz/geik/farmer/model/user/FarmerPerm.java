package xyz.geik.farmer.model.user;

public enum FarmerPerm {
    COOP,
    MEMBER,
    OWNER;

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

    public static int getRoleId(FarmerPerm perm) {
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
