package xyz.geik.farmer.configuration;


import lombok.Getter;
import lombok.Setter;
import xyz.geik.glib.shades.okaeri.configs.OkaeriConfig;
import xyz.geik.glib.shades.okaeri.configs.annotation.Comment;
import xyz.geik.glib.shades.okaeri.configs.annotation.NameStrategy;
import xyz.geik.glib.shades.okaeri.configs.annotation.Names;

import java.util.ArrayList;
import java.util.Arrays;
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
        private List<String> items = new ArrayList<>();
    }
}