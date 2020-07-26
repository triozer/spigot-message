package fr.triozer.message.player;

import fr.triozer.message.MessagePluginConfiguration;
import fr.triozer.message.api.messenger.Messenger;
import fr.triozer.message.api.messenger.MessengerSettings;
import fr.triozer.message.data.TextMessageData;
import fr.triozer.message.event.PlayerReplyMessageEvent;
import fr.triozer.message.event.PlayerSendMessageEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Cédric / Triozer
 */
public class PlayerMessenger implements Messenger {

    private final Player            player;
    private final MessengerSettings settings;
    private       Messenger         lastReceiver;
    private       TextMessageData   lastMessageData;

    public PlayerMessenger(Player player) {
        this.player = player;
        this.settings = new MessengerSettings();
    }

    private boolean canSendMessage(Messenger receiver, TextMessageData messageData) {
        if (this == receiver && !MessagePluginConfiguration.Option.PSYCHO) {
            this.player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Error.PSYCHO));
            return false;
        }
        if (!receiver.getSettings().hasEnabledPM()) {
            return false;
        }

        TextMessageData message = this.lastMessageData;
        this.lastMessageData = messageData;

        if (message == null) {
            return true;
        }
        if (!MessagePluginConfiguration.Option.ANTI_SPAM || this.canBypassAntiSpam()) {
            return true;
        }

        if (messageData.getTime() - message.getTime() <= MessagePluginConfiguration.Option.ANTI_SPAM_INTERVAL) {
            this.player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Error.SPAM));
            return false;
        }

        if (messageData.getMessageContent().equalsIgnoreCase(message.getMessageContent())) {
            if (MessagePluginConfiguration.Option.ANTI_SPAM_SAME_MESSAGE == -1) {
                this.player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Error.SAME_MESSAGE));
                return false;
            }
            if (messageData.getTime() - message.getTime() <= MessagePluginConfiguration.Option.ANTI_SPAM_SAME_MESSAGE) {
                this.player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Error.SAME_MESSAGE));
                return false;
            }
        }

        return true;
    }

    private void sendMessage(Messenger receiver, String message) {
        this.getCommandSender().sendMessage(
                MessagePluginConfiguration.t(MessagePluginConfiguration.Format.OUT)
                        .replace("{sender}", this.getName())
                        .replace("{receiver}", receiver.getName())
                        .replace("{message}", message)
        );
        receiver.getCommandSender().sendMessage(
                MessagePluginConfiguration.t(MessagePluginConfiguration.Format.IN)
                        .replace("{sender}", this.getName())
                        .replace("{receiver}", receiver.getName())
                        .replace("{message}", message)
        );
    }

    @Override
    public void send(Messenger receiver, String message) {
        TextMessageData messageData = new TextMessageData(message);
        if (!this.canSendMessage(receiver, messageData)) {
            return;
        }
        PlayerSendMessageEvent event = new PlayerSendMessageEvent(this, receiver, messageData);
        if (event.isCancelled()) {
            return;
        }
        this.sendMessage(receiver, message);
        receiver.setLastReceiver(this);
        this.lastReceiver = receiver;
    }

    @Override
    public void reply(String message) {
        if (this.lastReceiver == null) {
            this.player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Error.NO_REPLY));
            return;
        }
        TextMessageData messageData = new TextMessageData(message);
        if (!this.canSendMessage(this.lastReceiver, messageData)) {
            return;
        }
        PlayerReplyMessageEvent event = new PlayerReplyMessageEvent(this, this.lastReceiver, messageData);
        if (event.isCancelled()) {
            return;
        }
        this.sendMessage(this.lastReceiver, message);
    }

    @Override
    public String getName() {
        return this.player.getName();
    }

    @Override
    public boolean canBypassDisabledPM() {
        return this.getCommandSender().hasPermission(MessagePluginConfiguration.Permission.BYPASS_MESSAGE);
    }

    @Override
    public boolean canBypassAntiSpam() {
        return this.getCommandSender().hasPermission(MessagePluginConfiguration.Permission.BYPASS_SPAM);
    }

    @Override
    public TextMessageData getLastMessage() {
        return this.lastMessageData;
    }

    @Override
    public Messenger getLastReceiver() {
        return this.lastReceiver;
    }

    @Override
    public void setLastReceiver(Messenger messenger) {
        this.lastReceiver = messenger;
    }

    @Override
    public CommandSender getCommandSender() {
        return this.player;
    }

    @Override
    public MessengerSettings getSettings() {
        return this.settings;
    }

}
