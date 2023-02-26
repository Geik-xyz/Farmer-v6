package xyz.geik.farmer.modules.exceptions;

/**
 * ReloadModuleException execute when reloading a module
 * if the module is not loaded or the module is not found
 */
public class ReloadModuleException extends Exception {
        public ReloadModuleException(String message) {
            super(message);
        }
}
