package fr.triozer.message;

import fr.triozer.message.util.YamlUtil;
import net.md_5.bungee.api.ChatColor;

public final class MessagePluginConfiguration {

    public static String t(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public final static class Message {
        public static final String PM_TOGGLE_ON   = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "messages.toggle-on", "");
        public static final String PM_TOGGLE_OFF  = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "messages.toggle-off", "");
        public static final String PM_DISABLED    = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "messages.disabled", "");
        public static final String PM_ALREADY_ON  = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "messages.already-on", "");
        public static final String PM_ALREADY_OFF = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "messages.already-off", "");
    }

    public final static class Error {
        public static final String COMMAND_NO_MESSAGE = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "errors.command.no-message", "");
        public static final String PLAYER_NOT_FOUND   = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "errors.cant-find-player", "");
        public static final String PSYCHO             = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "errors.no-psycho", "");
        public static final String SPAM               = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "errors.spam", "");
        public static final String SAME_MESSAGE       = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "errors.same-message", "");
        public static final String NO_REPLY           = YamlUtil.get(MessagePlugin.INSTANCE.getMessages(), "errors.no-reply", "");
    }

    public final static class Format {
        public static final String IN  = YamlUtil.get(MessagePlugin.INSTANCE.getConfig(), "format.in", "");
        public static final String OUT = YamlUtil.get(MessagePlugin.INSTANCE.getConfig(), "format.out", "");
    }

    public final static class Option {
        public static final boolean PSYCHO                 = YamlUtil.get(MessagePlugin.INSTANCE.getConfig(), "options.psycho", false);
        public static final boolean ANTI_SPAM              = YamlUtil.get(MessagePlugin.INSTANCE.getConfig(), "options.anti-spam.enabled", true);
        public static final int     ANTI_SPAM_INTERVAL     = YamlUtil.get(MessagePlugin.INSTANCE.getConfig(), "options.anti-spam.interval", 3);
        public static final int     ANTI_SPAM_SAME_MESSAGE = YamlUtil.get(MessagePlugin.INSTANCE.getConfig(), "options.anti-spam.same-message", -1);
    }

    public final static class Permission {
        public static final String BYPASS_MESSAGE = YamlUtil.get(MessagePlugin.INSTANCE.getConfig(), "permissions.bypass.message", "message.bypass.message");
        public static final String BYPASS_SPAM    = YamlUtil.get(MessagePlugin.INSTANCE.getConfig(), "permissions.bypass.anti-spam", "message.bypass.anti-spam");
    }

}
