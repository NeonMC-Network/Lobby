package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.core.Main;
import me.naptie.bukkit.lobby.Messages;
import me.naptie.bukkit.lobby.commands.SelectLobby;
import me.naptie.bukkit.lobby.objects.LobbyServer;
import me.naptie.bukkit.player.utils.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getClickedInventory() != null) {
                InventoryView view = event.getView();
                if (view.getTitle().equals(Messages.getMessage(player, "LOBBY_SELECTOR").replace("%type%", Messages.translate(me.naptie.bukkit.lobby.Main.serverDisplayName.split(" Lobby")[0], "en-US", ConfigManager.getLanguageName(player))))) {
                    LobbyServer server = SelectLobby.getServer(player, event.getCurrentItem());
                    if (server != null) {
                        Main.getInstance().connect(player, server.getServerName(), true);
                        player.closeInventory();
                    }
                }
            }
        }
    }
}
