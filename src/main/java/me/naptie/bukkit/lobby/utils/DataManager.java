package me.naptie.bukkit.lobby.utils;

import me.naptie.bukkit.lobby.Main;
import org.bukkit.OfflinePlayer;

import java.io.File;

public class DataManager {

    private static String world = null;

    public static void deleteData(OfflinePlayer player) {
        if (world == null) {
            world = Main.getInstance().getConfig().getString("spawn.world");
        }
        deleteFile(new File(world + "/playerdata", player.getUniqueId() + ".dat"));
        deleteFile(new File(world + "/stats", player.getUniqueId() + ".json"));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

}
