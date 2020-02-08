package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.inventory.utils.ConfigManager;
import me.naptie.bukkit.lobby.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerMove implements Listener {

	private static Set<UUID> justProcessed = new HashSet<>();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();
		final UUID uuid = player.getUniqueId();

		if (justProcessed.contains(uuid)) {
			return;
		}
		if (location.getWorld().getName().toLowerCase().contains("lobby")) {
			if (player.getLocation().getBlock().getType().equals(Material.NETHER_PORTAL)) {
				if (Main.type.equalsIgnoreCase("lobby")) {
					Inventory inventory = ConfigManager.createMenu(player, "serverselector");
					player.openInventory(inventory);
				} else {
					me.naptie.bukkit.core.Main.getInstance().connect(player, "lobby01", false);
				}
				ExecutorService thread = Executors.newSingleThreadExecutor();
				thread.execute(() -> {
					justProcessed.add(uuid);
					try {
						TimeUnit.SECONDS.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					justProcessed.remove(uuid);
				});
				thread.shutdown();
			}

		}

	}

}
