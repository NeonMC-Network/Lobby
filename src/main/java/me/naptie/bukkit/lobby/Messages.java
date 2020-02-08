package me.naptie.bukkit.lobby;

import me.naptie.bukkit.lobby.utils.CU;
import me.naptie.bukkit.player.utils.ConfigManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages {

	public static final String NOT_A_PLAYER = getMessage("zh-CN", "NOT_A_PLAYER");

	public static String getMessage(YamlConfiguration language, String message) {
		return CU.t(language.getString(message));
	}

	public static String getMessage(String language, String message) {
		return CU.t(YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), language + ".yml")).getString(message));
	}

	public static String getMessage(OfflinePlayer player, String message) {
		return CU.t(getLanguage(player).getString(message));
	}

	private static YamlConfiguration getLanguage(OfflinePlayer player) {
		File locale = new File(Main.getInstance().getDataFolder(), ConfigManager.getLanguageName(player) + ".yml");
		return YamlConfiguration.loadConfiguration(locale);
	}

	public static String translate(String text, String fromLanguage, String toLanguage) {
		String result = "";
		YamlConfiguration sourceYaml = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), fromLanguage + ".yml"));
		YamlConfiguration targetYaml = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), toLanguage + ".yml"));
		for (String key : sourceYaml.getKeys(false)) {
			if (text.equalsIgnoreCase(sourceYaml.getString(key))) {
				result = targetYaml.getString(key);
			}
		}
		return result;
	}

}
