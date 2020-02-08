package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.objects.LobbyScoreboard;
import me.naptie.bukkit.lobby.utils.DataManager;
import me.naptie.bukkit.lobby.utils.NametagManager;
import me.naptie.bukkit.rank.objects.Rank;
import me.naptie.bukkit.rank.utils.CU;
import me.naptie.bukkit.rank.utils.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        try {
            Objects.requireNonNull(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR)).unregister();
        } catch (NullPointerException ignored) {
        }
        LobbyScoreboard.getScoreboardMap().remove(player.getUniqueId());
        Executors.newSingleThreadExecutor().execute(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Main.mysql.editor.set(Main.id, "players", Bukkit.getOnlinePlayers().size());
			DataManager.deleteData(player);
		});
        String prefix = RankManager.getPrefix(player);
        String suffix = CU.t(RankManager.getRank(player).equals(Rank.MEMBER) ? "&7" : "&r");
        NametagManager.changePlayerName(player, prefix, suffix, NametagManager.TeamAction.DESTROY);
    }
}
