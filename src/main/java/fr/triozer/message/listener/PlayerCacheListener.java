package fr.triozer.message.listener;

import fr.triozer.message.MessagePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerCacheListener implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        MessagePlugin.registerPlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        MessagePlugin.MESSENGERS.remove(event.getPlayer().getUniqueId());
    }

}
