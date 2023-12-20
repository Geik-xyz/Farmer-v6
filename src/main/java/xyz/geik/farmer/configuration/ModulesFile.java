package xyz.geik.farmer.configuration;


import lombok.Getter;
import lombok.Setter;
import xyz.geik.glib.shades.okaeri.configs.OkaeriConfig;
import xyz.geik.glib.shades.okaeri.configs.annotation.Comment;
import xyz.geik.glib.shades.okaeri.configs.annotation.NameStrategy;
import xyz.geik.glib.shades.okaeri.configs.annotation.Names;

import java.util.List;

/**
 * Modules file
 * @author geik
 * @since 2.0
 */
@Getter
@Setter
@Names(strategy = NameStrategy.IDENTITY)
public class ModulesFile extends OkaeriConfig {

    @Comment("Voucher Module")
    private Voucher Voucher = new Voucher();

    /**
     * Module configuration
     *
     * @author geik
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Voucher extends OkaeriConfig {
        @Comment({"if you don't want to use voucher system",
                "which is place farmer with a voucher only console",
                "and players with farmer.admin permission can give voucher.",
                "you can disable buy feature and give farmer with command"})
        private boolean status = true;

        @Comment({"if you want to use voucher system with override method",
                "you can enable this setting. it will override voucher to farmer",
                "which replace farmer level same as voucher level when voucher level",
                "is higher than farmer level."})
        private boolean useWhenFarmerExist = false;

        @Comment({"if you want to give voucher when farmer removed",
                "you can enable this setting. it will give voucher to player"})
        private boolean giveVoucherWhenRemove = false;
    }

    @Comment("Auto Harvest Module")
    private AutoHarvest AutoHarvest = new AutoHarvest();

    /**
     * Module configuration
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class AutoHarvest extends OkaeriConfig {
        @Comment({"auto harvest crops addon harvest crops automatically",
                "if you want this module to be enabled, you can set this to true"})
        private boolean status = false;

        @Comment({"if you want to make a little difficult to players, you can set this to true",
                "also it can do 1 check for each crop can impact performance small amount, but it can be a problem if you have a lot of crops",
                "if you set this to true, the crops will be harvested only if the piston is top direction of the crops",
                "if you set this to false, the crops will be harvested immediately even there is no piston"})
        private boolean requirePiston = false;

        @Comment({"this setting required requirePiston to be true to work",
                "recommended to set this to false if you have performance issues. (does 5 block check for each crop)",
                "if you set this to false, the crops will be harvested only if the piston is top direction of the crops",
                "if you set this to true, the crops will be harvested if the piston in any direction of the crops"})
        private boolean checkAllDirections = false;

        @Comment({"Harvest crops without farmer"})
        private boolean withoutFarmer = false;

        @Comment({"when stock is full, the crops cannot drop to ground",
                "recommended to set this to false if you have performance issues. (does 5 block check for each crop)",
                "if you set this to false, the crops will grow even if the stock is full.",
                "and the crops will be harvested and dropped on the ground."})
        private boolean checkStock = true;

        @Comment({"default value of autoharvest module"})
        private boolean defaultStatus = false;

        @Comment({"there is perm for use this module default: autoharvest.harvest"})
        private String customPerm = "farmer.autoharvest";

        @Comment({"write the crops you want to harvest here",
                "*IMPORTANT* Write only base item of crops. for example, if you want to harvest wheat, you must write WHEAT here",
                "if you want to harvest all crops, you must write all items here (*if you remove this setting, it can cause errors*)",
                "also you must write the crop names same as items.yml items.",
                "Available harvests: WHEAT, CARROT, POTATO, PUMPKIN, MELON, BEETROOT, NETHER_WART, SUGAR_CANE, COCOA_BEANS"})
        private List<String> items = List.of("WHEAT", "CARROT", "POTATO", "PUMPKIN");
    }

    @Comment("Geyser Module")
    private Geyser Geyser = new Geyser();

    /**
     * Module configuration
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Geyser extends OkaeriConfig {
        @Comment({"if you want to use geyser module",
                "set feature to true."})
        private boolean status = false;
    }

    @Comment("Spawner Killer Module")
    private SpawnerKiller SpawnerKiller = new SpawnerKiller();

    /**
     * Module configuration
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class SpawnerKiller extends OkaeriConfig {
        @Comment({"if you want to use spawner killer system",
                "set feature to true.",
                "and players with farmer.admin permission can give spawner killer.",
                "you can disable buy feature and give farmer with command"})
        private boolean status = false;

        @Comment({"if you want to kill mobs without farmer set it false"})
        private boolean requireFarmer = true;

        @Comment({"cook foods on spawner drop"})
        private boolean cookFoods = true;

        @Comment({"remove mob can't see mob only spawn item."})
        private boolean removeMob = true;

        @Comment({"default status for spawner killer when farmer place",
                "if set true, farmer will be enable spawner killer by default",
                "if set false, farmer will be disable spawner killer by default"})
        private boolean defaultStatus = true;

        @Comment({"custom perm for spawner killer status changer"})
        private String customPerm = "farmer.spawnerkiller";

        @Comment({"set whitelist mobs for spawner killer",
                "if you want to kill only whitelist mobs, set mode to whitelist",
                "if you want to kill all mobs except blacklist mobs, set mode to blacklist"})
        private String mode = "blacklist";

        @Comment({"You can add remove blacklist section",
                "if you want to remove mobs from blacklist"})
        private List<String> whitelist = List.of("VILLAGER");

        @Comment({"You can add remove blacklist section",
                "if you want to remove mobs from blacklist"})
        private List<String> blacklist = List.of("VILLAGER");
    }

    @Comment("Production Module")
    private Production Production = new Production();

    /**
     * Module configuration
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Production extends OkaeriConfig {
        @Comment({"if you don't want to calculate",
                "average production on farmer items",
                "set it to false"})
        private boolean status = false;

        @Comment({"if you want to kill mobs without farmer set it false"})
        private int reCalculate = 15;

        @Comment({"if you want to calculate average production",
                "write the items you want to calculate",
                "write the items in the items.yml file with same name",
                "you can also remove this section, and it will calculate all items in the items.yml file"})
        private List<String> items = List.of("CACTUS");
    }

    @Comment("Auto Seller Module")
    private AutoSeller AutoSeller = new AutoSeller();

    /**
     * Module configuration
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class AutoSeller extends OkaeriConfig {
        @Comment({"if you want to enable it, set this to true",
                "this setting will enable the auto-seller",
                "and players with farmer.admin permission can give auto seller.",
                "you can disable buy feature and give farmer with command"})
        private boolean status = false;

        @Comment({"if you want to enable the auto-seller for all players, set this to true",
                "if you want to enable the auto-seller for specific players, set this to false (perm-check)",
                "which replace farmer level same as auto seller level when auto seller level",
                "is higher than farmer level."})
        private boolean defaultStatus = false;

        @Comment({"custom perm only used if defaultStatus is false",
                "only players with this perm will be able to use the auto-seller"})
        private String customPerm = "farmer.autoseller";

        @Comment({"all the items that will be sold by the auto-seller",
                "you can add as many items as you want",
                "the items must be same as the ones in the items.yml of the Farmer",
                "you can also remove this section for enable it to all items"})
        private List<String> items = List.of("PUMPKIN", "MELON", "WHEAT", "CACTUS");
    }

    /**
     * TODO Add other modules here with class name.
     * For example if you are adding Voucher addon,
     * You should use class name of it for naming categories. Example: Voucher on top.
     * Also you should make status option for enabling status. Make sure it's not FEATURE ;)
     * Lastly add config values to here then catch them with Main.getModulesFile().getVoucher().yoursetting();
     */
}