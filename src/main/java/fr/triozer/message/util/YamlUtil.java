package fr.triozer.message.util;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Cédric / Triozer
 */
public final class YamlUtil {

    public static <T> T get(FileConfiguration from, String path) {
        return (T) from.get(path);
    }

}
