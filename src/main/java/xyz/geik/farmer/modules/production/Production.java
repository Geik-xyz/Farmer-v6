package xyz.geik.farmer.modules.production;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.FarmerAPI;
import xyz.geik.farmer.helpers.Settings;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.modules.FarmerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Production module main class
 */
@Getter
public class Production extends FarmerModule {

    // Instance of module
    private static Production instance;

    private String[] numberFormat = new String[]{"k", "m", "b", "t"};
    private long reCalculate = 15L;

    private List<String> productionItems = new ArrayList<>();

    /**
     * Get instance of module
     *
     * @return instance
     */
    public static Production getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        setName("Production");
        setEnabled(true);
        setDescription("Average Production Calculating module");
        setModulePrefix("Production Calculating");
        instance = this;
        setConfig(Main.getInstance());
        getProductionItems().addAll(getConfig().getStringList("items"));
        if (!getConfig().getBoolean("settings.feature"))
            setEnabled(false);
    }

    @Override
    public void onEnable() {
        registerListener(new ProductionCalculateEvent());
        setLang(Settings.lang, Main.getInstance());
        numberFormat[0] = getLang().getText("numberFormat.thousand");
        numberFormat[1] = getLang().getText("numberFormat.million");
        numberFormat[2] = getLang().getText("numberFormat.billion");
        numberFormat[3] = getLang().getText("numberFormat.trillion");
        reCalculate = getConfig().get("settings.reCalculate", 15L);
    }

    @Override
    public void onReload() {
        if (!this.isEnabled())
            return;
        numberFormat[0] = getLang().getText("numberFormat.thousand");
        numberFormat[1] = getLang().getText("numberFormat.million");
        numberFormat[2] = getLang().getText("numberFormat.billion");
        numberFormat[3] = getLang().getText("numberFormat.trillion");
        reCalculate = getConfig().get("settings.reCalculate", 15L);
    }

    @Override
    public void onDisable() {}

    /**
     * @param item item of farmer
     * @return boolean
     */
    public static boolean isCalculateItem(@NotNull FarmerItem item) {
        return Production.getInstance().getProductionItems().contains(item.getName())
                || getInstance().getProductionItems().isEmpty();
    }
}
