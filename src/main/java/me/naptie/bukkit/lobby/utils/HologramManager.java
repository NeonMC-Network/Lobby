package me.naptie.bukkit.lobby.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.player.utils.ConfigManager;
import me.naptie.bukkit.player.utils.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class HologramManager {

	private Plugin plugin;
	private FileConfiguration config;
	private Map<Hologram, String> hologramMap = new HashMap<>();

	public HologramManager(Plugin plugin) {
		this.plugin = plugin;
		config = Main.getInstance().getConfig();
		createHolograms();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::updateHolograms, config.getLong("holograms-update-in-ticks"), config.getLong("holograms-update-in-ticks"));
	}

	private void createHolograms() {
		for (String key : config.getConfigurationSection("holograms").getKeys(false)) {
			World world = Bukkit.getWorld(config.getString("holograms." + key + ".world"));
			double x = config.getDouble("holograms." + key + ".x");
			double y = config.getDouble("holograms." + key + ".y");
			double z = config.getDouble("holograms." + key + ".z");
			Location location = new Location(world, x, y, z);
			Hologram hologram = HologramsAPI.createHologram(plugin, location);
			hologram.appendTextLine(CU.t(config.getString("holograms." + key + ".header").replace("%game%", Main.type)));
			hologram.appendTextLine("");
			if (key.contains("top")) {
				int max = 10;
				Map<UUID, Integer> sourceMap = new HashMap<>();
				for (File file : Objects.requireNonNull(ConfigManager.getDataFolder().listFiles())) {
					if (file.getName().endsWith(".yml")) {
						YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
						sourceMap.put(UUID.fromString(file.getName().replace(".yml", "")), data.getInt(Main.type.toLowerCase() + "." + key.split("-")[1]));
						max = config.getInt("holograms." + key + ".size");
					}
				}
				int i = 0;
				for (Iterator<Map<UUID, Integer>> it = MapUtil.sort(sourceMap).iterator(); it.hasNext() && i < max; ++i) {
					Map<UUID, Integer> each = it.next();
					for (UUID uuid : each.keySet())
						hologram.appendTextLine(CU.t(config.getString("holograms." + key + ".format").replace("%pos%", i + 1 + "").replace("%player%", RankManager.getRankedName(uuid, me.naptie.bukkit.rank.utils.RankManager.getRank(Bukkit.getOfflinePlayer(uuid)).name())).replace("%data%", each.get(uuid) + "")));
				}
			}
			hologramMap.put(hologram, key);
		}
	}

	public void updateHolograms() {
		for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
			hologram.clearLines();
			String key = hologramMap.get(hologram);
			hologram.appendTextLine(CU.t(config.getString("holograms." + key + ".header").replace("%game%", Main.type)));
			hologram.appendTextLine("");
			if (key.contains("top")) {
				int max = 10;
				Map<UUID, Integer> sourceMap = new HashMap<>();
				for (File file : Objects.requireNonNull(ConfigManager.getDataFolder().listFiles())) {
					if (file.getName().endsWith(".yml")) {
						YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
						sourceMap.put(UUID.fromString(file.getName().replace(".yml", "")), data.getInt(Main.type.toLowerCase() + "." + key.split("-")[1]));
						max = config.getInt("holograms." + key + ".size");
					}
				}
				int i = 0;
				for (Iterator<Map<UUID, Integer>> it = MapUtil.sort(sourceMap).iterator(); it.hasNext() && i < max; ++i) {
					Map<UUID, Integer> each = it.next();
					for (UUID uuid : each.keySet())
						hologram.appendTextLine(CU.t(config.getString("holograms." + key + ".format").replace("%pos%", i + 1 + "").replace("%player%", RankManager.getRankedName(uuid, me.naptie.bukkit.rank.utils.RankManager.getRank(Bukkit.getOfflinePlayer(uuid)).name())).replace("%data%", each.get(uuid) + "")));
				}
			}
		}
	}

}
