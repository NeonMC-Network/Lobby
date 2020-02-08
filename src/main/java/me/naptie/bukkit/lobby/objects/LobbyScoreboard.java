package me.naptie.bukkit.lobby.objects;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Messages;
import me.naptie.bukkit.lobby.utils.CU;
import me.naptie.bukkit.player.utils.ConfigManager;
import me.naptie.bukkit.rank.utils.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class LobbyScoreboard {

	private Player player;
	private List<String> lastStrings = new ArrayList<>(5);
	public static Map<UUID, Scoreboard> scoreboardMap = new HashMap<>();
	private Scoreboard board;

	public LobbyScoreboard(Player player) {
		System.out.println("Setting up scoreboard for " + player.getName());
		this.player = player;
		this.board = this.player.getScoreboard();
		if (this.board == null || scoreboardMap.containsValue(this.board)) {
			this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		}
		scoreboardMap.put(player.getUniqueId(), this.board);
		System.out.println("Added " + player.getName() + "'s scoreboard " + board.toString().replace("org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboard@", "") + " to the scoreboard map");
		String score10 = Messages.getMessage(player, "YOU") + ": " + (this.player.getDisplayName().contains("] ") ? this.player.getDisplayName().split("] ")[1] : this.player.getDisplayName());
		String score8 = CU.t(Messages.getMessage(player, "RANK") + ": " + RankManager.getRankName(this.player));
		String score6 = CU.t(Messages.getMessage(player, "LEVEL") + ": &a" + ConfigManager.getLevel(this.player));
		String score4 = CU.t(Messages.getMessage(player, "POINT") + ": &a" + (Objects.nonNull(ConfigManager.getData(this.player).getString("point")) ? ConfigManager.getData(this.player).getString("point") : "0"));
		String score2 = CU.t(Messages.getMessage(player, "SERVER") + ": &a" + Messages.getMessage(player, "LOBBY").replace("%type%", Messages.translate(Main.serverDisplayName.split(" Lobby")[0], "en-US", ConfigManager.getLanguageName(player))).replace("%number%", Integer.valueOf(Main.serverName.replaceAll("\\D+", "")) + ""));
		this.lastStrings.add(score10);
		this.lastStrings.add(score8);
		this.lastStrings.add(score6);
		this.lastStrings.add(score4);
		this.lastStrings.add(score2);
		System.out.println("The identifier of scoreboard for " + player.getName() + " is " + board.toString().replace("org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboard@", ""));
		Objective objective = board.registerNewObjective("Lobby", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(CU.t(Main.getInstance().getConfig().getString("scoreboard.title")));
		objective.getScore(CU.t("&a")).setScore(11);
		objective.getScore(score10).setScore(10);
		objective.getScore(CU.t("&b")).setScore(9);
		objective.getScore(score8).setScore(8);
		objective.getScore(CU.t("&c")).setScore(7);
		objective.getScore(score6).setScore(6);
		objective.getScore(CU.t("&e")).setScore(5);
		objective.getScore(score4).setScore(4);
		objective.getScore(CU.t("&6")).setScore(3);
		objective.getScore(score2).setScore(2);
		objective.getScore(CU.t("&7")).setScore(1);
		objective.getScore(CU.t("&eNeonMC Network")).setScore(0);

		this.player.setScoreboard(board);
		System.out.println("Objective " + objective.getName() + " of scoreboard " + board.toString().replace("org.bukkit.craftbukkit.v1_15_R1.scoreboard.CraftScoreboard@", "") + " displayed to " + player.getName());
	}

	public void update() {
		for (String lastString : this.lastStrings) {
			board.resetScores(lastString);
		}
		Objective objective = board.getObjective("Lobby");
		String score10 = Messages.getMessage(player, "YOU") + ": " + (this.player.getDisplayName().contains(" ") ? this.player.getDisplayName().split(" ")[1] : this.player.getDisplayName());
		String score8 = CU.t(Messages.getMessage(player, "RANK") + ": " + RankManager.getRankName(this.player));
		String score6 = CU.t(Messages.getMessage(player, "LEVEL") + ": " + ConfigManager.getLevel(this.player));
		String score4 = CU.t(Messages.getMessage(player, "POINT") + ": &a" + ConfigManager.getData(this.player).getString("point"));
		String score2 = CU.t(Messages.getMessage(player, "SERVER") + ": &a" + Messages.getMessage(player, "LOBBY").replace("%type%", Messages.translate(Main.serverDisplayName.split(" Lobby")[0], "en-US", ConfigManager.getLanguageName(player))).replace("%number%", Integer.valueOf(Main.serverName.replaceAll("\\D+", "")) + ""));
		this.lastStrings.set(0, score10);
		this.lastStrings.set(1, score8);
		this.lastStrings.set(2, score6);
		this.lastStrings.set(3, score4);
		this.lastStrings.set(4, score2);
		objective.getScore(score10).setScore(10);
		objective.getScore(score8).setScore(8);
		objective.getScore(score6).setScore(6);
		objective.getScore(score4).setScore(4);
		objective.getScore(score2).setScore(2);
	}

	public static Map<UUID, Scoreboard> getScoreboardMap() {
		return scoreboardMap;
	}

}
