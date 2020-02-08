package me.naptie.bukkit.lobby.commands;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class TeleportToParkour implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			World world = Bukkit.getWorld(Objects.requireNonNull(Main.getInstance().getConfig().getString("parkour.world")));
			double x = Main.getInstance().getConfig().getDouble("parkour.x");
			double y = Main.getInstance().getConfig().getDouble("parkour.y");
			double z = Main.getInstance().getConfig().getDouble("parkour.z");
			float pitch = (float) Main.getInstance().getConfig().getDouble("parkour.pitch");
			float yaw = (float) Main.getInstance().getConfig().getDouble("parkour.yaw");
			Location parkour = new Location(world, x, y, z, yaw, pitch);
			player.teleport(parkour);
			return true;
		} else {
			commandSender.sendMessage(Messages.NOT_A_PLAYER);
			return true;
		}
	}
}
