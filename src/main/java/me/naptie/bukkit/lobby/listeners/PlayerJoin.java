package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Permissions;
import me.naptie.bukkit.lobby.commands.GetSpawnItems;
import me.naptie.bukkit.lobby.commands.ViewPlayers;
import me.naptie.bukkit.lobby.objects.LobbyScoreboard;
import me.naptie.bukkit.lobby.utils.NametagManager;
import me.naptie.bukkit.player.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class PlayerJoin implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
		if (player.hasPermission(Permissions.FLIGHT)) {
			player.setAllowFlight(true);
			player.setFlying(true);
			player.teleport(Main.flight);
		} else {
			player.teleport(Main.spawn);
		}
		for (Player target : ViewPlayers.playersHiding) {
			target.hidePlayer(player);
		}
		GetSpawnItems.getSpawnItems(player);
		player.setLevel(ConfigManager.getLevel(ConfigManager.getData(player).getInt("point")));
		Main.scoreboardMap.put(player.getUniqueId(), new LobbyScoreboard(player));
		Main.mysql.editor.set(Main.id, "players", Bukkit.getOnlinePlayers().size());
		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException ignored) {
		}
		NametagManager.update();
		if (Main.getInstance().getHologramManager() != null)
			Main.getInstance().getHologramManager().updateHolograms();
	}

}
