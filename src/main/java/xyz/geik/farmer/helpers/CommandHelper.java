package xyz.geik.farmer.helpers;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import xyz.geik.glib.shades.triumphteam.cmd.core.exceptions.CommandRegistrationException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Utility class for unregistering commands
 *
 * @author Efe Kurban (hyperion), CraftLuna, TriumphTeam
 */
public class CommandHelper {

    /**
     * Unregisters every command related to Farmer v6
     */
    public static void unregisterCommands() {
        Lists.newArrayList("çiftçi", "farmer", "farm", "fm", "ciftci").forEach(CommandHelper::unregisterCommand);
    }

    /**
     * Unregisters a command
     *
     * @param name Command's name
     */
    private static void unregisterCommand(String name) {
        getBukkitCommands(getCommandMap()).remove(name);
    }

    /**
     * @return CommandMap object from Server
     */
    @NotNull
    private static CommandMap getCommandMap() {
        try {
            final Server server = Bukkit.getServer();
            final Method getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
            getCommandMap.setAccessible(true);

            return (CommandMap) getCommandMap.invoke(server);
        } catch (final Exception ignored) {
            throw new CommandRegistrationException("Unable get Command Map. Commands will not be registered!");
        }
    }

    /**
     * @return Map of Registered Commands, directly from Bukkit
     */
    @NotNull
    private static Map<String, Command> getBukkitCommands(@NotNull final CommandMap commandMap) {
        try {
            final Field bukkitCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            bukkitCommands.setAccessible(true);
            //noinspection unchecked
            return (Map<String, Command>) bukkitCommands.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CommandRegistrationException("Unable get Bukkit commands. Commands might not be registered correctly!");
        }
    }

}
