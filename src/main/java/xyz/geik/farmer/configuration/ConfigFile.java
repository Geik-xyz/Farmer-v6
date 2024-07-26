package xyz.geik.farmer.configuration;

import lombok.Getter;
import lombok.Setter;
import xyz.geik.glib.shades.okaeri.configs.OkaeriConfig;
import xyz.geik.glib.shades.okaeri.configs.annotation.Comment;
import xyz.geik.glib.shades.okaeri.configs.annotation.NameModifier;
import xyz.geik.glib.shades.okaeri.configs.annotation.NameStrategy;
import xyz.geik.glib.shades.okaeri.configs.annotation.Names;

import java.util.Arrays;
import java.util.List;

/**
 * Main config file
 * @author geik
 * @since 2.0
 */
@Getter
@Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class ConfigFile extends OkaeriConfig {

    /**
     * Settings menu of config
     */
    @Comment("Main settings")
    private Settings settings = new Settings();

    /**
     * Settings configuration of config
     *
     * @author geik
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Settings extends OkaeriConfig {
        @Comment("Language of plugin")
        private String lang = "en";

        @Comment("if you want to give farmer with economy leave it true")
        private boolean buyFarmer = true;

        @Comment("price of farmer necessary if buyFarmer is true")
        private double farmerPrice = 1000.0;

        @Comment("crates farmer automatically (If plugin supports) also bypass money requirement")
        private boolean autoCreateFarmer = false;

        @Comment("you can give farmer.user.<amount> perm to owner of farmer")
        private int defaultMaxFarmerUser = 3;

        @Comment("auto or vault, royaleconomy, playerpoints, gringotts, elementalgems")
        private String economy = "auto";

        @Comment("farmer ignore collecting if item dropped by player")
        private boolean ignorePlayerDrop = false;

        @Comment("Allowed worlds. You can also use worlds like 'island_*' to allow all worlds that contains 'island_' prefix.")
        private List<String> allowedWorlds = Arrays.asList("ASkyBlock", "Island", "SuperiorWorld", "bskyblock_world", "island_normal_world", "island_*");

        @Comment("Unlocks * character for world name seeker feature")
        private boolean AllowedWorldsStarCharFeature = false;

        @Comment("Default role which will be assigned to joined player (Options: COOP - MEMBER)")
        private String defaultJoinRole = "COOP";
    }

    /**
     * Settings menu of config
     */
    @Comment("Tax settings")
    private Tax tax = new Tax();

    /**
     * Tax settings configuration
     *
     * @author geik
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Tax extends OkaeriConfig {
        @Comment("default rate of tax")
        private double rate = 20.0;

        @Comment("is tax amount will be deposit to a account")
        private boolean deposit = false;

        @Comment("if deposit settings true then deposits the tax to this user")
        private String depositUser = "Geyik";
    }

    /**
     * Gui's configuration holder
     */
    private Gui gui = new Gui();

    /**
     * Gui configuration class
     * @author geik
     * @since 2.0
     */
    @Getter @Setter
    public static class Gui extends OkaeriConfig {

        @Comment({"Layout for gui placement",
                "m -> Management Panel item",
                "g -> Item Group element item",
                "p -> Previous Page item",
                "n -> Next Page item",
                "h -> Help item"
        })
        private List<String> farmerLayout = Arrays.asList(
                "    m    ",
                " ggggggg ",
                " ggggggg ",
                " ggggggg ",
                " ggggggg ",
                "p   h   n"
        );

        @Comment({"Layout for gui placement",
                "t -> taking situation icon",
                "l -> level up icon",
                "u -> user management icon"
        })
        private List<String> manageLayout = Arrays.asList(
                "    m    ",
                " t  l  u ",
                "         "
        );

        @Comment({"Layout for gui placement",
                "b -> buy farmer icon"
        })
        private List<String> buyFarmerLayout = Arrays.asList(
                "         ",
                "    b    ",
                "         "
        );

        @Comment({"Layout for gui placement",
                "u -> User head",
                "a -> Add user icon",
                "p -> Previous Page item",
                "n -> Next Page item",
                "h -> Help item"
        })
        private List<String> usersLayout = Arrays.asList(
                "    h    ",
                "uuuuuuuuu",
                "uuuuuuuuu",
                "p   a   n"
        );

        @Comment({"Layout for gui placement",
                "l -> Left click icon",
                "r -> Right click icon",
                "s -> Shift Right click icon"
        })
        private List<String> geyserLayout = Arrays.asList(
                "         ",
                " l  r  s ",
                "         "
        );

        @Comment({"Layout for gui placement",
                "s -> AutoSell",
                "k -> SpwanerKiller",
                "h -> AutoHarvest"
        })
        private List<String> moduleLayout = Arrays.asList(
                "         ",
                " s  k  h ",
                "         "
        );


        private GlobalItems globalItems = new GlobalItems();
        /**
         * GlobalItems settings
         */
        @Getter
        @Setter
        public static class GlobalItems extends OkaeriConfig {

            private FillerItem fillerItem = new FillerItem();
            /**
             * FillerItem settings
             */
            @Getter
            @Setter
            public static class FillerItem extends OkaeriConfig {
                private boolean useFiller = true;
                private String material = "GRAY_STAINED_GLASS_PANE";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private NextPage nextPage = new NextPage();
            /**
             * NextPage settings
             */
            @Getter
            @Setter
            public static class NextPage extends OkaeriConfig {
                private String material = "ARROW";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private PreviousPage previousPage = new PreviousPage();
            /**
             * PreviousPage settings
             */
            @Getter
            @Setter
            public static class PreviousPage extends OkaeriConfig {
                private String material = "ARROW";
                private int modelData = 0;
                boolean hasGlow = false;
            }
        }

        private FarmerGuiItems farmerGuiItems = new FarmerGuiItems();
        /**
         * FarmerGuiItems settings
         */
        @Getter
        @Setter
        public static class FarmerGuiItems extends OkaeriConfig {
            private Manage manage = new Manage();
            /**
             * Manage item settings
             */
            @Getter
            @Setter
            public static class Manage extends OkaeriConfig {
                private String material = "ewogICJ0aW1lc3RhbXAiIDogMTYyMDM5NzA2MjE1MSwKICAicHJvZmlsZUlkIiA6ICI0ZGI2MWRkOTM0Mzk0M2M0YjhhOTZiNDQwMWM3MDM1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZWVyYmVsbHltYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZTgxOTkwNmViMTc5NDM5YjhkZDU1NTExMzJlNTRlYjQ3MTczZTBmNDU4ODYxYWQyYThjOTM3OTE4Mzg5MSIKICAgIH0KICB9Cn0=";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private Help help = new Help();
            /**
             * Help item settings
             */
            @Getter
            @Setter
            public static class Help extends OkaeriConfig {
                private String material = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE2Y2M1NzU1Y2RkMjYwZjdiNGI1YzFhMWYxZjNiZDMxODUxZmMxZDk4Yjc0NDM3YjJmYjRiZDZlYjhkMiJ9fX0=";
                private int modelData = 0;
                boolean hasGlow = false;
            }
        }

        private ManageGuiItems manageGuiItems = new ManageGuiItems();
        /**
         * ManageGuiItems settings
         */
        @Getter
        @Setter
        public static class ManageGuiItems extends OkaeriConfig {
            private UpgradeNext upgradeNext = new UpgradeNext();
            /**
             * UpgradeNext item settings
             */
            @Getter
            @Setter
            public static class UpgradeNext extends OkaeriConfig {
                private String material = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjNmVjM2I3NTM1NGI0OTIyMmE4OWM2NjNjNGFjYWQ1MjY0ZmI5NzdjYWUyNmYwYjU0ODNhNTk5YzQ2NCJ9fX0=";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private MaxLevel maxLevel = new MaxLevel();
            /**
             * MaxLevel item settings
             */
            @Getter
            @Setter
            public static class MaxLevel extends OkaeriConfig {
                private String material = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ3OGNjMzkxYWZmYjgwYjJiMzVlYjczNjRmZjc2MmQzODQyNGMwN2U3MjRiOTkzOTZkZWU5MjFmYmJjOWNmIn19fQ==";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private CloseFarmer closeFarmer = new CloseFarmer();
            /**
             * CloseFarmer item settings
             */
            @Getter
            @Setter
            public static class CloseFarmer extends OkaeriConfig {
                private String material = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private Users users = new Users();
            /**
             * Users item settings
             */
            @Getter
            @Setter
            public static class Users extends OkaeriConfig {
                private String material = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjg1NDA2MGFhNTc3NmI3MzY2OGM4OTg2NTkwOWQxMmQwNjIyNDgzZTYwMGI2NDZmOTBjMTg2YzY1Yjc1ZmY0NSJ9fX0=";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private Modules modules = new Modules();
            /**
             * Modules item settings
             */
            @Getter
            @Setter
            public static class Modules extends OkaeriConfig {
                private String material = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZlZmM4NmRiOTIyMTdjNWEzODk2NzJiMjgyNDI3NWU3YTIwNmQ3ZWMwZjJjN2U0Y2E0ODNjNmUxN2M5ZjZkNSJ9fX0=";
                private int modelData = 0;
                boolean hasGlow = false;
            }
        }

        private BuyGuiItems buyGuiItems = new BuyGuiItems();
        /**
         * BuyGuiItems settings
         */
        @Getter
        @Setter
        public static class BuyGuiItems extends OkaeriConfig {
            private BuyItem buyItem = new BuyItem();
            /**
             * BuyItem item settings
             */
            @Getter
            @Setter
            public static class BuyItem extends OkaeriConfig {
                private String material = "ewogICJ0aW1lc3RhbXAiIDogMTYyMDM5NzA2MjE1MSwKICAicHJvZmlsZUlkIiA6ICI0ZGI2MWRkOTM0Mzk0M2M0YjhhOTZiNDQwMWM3MDM1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZWVyYmVsbHltYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZTgxOTkwNmViMTc5NDM5YjhkZDU1NTExMzJlNTRlYjQ3MTczZTBmNDU4ODYxYWQyYThjOTM3OTE4Mzg5MSIKICAgIH0KICB9Cn0=";
                private int modelData = 0;
                boolean hasGlow = false;
            }
        }

        private UsersGuiItems usersGuiItems = new UsersGuiItems();
        /**
         * UsersGuiItems settings
         */
        @Getter
        @Setter
        public static class UsersGuiItems extends OkaeriConfig {

            private AddUser addUser = new AddUser();
            /**
             * AddUser item settings
             */
            @Getter
            @Setter
            public static class AddUser extends OkaeriConfig {
                private String material = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private Help help = new Help();
            /**
             * Help item settings
             */
            @Getter
            @Setter
            public static class Help extends OkaeriConfig {
                private String material = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE2Y2M1NzU1Y2RkMjYwZjdiNGI1YzFhMWYxZjNiZDMxODUxZmMxZDk4Yjc0NDM3YjJmYjRiZDZlYjhkMiJ9fX0=";
                private int modelData = 0;
                boolean hasGlow = false;
            }
        }


        private GeyserGuiItems geyserGuiItems = new GeyserGuiItems();
        /**
         * GeyserGuiItems settings
         */
        @Getter
        @Setter
        public static class GeyserGuiItems extends OkaeriConfig {

            private LeftClick leftClick = new LeftClick();
            /**
             * LeftClick item settings
             */
            @Getter
            @Setter
            public static class LeftClick extends OkaeriConfig {
                private String material = "DIAMOND";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private RightClick rightClick = new RightClick();
            /**
             * RightClick item settings
             */
            @Getter
            @Setter
            public static class RightClick extends OkaeriConfig {
                private String material = "DIAMOND";
                private int modelData = 0;
                boolean hasGlow = false;
            }

            private ShiftRightClick shiftRightClick = new ShiftRightClick();
            /**
             * ShiftRightClick item settings
             */
            @Getter
            @Setter
            public static class ShiftRightClick extends OkaeriConfig {
                private String material = "DIAMOND";
                private int modelData = 0;
                boolean hasGlow = false;
            }
        }
    }

    @Comment({"If you don't know about database settings", "please don't change here. Leave it SQLite"})
    private Database database = new Database();

    /**
     * Database configuration settings
     * @author geik
     * @since 2.0
     */
    @Getter @Setter
    public static class Database extends OkaeriConfig {
        private String databaseType = "SQLite";
        private String host = "localhost";
        private String port = "3306";
        private String tableName = "farmer_db";
        private String userName = "farmer";
        private String password = "supersecretpassword";
    }
}
