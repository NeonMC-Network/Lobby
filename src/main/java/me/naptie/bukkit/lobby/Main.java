package me.naptie.bukkit.lobby;

import me.naptie.bukkit.core.utils.CoreStorage;
import me.naptie.bukkit.lobby.commands.*;
import me.naptie.bukkit.lobby.listeners.*;
import me.naptie.bukkit.lobby.commands.*;
import me.naptie.bukkit.lobby.listeners.*;
import me.naptie.bukkit.lobby.objects.LobbyScoreboard;
import me.naptie.bukkit.lobby.utils.HologramManager;
import me.naptie.bukkit.lobby.utils.MySQLManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;


public class Main extends JavaPlugin {

	public static Logger logger;
	public static Location spawn;
	public static Location flight;
	public static MySQLManager mysql;
	public static int id;
	public static String type;
	public static String serverName;
	public static String serverDisplayName;
	public static Map<UUID, LobbyScoreboard> scoreboardMap = new HashMap<>();
	private static Main instance;
	private PluginDescriptionFile descriptionFile;
	private HologramManager hologramManager;

	public static Main getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		logger = getLogger();
		descriptionFile = getDescription();
		getConfig().options().copyDefaults(true);
		getConfig().options().copyHeader(true);
		saveDefaultConfig();
		for (String language : getConfig().getStringList("languages")) {
			File localeFile = new File(getDataFolder(), language + ".yml");
			if (localeFile.exists()) {
				if (getConfig().getBoolean("update-language-files")) {
					saveResource(language + ".yml", true);
				}
			} else {
				saveResource(language + ".yml", false);
			}
		}
		World world = Bukkit.getServer().getWorld((Objects.requireNonNull(getConfig().getString("spawn.world"))));
		double x = getConfig().getDouble("spawn.x");
		double y = getConfig().getDouble("spawn.y");
		double z = getConfig().getDouble("spawn.z");
		float pitch = (float) getConfig().getDouble("spawn.pitch");
		float yaw = (float) getConfig().getDouble("spawn.yaw");
		spawn = new Location(world, x, y, z, yaw, pitch);
		flight = new Location(world, x, y + 2, z, yaw, pitch);
		registerCommands();
		registerEvents();
		serverName = CoreStorage.getServerName(getServer().getPort());
		mysql = new MySQLManager();
		boolean exists = false;
		for (int i : mysql.editor.getAllKeys()) {
			if (mysql.editor.get(i).get("server").equals(serverName)) {
				id = i;
				exists = true;
			}
		}
		while (!exists && mysql.editor.contains(id)) {
			id++;
		}
		String ints = serverName.replaceAll("\\D+", "");
		type = StringUtils.capitalize(serverName.replaceAll(ints, ""));
		serverDisplayName = (type.equalsIgnoreCase("lobby") ? "Main Lobby " : type + " Lobby ") + ints;
		mysql.editor.set(id, type, serverName, serverDisplayName, 0, getServer().getMaxPlayers(), getServer().getOnlinePlayers().size(), 0, "LOBBY", 1);
		for (Player player : Bukkit.getOnlinePlayers()) {
			LobbyScoreboard lobbyScoreboard = new LobbyScoreboard(player);
			scoreboardMap.put(player.getUniqueId(), lobbyScoreboard);
		}
		if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays") && !type.equalsIgnoreCase("lobby"))
			hologramManager = new HologramManager(this);

