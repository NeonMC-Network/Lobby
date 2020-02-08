package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.lobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && event.getEntity().getWorld().getName().toLowerCase().contains("lobby")) {
			Player player = (Player) event.getEntity();
			if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID) || event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
				event.setCancelled(true);
				player.teleport(Main.spawn);
			} else {
				event.setCancelled(true);
			}
		}
	}

}
