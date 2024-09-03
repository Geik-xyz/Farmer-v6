package xyz.geik.farmer.helpers;

import java.net.URL;
import java.net.URLClassLoader;

public class ClassHelper extends URLClassLoader {

    public ClassHelper(final URL[] array, final ClassLoader classLoader) {
        super(array, classLoader);
    }

}
