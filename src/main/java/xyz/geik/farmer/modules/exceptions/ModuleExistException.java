package xyz.geik.farmer.modules.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.modules.FarmerModule;

/**
 * Module already loaded exception
 */
@Getter
public class ModuleExistException extends Exception {

    /**
     * Name of addon
     */
    private final String addonName;

    /**
     * Is addon enabled
     */
    private final boolean isEnabled;

    /**
     * Module already loaded exception
     *
     * @param message of exception
     * @param module of farmer
     */
    public ModuleExistException(String message, @NotNull FarmerModule module) {
        super(message);
        this.addonName = module.getName();
        this.isEnabled = module.isEnabled();
    }
}
