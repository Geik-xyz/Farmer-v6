package xyz.geik.farmer.helpers;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Base class for class loader
 *
 * @author WaterArchery
 */
public class ClassHelper extends URLClassLoader {

    // Constructor of ClassHelper
    public ClassHelper(final URL[] array, final ClassLoader classLoader) {
        super(array, classLoader);
    }

}
