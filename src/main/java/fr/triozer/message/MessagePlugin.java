package fr.triozer.message;

import com.google.common.base.Charsets;
import fr.triozer.message.api.messenger.Messenger;
import fr.triozer.message.command.MessageCommand;
import fr.triozer.message.command.ReplyCommand;
import fr.triozer.message.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Consumer;

import static net.md_5.bungee.api.ChatColor.*;

public final class MessagePlugin extends JavaPlugin {

    public static Console       LOG = new Console("[Message]");
    public static MessagePlugin            INSTANCE;
    public static HashMap<UUID, Messenger> MESSENGERS;

    private FileConfiguration data;
    private FileConfiguration messages;
    private File              messagesFile;
    private File              dataFile;

    @Override
    public void onEnable() {
        long duration = System.currentTimeMillis();
        INSTANCE = this;
        MESSENGERS = new HashMap<>();

        LOG.send(DARK_GRAY + "___________________________________________________");
        LOG.send("");
        LOG.send(GREEN + "                          *##\n");
        LOG.send(GREEN + "                       ##%%@@&%#\n");
        LOG.send(GREEN + "                   ###&@@@@@@@@@#(\n");
        LOG.send(GREEN + "                *#&@@@@@@@@@@@@@@((\n");
        LOG.send(GREEN + "                  .(%&@@@@@@@(((.\n");
        LOG.send(GREEN + "                    ,((&&##(,\n");
        LOG.send(GREEN + "                       ***\n");
        LOG.send("");
        LOG.send(DARK_GRAY + "___________________________________________________");

        generateConfigurationFiles();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            checkForNewerVersion((version) -> {
                String text = GRAY + "Actually running " + AQUA + getName() + GRAY + " v" + AQUA + getDescription().getVersion() + GRAY + " ! ";

                if (!getDescription().getVersion().equals(version)) {
                    text += "A new version is out (" + RED + version + GRAY + "). Please update the plugin for better support.";
                } else {
                    text += "You have the latest version. Thanks for support !";
                }

                LOG.send("");
                LOG.send(text);
                LOG.send("");
            });
        });

        duration = System.currentTimeMillis() - duration;
        LOG.send("    " + GREEN + "I'm ready !" + DARK_GRAY + " (took " + duration + "ms)");
        LOG.send(DARK_GRAY + "___________________________________________________");

        this.registerCommands();
    }

    private void registerCommands() {
        this.getCommand("message").setExecutor(new MessageCommand());
        this.getCommand("reply").setExecutor(new ReplyCommand());
    }

    private void generateConfigurationFiles() {
        try {
            if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
                throw new RuntimeException("Failed to create the plugin folder.");
            }

            File configurationFile = new File(getDataFolder(), "config.yml");
            this.messagesFile = new File(getDataFolder(), "messages.yml");
            this.dataFile = new File(getDataFolder(), "data.yml");

            this.data = new YamlConfiguration();
            this.messages = new YamlConfiguration();

            if (!this.dataFile.exists() && !this.dataFile.createNewFile()) {
                throw new RuntimeException("Failed to create the players data file.");
            }

            getConfig().load(configurationFile);
            this.messages.load(this.messagesFile);
            this.data.load(this.dataFile);

            LOG.send("    " + DARK_GRAY + "Configuration: " + GREEN + "Loaded" + DARK_GRAY + ".");
            LOG.send("    " + DARK_GRAY + "Messages: " + GREEN + "Loaded" + DARK_GRAY + ".");
        } catch (IOException | InvalidConfigurationException e) {
            LOG.stacktrace("An error occured while creating configuration files.", e);
        }
    }

    private void checkForNewerVersion(Consumer<String> consumer) {
        try (
                InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=33241").openStream();
                Scanner scanner = new Scanner(inputStream)
        ) {
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            LOG.stacktrace("Can't look for updates", exception);
        }
    }

    /**
     * Gets the players data configuration.
     *
     * @return The players data configuration.
     */
    public final FileConfiguration getData() {
        return this.data;
    }

    /**
     * Gets the players data file.
     *
     * @return The players data file.
     */
    public final File getDataFile() {
        return this.dataFile;
    }

    public final FileConfiguration getMessages() {
        return this.messages;
    }

    public void reloadMessage() {
        this.messages = YamlConfiguration.loadConfiguration(this.messagesFile);

        final InputStream defConfigStream = getResource("message.yml");
        if (defConfigStream == null) {
            return;
        }

        this.messages.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }
}
