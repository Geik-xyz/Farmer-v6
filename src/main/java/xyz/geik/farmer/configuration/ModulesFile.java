package xyz.geik.farmer.configuration;


import lombok.Getter;
import lombok.Setter;
import xyz.geik.glib.shades.okaeri.configs.OkaeriConfig;
import xyz.geik.glib.shades.okaeri.configs.annotation.Comment;
import xyz.geik.glib.shades.okaeri.configs.annotation.NameModifier;
import xyz.geik.glib.shades.okaeri.configs.annotation.NameStrategy;
import xyz.geik.glib.shades.okaeri.configs.annotation.Names;

/**
 * Modules file
 * @author geik
 * @since 2.0
 */
@Getter
@Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class ModulesFile extends OkaeriConfig {

    @Comment("Voucher Module")
    private Voucher voucher = new Voucher();

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

    /**
     * TODO Add other modules here with class name.
     * For example if you are adding Voucher addon,
     * You should use class name of it for naming categories. Example: Voucher on top.
     * Also you should make status option for enabling status. Make sure it's not FEATURE ;)
     * Lastly add config values to here then catch them with Main.getModulesFile().getVoucher().yoursetting();
     */
}