package xyz.geik.farmer.configuration.lang;

import lombok.Getter;
import lombok.Setter;
import xyz.geik.farmer.configuration.LangFile;
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
public class en extends LangFile {

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
    public static class Messages extends LangFile.Messages {

        @Comment("Prefix of messages")
        private String prefix = "<aqua>Farmer <dark_gray>»";

        @Comment("placeholders: {money} money which deposited to player {tax} tax amount.")
        private String sellComplate = "{prefix} <green>Items sold. <gold>Profit: <yellow>{money}<white>, <gold>Tax: <yellow>{tax}";
        private String wrongWorld = "{prefix} <red>You cannot do this in this world.";
        private String noRegion = "{prefix} <red>There is no region for bound a farmer.";
        private String removedFarmer = "{prefix} <green>Removed farmer successfully.";
        private String noFarmer = "{prefix} <red>There is no farmer bound here.";
        private String mustBeOwner = "{prefix} <red>You must to be Region Owner for this.";
        private String inventoryFull = "{prefix} <red>Inventory full!";
        @Comment("placeholders: {money} players money {req_money} required money.")
        private String notEnoughMoney = "{prefix} <red>Don't have enough money! Required: <dark_red>{req_money}";
        @Comment("placeholders: {level} new upgraded level {capacity} new upgraded capacity.")
        private String levelUpgraded = "{prefix} <green>Farmer upgraded to <gold>{level}<green> level. <dark_green>New Capacity: <yellow>{capacity}";
        @Comment("placeholders: {status} shows status of farmer status. (#toggledON, #toggledOFF)")
        private String toggleFarmer = "{prefix} <green>Farmers collection settings changed to: <yellow>{status}";
        private String featureDisabled = "{prefix} <red>This feature disallowed.";
        private String reloadSuccess = "{prefix} <green>Config reloaded successfully. It took %ms%";
        private String boughtFarmer = "{prefix} <green>Farmer bought successfully.";
        private String inCooldown = "{prefix} <red>You should wait {time}s to do it again.";
        private String waitingInput = "{prefix} <green>Type input to chat in 6sec and type <red>{cancel} <green>to cancel.";
        private String notOwner = "{prefix} <red>You must be the owner of the region to use this command.";
        private String inputCancel = "{prefix} <red>No longer waiting for input.";
        private String userAdded = "{prefix} <green>Added successfully.";
        private String userAlreadyExist = "{prefix} <dark_red>{player} <red>Already added.";
        private String userCouldntFound = "{prefix} <red>User has not played before!";
        private String reachedMaxUser = "{prefix} <red>You have reached max user capacity.";
        private String playerNotOnline = "{prefix} <red>Target player is not online.";
        private String playerNotAvailable = "{prefix} <red>Player is not available.";
        private String targetPlayerNotAvailable = "{prefix} <red>Target player is not available.";
        private String configReloaded = "{prefix} <green>Config reloaded successfully.";
        private String invalidArgument = "{prefix} <red>Invalid argument!";
        private String unknownCommand = "{prefix} <red>Unknown command!";
        private String notEnoughArguments = "{prefix} <red>Not enough arguments!";
        private String tooManyArguments = "{prefix} <red>Too many arguments!";
        private String noPerm = "{prefix} <red>You do not have permission to do this action!";
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
    public static class Various extends LangFile.Various {
        private String percentBar = "▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪";
        private String toggleOn = "<green>Active";
        private String toggleOff = "<red>Disabled";
        private String inputCancelWord = "cancel";
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
    public static class BuyDisabled extends LangFile.BuyDisabled {
        private String title = "<gold>Farmer";
        private String subtitle = "<red>Visit our website for farmer";
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
    public static class Roles extends LangFile.Roles {
        private String owner = "<red>Owner";
        private String member = "<green>Member";
        private String coop = "<yellow>Coop";
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
    public static class Gui extends LangFile.Gui {

        private NextPage nextPage = new NextPage();

