package me.naptie.bukkit.lobby.commands;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			if (player.isFlying()) {
				player.teleport(Main.flight);
			} else {
				player.teleport(Main.spawn);
			}
			return true;
		} else {
			commandSender.sendMessage(Messages.NOT_A_PLAYER);
			return true;
		}
	}

}
