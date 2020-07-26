package fr.triozer.message.api.messenger;

import fr.triozer.message.api.data.MessageData;
import org.bukkit.command.CommandSender;

/**
 * @author Cédric / Triozer
 */
public interface Messenger {

    void send(Messenger receiver, String message);

    void reply(String message);

    String getName();

    boolean canBypassDisabledPM();

    boolean canBypassAntiSpam();

    MessageData getLastMessage();

    Messenger getLastReceiver();

    void setLastReceiver(Messenger messenger);

    CommandSender getCommandSender();

    MessengerSettings getSettings();

}
