package me.naptie.bukkit.lobby.commands;

import me.naptie.bukkit.lobby.Messages;
import me.naptie.bukkit.lobby.utils.CU;
import me.naptie.bukkit.inventory.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

public class ViewPlayers implements CommandExecutor {

	public static Set<Player> playersHiding = new HashSet<>();

	public static void changeItem(Player player) {
		ItemStack stack;
		if (playersHiding.contains(player)) {
			stack = new ItemStack(Material.GRAY_DYE);
			ItemMeta stackMeta = stack.getItemMeta();
			stackMeta.setDisplayName(Messages.getMessage(player, "PLAYER_VISIBILITY_NAME_OFF"));
			stackMeta.setLore(CU.t(me.naptie.bukkit.lobby.Main.getInstance().getConfig().getStringList("items.playervisibility.lore." + me.naptie.bukkit.player.utils.ConfigManager.getLanguageName(player))));
			stack.setItemMeta(stackMeta);
		} else {
			stack = new ItemStack(Material.LIME_DYE);
			ItemMeta stackMeta = stack.getItemMeta();
			stackMeta.setDisplayName(Messages.getMessage(player, "PLAYER_VISIBILITY_NAME_ON"));
			stackMeta.setLore(CU.t(me.naptie.bukkit.lobby.Main.getInstance().getConfig().getStringList("items.playervisibility.lore." + me.naptie.bukkit.player.utils.ConfigManager.getLanguageName(player))));
			stack.setItemMeta(stackMeta);
		}
		for (int slot = 0; slot <= 35; slot++) {
			if (player.getInventory().getItem(slot) != null) {
				if (player.getInventory().getItem(slot).getType().equals(Material.GRAY_DYE) || player.getInventory().getItem(slot).getType().equals(Material.LIME_DYE))
					player.getInventory().setItem(slot, stack);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			if (strings.length == 0) {
				if (playersHiding.contains(player)) {
					for (Player online : Bukkit.getOnlinePlayers())
						player.showPlayer(me.naptie.bukkit.lobby.Main.getInstance(), online);
					playersHiding.remove(player);
					changeItem(player);
					player.sendMessage(Messages.getMessage(player, "PLAYER_VISIBILITY_ON"));
					return true;
				} else {
					for (Player online : Bukkit.getOnlinePlayers())
						player.hidePlayer(me.naptie.bukkit.lobby.Main.getInstance(), online);
					playersHiding.add(player);
					changeItem(player);
					player.sendMessage(Messages.getMessage(player, "PLAYER_VISIBILITY_OFF"));
					return true;
				}
			} else {
				if (strings[0].equalsIgnoreCase("on")) {
					for (Player online : Bukkit.getOnlinePlayers())
						player.showPlayer(me.naptie.bukkit.lobby.Main.getInstance(), online);
					playersHiding.remove(player);
					changeItem(player);
					player.sendMessage(Messages.getMessage(player, "PLAYER_VISIBILITY_ON"));
					return true;
				} else if (strings[0].equalsIgnoreCase("off")) {
					for (Player online : Bukkit.getOnlinePlayers())
						player.hidePlayer(me.naptie.bukkit.lobby.Main.getInstance(), online);
					playersHiding.add(player);
					changeItem(player);
					player.sendMessage(Messages.getMessage(player, "PLAYER_VISIBILITY_OFF"));
					return true;
				} else {
					player.sendMessage(Messages.getMessage(player, "USAGE").replace("%usage%", "/viewplayers [on|off]"));
					return true;
				}
			}

		} else {
			commandSender.sendMessage(Messages.NOT_A_PLAYER);
			return true;
		}
	}
}
