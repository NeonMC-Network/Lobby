package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.commands.GetSpawnItems;
import me.naptie.bukkit.player.events.LanguageChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LanguageChange implements Listener {

	@EventHandler
	public void onLanguageChange(LanguageChangeEvent e) {
		Player player = e.getPlayer();
		if (player != null) {
			Main.scoreboardMap.get(player.getUniqueId()).update();
			if (e.isChangingItemName())
				GetSpawnItems.getSpawnItems(player);
		}
	}

}
