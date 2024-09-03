package xyz.geik.farmer.helpers;

import org.jetbrains.annotations.Nullable;
import xyz.geik.farmer.Main;
import xyz.geik.glib.module.Modulable;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModuleHelper {

    private static ModuleHelper instance;
    private final List<Modulable> modules = new ArrayList<>();

    public synchronized static ModuleHelper getInstance() {
        if (instance == null) instance = new ModuleHelper();
        return instance;
    }

    public void loadModules() {
        unloadModules();

        try {
            File folder = new File(Main.getInstance().getDataFolder(), "/modules");

            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (!file.getName().endsWith(".jar")) continue;

                FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                ClassLoader loader = ClassHelper.class.getClassLoader();
                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, loader);

                for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry()) {
                    if (!zipEntry.isDirectory() && zipEntry.getName().endsWith(".class")) {
                        String className = zipEntry.getName().replaceAll("/", ".").replaceAll(".class", "");
                        Class<?> loadedClass = urlClassLoader.loadClass(className);
                        Class<?>[] interfaces = loadedClass.getInterfaces();

                        if (interfaces.length > 0 && interfaces[0].getName().endsWith("Modulable")) {
                            Modulable module = (Modulable) loadedClass.getDeclaredConstructor().newInstance();
                            module.onEnable();
                            modules.add(module);
                        }
                    }
                }

                urlClassLoader.close();
                fileInputStream.close();
                zipInputStream.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void unloadModules() {
        for (Modulable module : new ArrayList<>(modules)) {
            module.onDisable();
            modules.remove(module);
        }
    }

    // Return null if Module is not loaded or invalid.
    public @Nullable Modulable getModule(String name) {
        for (Modulable module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }

        return null;
    }

    // Return null if Module is not loaded or invalid.
    public @Nullable <T extends Modulable> Modulable getModule(Class<T> classType) {
        for (Modulable module : modules) {
            if (module.getClass() == classType) return module;
        }

        return null;
    }

}
