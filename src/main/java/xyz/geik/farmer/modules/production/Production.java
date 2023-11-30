package xyz.geik.farmer.modules.production;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.modules.FarmerModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Production module main class
 */
@Getter
public class Production extends FarmerModule {

    /**
     * Constructor of class
     */
    public Production() {}

    /**
     * -- GETTER --
     *  Get instance of module
     */
    @Getter
    private static Production instance;

    /**
     * number formats which is k,m,b,t for 1k instead of 1000
     */
    private String[] numberFormat = new String[]{"k", "m", "b", "t"};

    /**
     * calculate interval time
     */
    private long reCalculate = 15L;

    /**
     * items to calculate
     */
    private List<String> productionItems = new ArrayList<>();

    /**
     * onLoad method of module
     */
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

    /**
     * onEnable method of module
     */
    @Override
    public void onEnable() {
        registerListener(new ProductionCalculateEvent());
        setLang(Main.getConfigFile().getSettings().getLang(), Main.getInstance());
        numberFormat[0] = getLang().getText("numberFormat.thousand");
        numberFormat[1] = getLang().getText("numberFormat.million");
        numberFormat[2] = getLang().getText("numberFormat.billion");
        numberFormat[3] = getLang().getText("numberFormat.trillion");
        reCalculate = getConfig().get("settings.reCalculate", 15L);
    }

    /**
     * onReload method of module
     */
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

    /**
     * onDisable method of module
     */
    @Override
    public void onDisable() {}

    /**
     * is item suitable to calculate
     *
     * @param item item of farmer
     * @return boolean
     */
    public static boolean isCalculateItem(@NotNull FarmerItem item) {
        return Production.getInstance().getProductionItems().contains(item.getName())
                || getInstance().getProductionItems().isEmpty();
    }
}
