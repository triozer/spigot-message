package fr.triozer.message.api.data;

/**
 * @author Cédric / Triozer
 */
public abstract class BaseMessageData implements MessageData {

    private final long time;

    public BaseMessageData() {
        this.time = System.currentTimeMillis();
    }

    @Override
    public long getTime() {
        return this.time;
    }

}
