package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Permissions;
import me.naptie.bukkit.lobby.utils.NametagManager;
import me.naptie.bukkit.rank.events.RankChangeEvent;
import me.naptie.bukkit.rank.utils.CU;
import me.naptie.bukkit.rank.utils.RankManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RankChange implements Listener {

	@EventHandler
	public void onRankChange(RankChangeEvent e) {
		Player player = e.getPlayer();
		if (player != null) {
			if (Main.scoreboardMap.containsKey(player.getUniqueId()))
				Main.scoreboardMap.get(player.getUniqueId()).update();
			NametagManager.changePlayerName(player, RankManager.getPrefix(player), CU.t("&r"), NametagManager.TeamAction.UPDATE);
			try {
				if (player.hasPermission(Permissions.FLIGHT)) {
					player.setAllowFlight(true);
					player.setFlying(true);
				} else {
					player.setFlying(false);
					player.setAllowFlight(false);
				}
			} catch (IllegalStateException ignored) {
			}
			if (Main.getInstance().getHologramManager() != null)
				Main.getInstance().getHologramManager().updateHolograms();
		}
	}

}