		logger.info("Enabled " + descriptionFile.getName() + " v" + descriptionFile.getVersion());
	}

	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			try {
				player.getScoreboard().getObjective(DisplaySlot.SIDEBAR).unregister();
			} catch (NullPointerException ignored) {
			}
		}
		getConfig().set("spawn.world", spawn.getWorld().getName());
		getConfig().set("spawn.x", spawn.getX());
		getConfig().set("spawn.y", spawn.getY());
		getConfig().set("spawn.z", spawn.getZ());
		getConfig().set("spawn.pitch", spawn.getPitch());
		getConfig().set("spawn.yaw", spawn.getYaw());
		saveConfig();
		mysql.editor.set(id, "players", 0);
		mysql.editor.set(id, "state", "ENDING");
		Set<Player> sentSet = new HashSet<>();
		for (int i : mysql.editor.getAllKeys()) {
			if (mysql.editor.get(i).get("type").equals(type) && ((String) mysql.editor.get(i).get("name")).contains("Lobby") && !mysql.editor.get(i).get("state").equals("ENDING")) {
				if ((int) mysql.editor.get(i).get("max") - (int) mysql.editor.get(i).get("players") >= Bukkit.getOnlinePlayers().size()) {
					boolean hasSent = false;
					for (Player player : Bukkit.getOnlinePlayers()) {
						if (!sentSet.contains(player) && !hasSent) {
							player.sendMessage(Messages.getMessage(player, "LOBBY_WENT_DOWN"));
							me.naptie.bukkit.core.Main.getInstance().connectWithoutChecking(player, (String) mysql.editor.get(i).get("server"), false);
							sentSet.add(player);
							hasSent = true;
						}
					}
				} else {
					int available = (int) mysql.editor.get(i).get("max") - (int) mysql.editor.get(i).get("players");
					int sent = 0;
					while (sent <= available) {
						boolean hasSent = false;
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (!sentSet.contains(player) && !hasSent) {
								player.sendMessage(Messages.getMessage(player, "LOBBY_WENT_DOWN"));
								me.naptie.bukkit.core.Main.getInstance().connectWithoutChecking(player, (String) mysql.editor.get(i).get("server"), false);
								sentSet.add(player);
								hasSent = true;
							}
						}
						sent++;
					}
				}
			}
		}
		if (Bukkit.getOnlinePlayers().size() > 0) {
			for (int i : mysql.editor.getAllKeys()) {
				if (mysql.editor.get(i).get("type").equals("Lobby") && !mysql.editor.get(i).get("state").equals("ENDING")) {
					if ((int) mysql.editor.get(i).get("max") - (int) mysql.editor.get(i).get("players") >= Bukkit.getOnlinePlayers().size()) {
						boolean hasSent = false;
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (!sentSet.contains(player) && !hasSent) {
								player.sendMessage(Messages.getMessage(player, "LOBBY_WENT_DOWN"));
								me.naptie.bukkit.core.Main.getInstance().connectWithoutChecking(player, (String) mysql.editor.get(i).get("server"), false);
								sentSet.add(player);
								hasSent = true;
							}
						}
					} else {
						int available = (int) mysql.editor.get(i).get("max") - (int) mysql.editor.get(i).get("players");
						int sent = 0;
						while (sent <= available) {
							boolean hasSent = false;
							for (Player player : Bukkit.getOnlinePlayers()) {
								if (!sentSet.contains(player) && !hasSent) {
									player.sendMessage(Messages.getMessage(player, "LOBBY_WENT_DOWN"));
									me.naptie.bukkit.core.Main.getInstance().connectWithoutChecking(player, (String) mysql.editor.get(i).get("server"), false);
									sentSet.add(player);
									hasSent = true;
								}
							}
							sent++;
						}
					}
				}
			}
		}
		mysql.editor.getMySQL().disconnect();
		if (Bukkit.getOnlinePlayers().size() > 0) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		instance = null;
		logger.info("Disabled " + descriptionFile.getName() + " v" + descriptionFile.getVersion());
		logger = null;
	}

	private void registerCommands() {
		getCommand("getspawnitems").setExecutor(new GetSpawnItems());
		getCommand("setlobby").setExecutor(new SetSpawn());
		getCommand("selectlobby").setExecutor(new SelectLobby());
		getCommand("spawn").setExecutor(new Spawn());
		getCommand("fly").setExecutor(new Fly());
		getCommand("viewplayers").setExecutor(new ViewPlayers());
		getCommand("teleporttoparkour").setExecutor(new TeleportToParkour());
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(new EntityDamage(), this);
		pm.registerEvents(new FoodLevelChange(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new PlayerInteractAtEntity(), this);
		pm.registerEvents(new PlayerMove(), this);
		pm.registerEvents(new BlockPlace(), this);
		pm.registerEvents(new BlockBreak(), this);
		pm.registerEvents(new InventoryClick(), this);
		pm.registerEvents(new RankChange(), this);
		pm.registerEvents(new LanguageChange(), this);
		pm.registerEvents(new ExperienceUpdate(), this);
		pm.registerEvents(new WeatherChange(), this);
	}

	public HologramManager getHologramManager() {
		return hologramManager;
	}
}
