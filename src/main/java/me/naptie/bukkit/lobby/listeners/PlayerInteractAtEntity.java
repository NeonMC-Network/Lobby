package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.core.commands.EditInventory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtEntity implements Listener {

	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked() instanceof ArmorStand) {
			ArmorStand clicked = (ArmorStand) event.getRightClicked();
			if (!EditInventory.getEditable(event.getPlayer().getUniqueId()) && !clicked.hasBasePlate()) {
				event.setCancelled(true);
			}
		}
	}

}