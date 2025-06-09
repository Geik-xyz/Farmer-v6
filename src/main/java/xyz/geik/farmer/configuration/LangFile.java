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
 * LangFile
 * @author geik
 * @since 1.0
 */
@Getter
@Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class LangFile extends OkaeriConfig {

    /**
     * Settings menu of config
     */
    @Comment("Chat messages")
    private Messages messages = new Messages();

    /**
     * Messages of plugin
     *
     * @author geik
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Messages extends OkaeriConfig {

        @Comment("Prefix of messages")
        private String prefix = "&3Farmer &8»";

        @Comment("placeholders: {money} money which deposited to player {tax} tax amount.")
        private String sellComplate = "{prefix} &aItems sold. &6Profit: &e{money}&f, &6Tax: &e{tax}";
        private String wrongWorld = "{prefix} &cYou cannot do this in this world.";
        private String noRegion = "{prefix} &cThere is no region for bound a farmer.";
        private String removedFarmer = "{prefix} &aRemoved farmer successfully.";
        private String noFarmer = "{prefix} &cThere is no farmer bound here.";
        private String mustBeOwner = "{prefix} &cYou must to be Region Owner for this.";
        private String inventoryFull = "{prefix} &cInventory full!";
        @Comment("placeholders: {money} players money {req_money} required money.")
        private String notEnoughMoney = "{prefix} &cDon't have enough money! Required: &4{req_money}";
        @Comment("placeholders: {level} new upgraded level {capacity} new upgraded capacity.")
        private String levelUpgraded = "{prefix} &aFarmer upgraded to &6{level}&a level. &2New Capacity: &e{capacity}";
        @Comment("placeholders: {status} shows status of farmer status. (#toggledON, #toggledOFF)")
        private String toggleFarmer = "{prefix} &aFarmers collection settings changed to: &e{status}";
        private String featureDisabled = "{prefix} &cThis feature disallowed.";
        private String reloadSuccess = "{prefix} &aConfig reloaded successfully. It took %ms%";
        private String boughtFarmer = "{prefix} &aFarmer bought successfully.";
        private String inCooldown = "{prefix} &cYou should wait {time}s for do it again.";
        private String waitingInput = "{prefix} &aType input to chat in 6sec and type &c{cancel} &afor cancel.";
        private String notOwner = "{prefix} &cYou must be the owner of the region to use this command.";
        private String inputCancel = "{prefix} &cNo longer waiting for input.";
        private String userAdded = "{prefix} &aAdded successfully.";
        private String userAlreadyExist = "{prefix} &4{player} &cAlready added.";
        private String userCouldntFound = "{prefix} &cUser has not played before!";
        private String reachedMaxUser = "{prefix} &cYou have reached max user capacity.";
        private String playerNotOnline = "{prefix} &cTarget player is not online.";
        private String playerNotAvailable = "{prefix} &cPlayer is not available.";
        private String targetPlayerNotAvailable = "{prefix} &cTarget player is not available.";
        private String configReloaded = "{prefix} &aConfig reloaded successfully.";
        private String invalidArgument = "{prefix} &cInvalid argument!";
        private String unknownCommand = "{prefix} &cUnknown command!";
        private String notEnoughArguments = "{prefix} &cNot enough arguments!";
        private String tooManyArguments = "{prefix} &cToo many arguments!";
        private String noPerm = "{prefix} &cYou do not have permission to do this action!";
    }

    private Various various = new Various();

    /**
     * Various lang settings
     *
     * @author geik
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Various extends OkaeriConfig {
        private String percentBar = "▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪";
        private String toggleOn = "&aActive";
        private String toggleOff = "&cDisabled";
        private String inputCancelWord = "cancel";
        private String calculatingGeneration = "&f&oCalculating...";

        @Comment({
                "Writes production amount with formatted number.",
                "Example: 1000 -> 1k, 1000000 -> 1m, 1000000000 -> 1b, 1000000000000 -> 1t"
        })
        private NumberFormat numberFormat = new NumberFormat();

        /**
         * NumberFormat settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class NumberFormat extends OkaeriConfig {
            private String thousand = "k";
            private String million = "m";
            private String billion = "b";
            private String trillion = "t";
        }
    }

    private BuyDisabled buyDisabled = new BuyDisabled();

    /**
     * BuyDisabled lang settings
     *
     * @author geik
     * @since 2.0
     */
    @Getter
    @Setter
    public static class BuyDisabled extends OkaeriConfig {
        private String title = "&6Farmer";
        private String subtitle = "&cVisit our website for farmer";
    }

    private Roles roles = new Roles();

    /**
     * Roles lang settings
     *
     * @author geik
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Roles extends OkaeriConfig {
        private String owner = "&cOwner";
        private String member = "&aMember";
        private String coop = "&eCoop";
    }

    private Gui gui = new Gui();

    /**
     * Gui lang settings
     *
     * @author geik
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Gui extends OkaeriConfig {

        private NextPage nextPage = new NextPage();

        /**
         * NextPage item gui settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class NextPage extends OkaeriConfig {
            @Comment("Placeholder: %nextpage% shows next page index.")
            private String name = "&eNext Page (%nextpage%)";
        }

        private PreviousPage previousPage = new PreviousPage();

        /**
         * PreviosPage item gui settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class PreviousPage extends OkaeriConfig {
            @Comment("Placeholder: %prevpage% shows previous page index.")
            private String name = "&ePrevious Page (%prevpage%)";
        }

        private FarmerGui farmerGui = new FarmerGui();

        /**
         * FarmerGui gui settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class FarmerGui extends OkaeriConfig {
            private String guiName = "&8Farmer Storage";

            private Items items = new Items();

            /**
             * Items of farmer gui settings
             *
             * @author geik
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends OkaeriConfig {

                private Manage manage = new Manage();

                /**
                 * Manage item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Manage extends OkaeriConfig {
                    private String name = "&eManagement Panel";
                    private List<String> lore = Arrays.asList(
                            "&7Only region owner can",
                            "&7open this panel.",
                            "",
                            "&dFarmer Stats:",
                            " &8▪ &7Level: &6{level}",
                            " &8▪ &7Capacity: &6{capacity}",
                            " &8▪ &7Tax Rate: &6{tax}",
                            "",
                            "&aClick for management panel!"
                    );
                }

                private Help help = new Help();
                /**
                 * Help item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Help extends OkaeriConfig {
                    private String name = "&eInformation";
                    private List<String> lore = Arrays.asList(
                            "&7This is inventory of Farmer.",
                            "&7Farmer stores items here.",
                            "&7What you can do here:",
                            " &8▪ &6Sell items",
                            " &8▪ &6Take items to inventory",
                            " &8▪ &6Management panel (Only Leader)",
                            "",
                            "&cIf you are coop you can",
                            "&conly see this menu."
                    );
                }

                private SellAll sellAll = new SellAll();
                /**
                 * SellAll item settings
                 *
                 * @author geik
                 * @since v6-b109
                 */
                @Getter
                @Setter
                public static class SellAll extends OkaeriConfig {
                    private String name = "&eSell All";
                    private List<String> lore = Arrays.asList(
                            "&7Sells all the items",
                            "&7that Farmer has.",
                            "&7",
                            "&aClick here to sell all!"
                    );
                }

                @Comment({
                        "Placeholders:",
                        "{stock} Shows how many item farmer have.",
                        "{maxstock} Shows maximum stock of farmer.",
                        "{percent} Shows stock fullness percent.",
                        "{bar} Shows percent in bar format. Uses #percentBar.",
                        "{price} Shows item price (each).",
                        "{stack_price} Shows item stack price (Basically multiplies price x64)"
                })
                private GroupItems groupItems = new GroupItems();
                /**
                 * GroupItems item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class GroupItems extends OkaeriConfig {
                    private List<String> lore = Arrays.asList(
                            "",
                            " &8▪ &7Stock: &f{stock}&8/&c{maxstock}",
                            " &8▪ &7Price: &f{price}$ each",
                            "&8&l  [{bar}&8&l] &r{percent}%",
                            "",
                            "&7Average Production (min): &f{prod_min}",
                            "&7Average Production (hour): &f{prod_hour}",
                            "&7Average Production (day): &f{prod_day}",
                            "{prod_blank}",
                            "&7Withdraw Stack &8[&eLeft Click&8]",
                            "&7Withdraw Max &8[&eRight Click&8]",
                            "&7Sell All &8[&eShift+Right Click&8]",
                            "",
                            "&4DANG: &cSell all feature takes",
                            "&4%{tax} &ctax.!"
                    );
                }

            }
        }

        private ManageGui manageGui = new ManageGui();
        /**
         * Manage gui settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class ManageGui extends OkaeriConfig {

            private String guiName = "&8Manager Panel";

            private Items items = new Items();

            /**
             * Items of manage gui settings
             *
             * @author geik
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends OkaeriConfig {

                @Comment({
                        "Placeholders:",
                        "{level} Shows level of farmer.",
                        "{max_level} Shows maximum level farmer can be.",
                        "{next_level} Shows next level of farmer.",
                        "{capacity} Shows farmer capacity.",
                        "{next_capacity} Shows the farmer's capacity at the next level.",
                        "{req_money} Shows required money of next level."
                })
                private UpgradeNext upgradeNext = new UpgradeNext();
                /**
                 * UpgradeNext item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class UpgradeNext extends OkaeriConfig {
                    private String name = "&6{level}. &eLevel Farmer";
                    private List<String> lore = Arrays.asList(
                            "",
                            " &8▪ &7New Level: &6{next_level}&7/&c{max_level}",
                            " &8▪ &7New Capacity: &e{next_capacity}",
                            " &8▪ &7Required Money: &6{req_money}",
                            "",
                            "&aClick for upgrade level!"
                    );
                }

                @Comment({
                        "Placeholders:",
                        "{level} Shows level of farmer.",
                        "{capacity} Shows farmer capacity.",
                })
                private MaxLevel maxLevel = new MaxLevel();
                /**
                 * MaxLevel item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class MaxLevel extends OkaeriConfig {
                    private String name = "&6{level}. &eLevel Farmer";
                    private List<String> lore = Arrays.asList(
                            "&7Farmer is in max level.",
                            "&7You cannot upgrade much more.",
                            "",
                            " &8▪ &7Capacity: &6{capacity}"
                    );
                }

                private CloseFarmer closeFarmer = new CloseFarmer();
                /**
                 * CloseFarmer item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class CloseFarmer extends OkaeriConfig {
                    private String name = "&eClose Collecting";
                    private List<String> lore = Arrays.asList(
                            "&7Closes farmer and it will be",
                            "&7useless until reopen.",
                            "",
                            " &8▪ &7Status: &6{status}",
                            "",
                            "&aClick for change!"
                    );
                }

                private Users users = new Users();
                /**
                 * Users item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Users extends OkaeriConfig {
                    private String name = "&eUser Management";
                    private List<String> lore = Arrays.asList(
                            "&7You can add/remove/modify",
                            "&7users in here.",
                            "",
                            "&aClick for open."
                    );
                }

                private Modules modules = new Modules();
                /**
                 * Modules item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Modules extends OkaeriConfig {
                    private String name = "&eModules";
                    private List<String> lore = Arrays.asList(
                            "&7You can modify farmer",
                            "&7modules in here.",
                            "",
                            "&aClick for open."
                    );
                }
            }
        }

        private BuyGui buyGui = new BuyGui();
        /**
         * BuyGui gui settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class BuyGui extends OkaeriConfig {

            private String guiName = "&8Buy Farmer";

            private Items items = new Items();

            /**
             * Items of buy gui settings
             *
             * @author geik
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends OkaeriConfig {

                private BuyItem buyItem = new BuyItem();
                /**
                 * BuyItem settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class BuyItem extends OkaeriConfig {
                    private String name = "&eBuy Farmer";
                    private List<String> lore = Arrays.asList(
                            "&7You can buy farmer by",
                            "&7clicking this item.",
                            "",
                            " &8▪ &7Price: &6{price}",
                            "",
                            "&aClick for buy!"
                    );
                }
            }
        }

        private UsersGui usersGui = new UsersGui();
        /**
         * Users gui settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class UsersGui extends OkaeriConfig {

            private String guiName = "&8Farmer Users";

            private Items items = new Items();

            /**
             * Items of buy gui settings
             *
             * @author geik
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends OkaeriConfig {

                private User user = new User();
                /**
                 * User head item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class User extends OkaeriConfig {
                    private String name = "&e%player_name%";
                    private List<String> lore = Arrays.asList(
                            "",
                            " &8▪ &7Role: &6{role}",
                            "",
                            "&aLeft or Right click for promote/demote",
                            "&4Shift+Right click for delete"
                    );
                }

                private Help help = new Help();
                /**
                 * Help item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Help extends OkaeriConfig {
                    private String name = "&eInformation";
                    private List<String> lore = Arrays.asList(
                            "&7You can promote/demote/remove",
                            "&7and add user here.",
                            "",
                            "&7Perm Info:",
                            " &8▪ &eCoop can only look farmer.",
                            " &8▪ &6Member can sell and take items.",
                            " &8▪ &cOwner can do everything."
                    );
                }

                private AddUser addUser = new AddUser();
                /**
                 * AddUser item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class AddUser extends OkaeriConfig {
                    private String name = "&eAdd user";
                    private List<String> lore = Arrays.asList(
                            "",
                            "&aClick for add user."
                    );
                }
            }
        }

        private GeyserGui geyserGui = new GeyserGui();
        /**
         * Geyser gui settings
         * (Only for geyser player)
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class GeyserGui extends OkaeriConfig {

            private String guiName = "&8Farmer Geyser Gui";

            private Items items = new Items();

            /**
             * Items of buy gui settings
             *
             * @author amownyy
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends OkaeriConfig {

                private LeftClick leftClick = new LeftClick();
                /**
                 * GeyserLeftClick item settings
                 * (Only for geyser player)
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class LeftClick extends OkaeriConfig {
                    private String name = "&eLeft Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "&aGet the selected item as a",
                            "&astack in your farmer inventory."
                    );
                }

                private RightClick rightClick = new RightClick();
                /**
                 * GeyserRightClick item settings
                 * (Only for geyser player)
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class RightClick extends OkaeriConfig {
                    private String name = "&eRight Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "&aGet the selected item as a",
                            "&amax amount in your farmer inventory."
                    );
                }

                private ShiftRightClick shiftRightClick = new ShiftRightClick();
                /**
                 * GeyserShiftRightClick item settings
                 * (Only for geyser player)
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class ShiftRightClick extends OkaeriConfig {
                    private String name = "&eShift+Right Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "&aSell all items in your farmer inventory."
                    );
                }
            }
        }

        private ModuleGui moduleGui = new ModuleGui();
        /**
         * ModuleGui gui settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class ModuleGui extends OkaeriConfig {
            private String guiName = "&8Farmer Modules";
        }

    }
}
