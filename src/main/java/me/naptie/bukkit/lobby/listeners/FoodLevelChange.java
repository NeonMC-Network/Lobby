package me.naptie.bukkit.lobby.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player && event.getEntity().getWorld().getName().toLowerCase().contains("lobby")) {
			Player player = (Player) event.getEntity();
			player.setFoodLevel(20);
		}
	}

}
