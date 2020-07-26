package fr.triozer.message.api.event;

import fr.triozer.message.api.data.MessageData;
import fr.triozer.message.api.messenger.Messenger;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * @author Cédric / Triozer
 */
public abstract class MessageEvent extends Event implements Cancellable {

    private       boolean   cancelled;
    private final Messenger sender;
    private final Messenger receiver;
    private       MessageData messageData;

    public MessageEvent(Messenger sender, Messenger receiver, MessageData messageData) {
        super();
        this.sender = sender;
        this.receiver = receiver;
        this.messageData = messageData;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Messenger getSender() {
        return this.sender;
    }

    public Messenger getReceiver() {
        return this.receiver;
    }

    public MessageData getMessageData() {
        return this.messageData;
    }

}
