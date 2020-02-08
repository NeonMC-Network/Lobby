package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.core.commands.EditInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.getWorld().getName().toLowerCase().contains("lobby") && !EditInventory.getEditable(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}

}
