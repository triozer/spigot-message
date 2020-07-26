package fr.triozer.message;

import com.google.common.base.Charsets;
import fr.triozer.message.api.messenger.Messenger;
import fr.triozer.message.command.MessageCommand;
import fr.triozer.message.command.ReplyCommand;
import fr.triozer.message.listener.PlayerCacheListener;
import fr.triozer.message.player.PlayerMessenger;
import fr.triozer.message.util.Console;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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

    public static Console                  LOG = new Console("[Message]");
    public static MessagePlugin            INSTANCE;
    public static HashMap<UUID, Messenger> MESSENGERS;

    private FileConfiguration data;
    private FileConfiguration message;
    private File messageFile;
    private File              dataFile;

    public static void registerPlayer(Player player) {
        UUID uniqueId = player.getUniqueId();
        PlayerMessenger value = new PlayerMessenger(player.getPlayer());
        if (MessagePlugin.INSTANCE.getData().contains(uniqueId.toString())) {
            value.getSettings().setPM(MessagePlugin.INSTANCE.getData().getBoolean(uniqueId + ".enabled"));
        } else {
            MessagePlugin.INSTANCE.getData().set(uniqueId + ".enable", true);
        }
        MessagePlugin.MESSENGERS.put(uniqueId, value);
    }

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
                LOG.fine(text);
                LOG.send("");
            });
        });

        duration = System.currentTimeMillis() - duration;
        LOG.send("");
        LOG.send("    " + GREEN + "I'm ready !" + DARK_GRAY + " (took " + duration + "ms)");
        LOG.send(DARK_GRAY + "___________________________________________________");

        this.registerCommands();
        this.registerListeners();

        Bukkit.getOnlinePlayers().forEach(MessagePlugin::registerPlayer);
    }

    private void registerCommands() {
        this.getCommand("message").setExecutor(new MessageCommand());
        this.getCommand("reply").setExecutor(new ReplyCommand());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerCacheListener(), this);
    }

    private void generateConfigurationFiles() {
        try {
            if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
                throw new RuntimeException("Failed to create the plugin folder.");
            }

            File configurationFile = new File(getDataFolder(), "config.yml");
            this.messageFile = new File(getDataFolder(), "message.yml");
            this.dataFile = new File(getDataFolder(), "data.yml");

            saveDefaultConfiguration(configurationFile);
            saveDefaultConfiguration(this.messageFile);

            if (!this.dataFile.exists() && !this.dataFile.createNewFile()) {
                throw new RuntimeException("Failed to create the players data file.");
            }

            this.data = new YamlConfiguration();
            this.message = new YamlConfiguration();

            getConfig().load(configurationFile);
            this.message.load(this.messageFile);
            this.data.load(this.dataFile);

            LOG.send("    " + DARK_GRAY + "Configuration: " + GREEN + "Loaded" + DARK_GRAY + ".");
            LOG.send("    " + DARK_GRAY + "Messages: " + GREEN + "Loaded" + DARK_GRAY + ".");
        } catch (IOException | InvalidConfigurationException e) {
            LOG.stacktrace("An error occurred while creating configuration files.", e);
        }
    }

    private void saveDefaultConfiguration(File file) {
        if (!file.exists()) {
            saveResource(file.getName(), false);
            LOG.send("    " + DARK_GRAY + "Configuration: " + GREEN + "the default " + AQUA + file.getName() + GREEN + " has been saved" + DARK_GRAY + ". " +
                    "Please watch it and modify it as you wish.");
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

    public final FileConfiguration getMessage() {
        return this.message;
    }

    public void reloadMessage() {
        this.message = YamlConfiguration.loadConfiguration(this.messageFile);

        final InputStream defConfigStream = getResource("message.yml");
        if (defConfigStream == null) {
            return;
        }

        this.message.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }
}
