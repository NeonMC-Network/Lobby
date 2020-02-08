package me.naptie.bukkit.lobby.utils;

import me.naptie.bukkit.lobby.objects.LobbyScoreboard;
import me.naptie.bukkit.rank.objects.Rank;
import me.naptie.bukkit.rank.utils.CU;
import me.naptie.bukkit.rank.utils.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.UUID;

public class NametagManager {

	public static void changePlayerName(Player player, String prefix, String suffix, TeamAction action) {
		if (action == null) {
			return;
		}

		for (UUID uuid : LobbyScoreboard.scoreboardMap.keySet()) {

			Scoreboard scoreboard = LobbyScoreboard.scoreboardMap.get(uuid);

			System.out.println("Updating " + prefix + player.getName() + suffix + " for " + Bukkit.getPlayer(uuid).getName() + "'s scoreboard " + scoreboard.toString().replace("org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboard@", ""));

			if (scoreboard.getTeam(player.getName()) == null) {
				scoreboard.registerNewTeam(player.getName());
			}

			Team team = scoreboard.getTeam(player.getName());
			assert team != null;
			team.setPrefix(CU.t(prefix));
			team.setSuffix(CU.t(suffix));
			team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
			// String rankName = prefix.replaceAll("[\\[\\]]", "");

			if (action.equals(TeamAction.CREATE)) {
				team.addEntry(player.getName());

			} else if (action.equals(TeamAction.UPDATE)) {
				team.unregister();
				scoreboard.registerNewTeam(player.getName());
				team = scoreboard.getTeam(player.getName());
				team.setPrefix(CU.t(prefix));
				team.setSuffix(CU.t(suffix));
				team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
				team.addEntry(player.getName());

			} else if (action.equals(TeamAction.DESTROY)) {
				team.unregister();
				try {
					Objects.requireNonNull(scoreboard.getObjective(player.getName())).unregister();
				} catch (NullPointerException ignored) {
				}
			}
		}
	}

	public static void update() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			String prefix = RankManager.getPrefix(RankManager.storage.get(player.getUniqueId()));
			String suffix = CU.t(RankManager.getRank(player).equals(Rank.MEMBER) ? "&7" : "&r");
			String displayName = prefix + player.getName() + suffix;
			player.setPlayerListName(displayName);
			player.setDisplayName(displayName);
			changePlayerName(player, prefix, suffix, TeamAction.UPDATE);
		}
	}

	public enum TeamAction {
		CREATE, DESTROY, UPDATE
	}

}
