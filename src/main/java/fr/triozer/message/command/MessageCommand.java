package fr.triozer.message.command;

import fr.triozer.message.MessagePlugin;
import fr.triozer.message.MessagePluginConfiguration;
import fr.triozer.message.player.PlayerMessenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {

   public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
      if (!(commandSender instanceof Player)) {
         MessagePlugin.LOG.error("Only a player can send messages with this command.");
         return true;
      }
      if (args.length == 0) {
         return false;
      }

      Player player = (Player) commandSender;
      PlayerMessenger sender = (PlayerMessenger) MessagePlugin.MESSENGERS.get(player.getUniqueId());

      if (args.length == 1) {
         if ("enable".equalsIgnoreCase(args[0])) {
            if (!sender.getSettings().hasEnabledPM()) {
               sender.getSettings().setPM(true);
               player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Message.PM_TOGGLE_ON));
            } else {
               player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Message.PM_ALREADY_ON));
            }
         } else if ("disable".equalsIgnoreCase(args[0])) {
            if (sender.getSettings().hasEnabledPM()) {
               sender.getSettings().setPM(false);
               player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Message.PM_TOGGLE_OFF));
            } else {
               player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Message.PM_ALREADY_OFF));
            }
         } else {
            player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Error.COMMAND_NO_MESSAGE.replace("{receiver}", args[0])));
         }
         return true;
      }

      Player target = Bukkit.getPlayerExact(args[0]);
      if (target == null) {
         player.sendMessage(MessagePluginConfiguration.t(MessagePluginConfiguration.Error.PLAYER_NOT_FOUND.replace("{player}", args[0])));
         return true;
      }

      StringBuilder message = new StringBuilder(args[1]);

      for(int i = 2; i < args.length; ++i) {
         message.append(" ").append(args[i]);
      }

      sender.send(MessagePlugin.MESSENGERS.get(target.getUniqueId()), message.toString());

      return true;
   }

}