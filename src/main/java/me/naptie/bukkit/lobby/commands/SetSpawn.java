package me.naptie.bukkit.lobby.commands;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Messages;
import me.naptie.bukkit.lobby.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			if (player.hasPermission(Permissions.SET_SPAWN)) {
				if (strings.length == 0) {
					Main.spawn = player.getLocation();
					player.sendMessage(Messages.getMessage(player, "SPAWN_SET"));
					return true;
				} else if (strings.length >= 6) {
					World world = Bukkit.getWorld(strings[0]);
					if (world == null) {
						player.sendMessage(Messages.getMessage(player, "WORLD_NOT_FOUND"));
						return true;
					}
					double x = Double.parseDouble(strings[1]);
					double y = Double.parseDouble(strings[2]);
					double z = Double.parseDouble(strings[3]);
					float pitch = Float.parseFloat(strings[4]);
					float yaw = Float.parseFloat(strings[5]);
					Main.spawn = new Location(world, x, y, z, yaw, pitch);
					Main.flight = new Location(world, x, y + 2, z, yaw, pitch);
					player.sendMessage(Messages.getMessage(player, "SPAWN_SET"));
					return true;
				} else {
					player.sendMessage(Messages.getMessage(player, "USAGE").replace("%usage%", "/setspawn [world] [x] [y] [z] [pitch] [yaw]"));
					return true;
				}
			} else {
				player.sendMessage(Messages.getMessage(player, "PERMISSION_DENIED"));
				return true;
			}
		} else {
			if (strings.length >= 6) {
				World world = Bukkit.getWorld(strings[0]);
				if (world == null) {
					commandSender.sendMessage(Messages.getMessage("zh-CN", "WORLD_NOT_FOUND"));
					return true;
				}
				double x = Double.parseDouble(strings[1]);
				double y = Double.parseDouble(strings[2]);
				double z = Double.parseDouble(strings[3]);
				float pitch = Float.parseFloat(strings[4]);
				float yaw = Float.parseFloat(strings[5]);
				Main.spawn = new Location(world, x, y, z, yaw, pitch);
				Main.flight = new Location(world, x, y + 2, z, yaw, pitch);
				commandSender.sendMessage(Messages.getMessage("zh-CN", "SPAWN_SET"));
				return true;
			} else {
				commandSender.sendMessage(Messages.getMessage("zh-CN", "USAGE").replace("%usage%", "setspawn [world] [x] [y] [z] [pitch] [yaw]"));
				return true;
			}
		}
	}

}
