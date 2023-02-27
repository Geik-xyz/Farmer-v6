package xyz.geik.farmer.modules.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.modules.FarmerModule;

/**
 * Module already loaded exception
 */
@Getter
public class ModuleExistException extends Exception {

    // Name of addon
    private String addonName;

    // Is addon enabled
    private boolean isEnabled;

    /**
     * Module already loaded exception
     *
     * @param message
     * @param module
     */
    public ModuleExistException(String message, @NotNull FarmerModule module) {
        super(message);
        this.addonName = module.getName();
        this.isEnabled = module.isEnabled();
    }
}
