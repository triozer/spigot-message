package fr.triozer.message.event;

import fr.triozer.message.api.data.MessageData;
import fr.triozer.message.api.event.MessageEvent;
import fr.triozer.message.api.messenger.Messenger;
import org.bukkit.event.HandlerList;

/**
 * @author Cédric / Triozer
 */
public class PlayerSendMessageEvent extends MessageEvent {
    private static final HandlerList handlers = new HandlerList();

    public PlayerSendMessageEvent(Messenger sender, Messenger receiver, MessageData messageData) {
        super(sender, receiver, messageData);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
