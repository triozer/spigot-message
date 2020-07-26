package fr.triozer.message.command;

import fr.triozer.message.MessagePlugin;
import fr.triozer.message.MessagePluginConfiguration;
import fr.triozer.message.player.PlayerMessenger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor {

   public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
      if (!(commandSender instanceof Player)) {
         MessagePlugin.LOG.error("Only a player can send messages with this command.");
         return true;
      }

      Player player = (Player) commandSender;

      if (args.length == 0) {
         return false;
      }

      PlayerMessenger sender = (PlayerMessenger) MessagePlugin.MESSENGERS.get(player.getUniqueId());

      StringBuilder message = new StringBuilder(args[0]);

      for(int i = 1; i < args.length; ++i) {
         message.append(" ").append(args[i]);
      }

      sender.reply(message.toString());

      return true;
   }

}