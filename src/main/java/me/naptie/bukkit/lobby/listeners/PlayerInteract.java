package me.naptie.bukkit.lobby.listeners;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Messages;
import me.naptie.bukkit.lobby.commands.ViewPlayers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class PlayerInteract implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (event.getAction() == Action.PHYSICAL && Objects.requireNonNull(event.getClickedBlock()).getType() == Material.FARMLAND)
			event.setCancelled(true);

		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getItem() != null) {
				if ((event.getItem().getType().equals(Material.GRAY_DYE) || event.getItem().getType().equals(Material.LIME_DYE)) && player.getInventory().getHeldItemSlot() == 7) {
					if (ViewPlayers.playersHiding.contains(player)) {
						for (Player online : Bukkit.getOnlinePlayers())
							player.showPlayer(Main.getInstance(), online);
						ViewPlayers.playersHiding.remove(player);
						ViewPlayers.changeItem(player);
						player.sendMessage(Messages.getMessage(player, "PLAYER_VISIBILITY_ON"));
					} else {
						for (Player online : Bukkit.getOnlinePlayers())
							player.hidePlayer(Main.getInstance(), online);
						ViewPlayers.playersHiding.add(player);
						ViewPlayers.changeItem(player);
						player.sendMessage(Messages.getMessage(player, "PLAYER_VISIBILITY_OFF"));
					}
				}
			}
		}
	}
}
