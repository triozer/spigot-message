package fr.triozer.message.data;

import fr.triozer.message.api.data.BaseMessageData;

/**
 * @author Cédric / Triozer
 */
public class TextMessageData extends BaseMessageData {

    private final String message;

    public TextMessageData(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessageContent() {
        return this.message;
    }

}