        /**
         * NextPage item gui settings
         *
         * @author geik
         * @since 2.0
         */
        @Getter
        @Setter
        public static class NextPage extends LangFile.Gui.NextPage {
            @Comment("Placeholder: %nextpage% shows next page index.")
            private String name = "<yellow>Next Page (%nextpage%)";
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
        public static class PreviousPage extends LangFile.Gui.PreviousPage {
            @Comment("Placeholder: %prevpage% shows previous page index.")
            private String name = "<yellow>Previous Page (%prevpage%)";
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
        public static class FarmerGui extends LangFile.Gui.FarmerGui {
            private String guiName = "<dark_gray>Farmer Storage";

            private Items items = new Items();

            /**
             * Items of farmer gui settings
             *
             * @author geik
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.FarmerGui.Items {

                private Manage manage = new Manage();

                /**
                 * Manage item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Manage extends LangFile.Gui.FarmerGui.Items.Manage {
                    private String name = "<yellow>Management Panel";
                    private List<String> lore = Arrays.asList(
                            "<gray>Only region owner can",
                            "<gray>open this panel.",
                            "",
                            "<light_purple>Farmer Stats:",
                            " <dark_gray>▪ <gray>Level: <gold>{level}",
                            " <dark_gray>▪ <gray>Capacity: <gold>{capacity}",
                            " <dark_gray>▪ <gray>Tax Rate: <gold>{tax}",
                            "",
                            "<green>Click to management panel!"
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
                public static class Help extends LangFile.Gui.FarmerGui.Items.Help {
                    private String name = "<yellow>Information";
                    private List<String> lore = Arrays.asList(
                            "<gray>This is inventory of Farmer.",
                            "<gray>Farmer stores items here.",
                            "<gray>What you can do here:",
                            " <dark_gray>▪ <gold>Sell items",
                            " <dark_gray>▪ <gold>Take items to inventory",
                            " <dark_gray>▪ <gold>Management panel (Only Leader)",
                            "",
                            "<red>If you are coop you can",
                            "<red>only see this menu."
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
                public static class SellAll extends LangFile.Gui.FarmerGui.Items.SellAll {
                    private String name = "<yellow>Sell All";
                    private List<String> lore = Arrays.asList(
                            "<gray>Sells all the items",
                            "<gray>that Farmer has.",
                            "<gray>",
                            "<green>Click here to sell all!"
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
                public static class GroupItems extends LangFile.Gui.FarmerGui.Items.GroupItems {
                    private List<String> lore = Arrays.asList(
                            "",
                            " <dark_gray>▪ <gray>Stock: <white>{stock}<dark_gray>/<red>{maxstock}",
                            " <dark_gray>▪ <gray>Price: <white>{price}$ each",
                            "<dark_gray><bold>[{bar}<dark_gray><bold>] <reset>{percent}%",
                            "",
                            "<gray>Average Production (min): <white>{prod_min}",
                            "<gray>Average Production (hour): <white>{prod_hour}",
                            "<gray>Average Production (day): <white>{prod_day}",
                            "{prod_blank}",
                            "<gray>Withdraw Stack <dark_gray>[<yellow>Left Click<dark_gray>]",
                            "<gray>Withdraw Max <dark_gray>[<yellow>Right Click<dark_gray>]",
                            "<gray>Sell All <dark_gray>[<yellow>Shift+Right Click<dark_gray>]",
                            "",
                            "<dark_red>DANG: <red>Sell all feature takes",
                            "<dark_red>%{tax} <red>tax.!"
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
        public static class ManageGui extends LangFile.Gui.ManageGui {

            private String guiName = "<dark_gray>Manager Panel";

            private Items items = new Items();

            /**
             * Items of manage gui settings
             *
             * @author geik
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.ManageGui.Items {

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
                public static class UpgradeNext extends LangFile.Gui.ManageGui.Items.UpgradeNext {
                    private String name = "<gold>{level}. <yellow>Level Farmer";
                    private List<String> lore = Arrays.asList(
                            "",
                            " <dark_gray>▪ <gray>New Level: <gold>{next_level}<gray>/<red>{max_level}",
                            " <dark_gray>▪ <gray>New Capacity: <yellow>{next_capacity}",
                            " <dark_gray>▪ <gray>Required Money: <gold>{req_money}",
                            "",
                            "<green>Click to upgrade level!"
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
                public static class MaxLevel extends LangFile.Gui.ManageGui.Items.MaxLevel {
                    private String name = "<gold>{level}. <yellow>Level Farmer";
                    private List<String> lore = Arrays.asList(
                            "<gray>Farmer is in max level.",
                            "<gray>You cannot upgrade much more.",
                            "",
                            " <dark_gray>▪ <gray>Capacity: <gold>{capacity}"
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
                public static class CloseFarmer extends LangFile.Gui.ManageGui.Items.CloseFarmer {
                    private String name = "<yellow>Close Collecting";
                    private List<String> lore = Arrays.asList(
                            "<gray>Closes farmer and it will be",
                            "<gray>useless until reopen.",
                            "",
                            " <dark_gray>▪ <gray>Status: <gold>{status}",
                            "",
                            "<green>Click to change!"
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
                public static class Users extends LangFile.Gui.ManageGui.Items.Users {
                    private String name = "<yellow>User Management";
                    private List<String> lore = Arrays.asList(
                            "<gray>You can add/remove/modify",
                            "<gray>users in here.",
                            "",
                            "<green>Click to open."
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
                public static class Modules extends LangFile.Gui.ManageGui.Items.Modules {
                    private String name = "<yellow>Modules";
                    private List<String> lore = Arrays.asList(
                            "<gray>You can modify farmer",
                            "<gray>modules in here.",
                            "",
                            "<green>Click to open."
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
        public static class BuyGui extends LangFile.Gui.BuyGui {

            private String guiName = "<dark_gray>Buy Farmer";

            private Items items = new Items();

            /**
             * Items of buy gui settings
             *
             * @author geik
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.BuyGui.Items {

                private BuyItem buyItem = new BuyItem();
                /**
                 * BuyItem settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class BuyItem extends LangFile.Gui.BuyGui.Items.BuyItem {
                    private String name = "<yellow>Buy Farmer";
                    private List<String> lore = Arrays.asList(
                            "<gray>You can buy farmer by",
                            "<gray>clicking this item.",
                            "",
                            " <dark_gray>▪ <gray>Price: <gold>{price}",
                            "",
                            "<green>Click to buy!"
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
        public static class UsersGui extends LangFile.Gui.UsersGui {

            private String guiName = "<dark_gray>Farmer Users";

            private Items items = new Items();

            /**
             * Items of buy gui settings
             *
             * @author geik
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.UsersGui.Items {

                private User user = new User();
                /**
                 * User head item settings
                 *
                 * @author geik
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class User extends LangFile.Gui.UsersGui.Items.User {
                    private String name = "<yellow>%player_name%";
                    private List<String> lore = Arrays.asList(
                            "",
                            " <dark_gray>▪ <gray>Role: <gold>{role}",
                            "",
                            "<green>Left or Right click to promote/demote",
                            "<dark_red>Shift+Right click to delete"
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
                public static class Help extends LangFile.Gui.UsersGui.Items.Help {
                    private String name = "<yellow>Information";
                    private List<String> lore = Arrays.asList(
                            "<gray>You can promote/demote/remove",
                            "<gray>or add user here.",
                            "",
                            "<gray>Perm Info:",
                            " <dark_gray>▪ <yellow>Coop can only look farmer.",
                            " <dark_gray>▪ <gold>Member can sell and take items.",
                            " <dark_gray>▪ <red>Owner can do everything."
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
                public static class AddUser extends LangFile.Gui.UsersGui.Items.AddUser {
                    private String name = "<yellow>Add user";
                    private List<String> lore = Arrays.asList(
                            "",
                            "<green>Click to add user."
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
        public static class ModuleGui extends LangFile.Gui.ModuleGui {
            private String guiName = "<dark_gray>Farmer Modules";
        }

    }
}