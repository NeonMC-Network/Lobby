package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.player.events.ExperienceUpdateEvent;
import me.naptie.bukkit.player.utils.ConfigManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ExperienceUpdate implements Listener {

	@EventHandler
	public void onExperienceUpdate(ExperienceUpdateEvent e) {
		OfflinePlayer offlinePlayer = e.getPlayer();
		if (offlinePlayer.isOnline()) {
			Player player = (Player) offlinePlayer;
			Main.scoreboardMap.get(player.getUniqueId()).update();
			player.setLevel(ConfigManager.getLevel(e.getCurrentPoint()));
		}
	}
}
