package fr.triozer.message.api.messenger;

/**
 * @author Cédric / Triozer
 */
public class MessengerSettings {

    private boolean enabledPM;

    public MessengerSettings() {
        this.enabledPM = true;
    }

    public boolean hasEnabledPM() {
        return this.enabledPM;
    }

    public void setPM(boolean enabledPM) {
        this.enabledPM = enabledPM;
    }

}
