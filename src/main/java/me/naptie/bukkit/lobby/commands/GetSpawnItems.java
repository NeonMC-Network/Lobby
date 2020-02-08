package me.naptie.bukkit.lobby.commands;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Messages;
import me.naptie.bukkit.lobby.utils.CU;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class GetSpawnItems implements CommandExecutor {

	public static void getSpawnItems(Player player) {
		Inventory i = player.getInventory();
		me.naptie.bukkit.inventory.Main.getInstance().getConfig();
		FileConfiguration config = me.naptie.bukkit.inventory.Main.getInstance().getConfig();
		if (config.getConfigurationSection("items") != null) {
			i.clear();
			for (String item : Objects.requireNonNull(config.getConfigurationSection("items")).getKeys(false)) {
				if (config.getBoolean("items." + item + ".spawn")) {
					Material material = Material.valueOf(config.getString("items." + item + ".material"));
					int count = config.getInt("items." + item + ".count");
					int slot = config.getInt("items." + item + ".slot");
					ItemStack stack;
					boolean isPVItem = item.equals("playervisibility") && (material.equals(Material.GRAY_DYE) || material.equals(Material.LIME_DYE)) && slot == 7;
					if (isPVItem) {
						if (ViewPlayers.playersHiding.contains(player)) {
							stack = new ItemStack(Material.GRAY_DYE);
						} else {
							stack = new ItemStack(Material.LIME_DYE);
						}
					} else {
						stack = new ItemStack(material, count);
					}
					ItemMeta meta = stack.getItemMeta();
					System.out.println("items." + item + ".name." + me.naptie.bukkit.player.utils.ConfigManager.getLanguageName(player));
					meta.setDisplayName(CU.t(config.getString("items." + item + ".name." + me.naptie.bukkit.player.utils.ConfigManager.getLanguageName(player))));
					meta.setLore(CU.t(config.getStringList("items." + item + ".lore." + me.naptie.bukkit.player.utils.ConfigManager.getLanguageName(player))));
					if (material.equals(Material.PLAYER_HEAD)) {
						String owner = config.getString("items." + item + ".owner");
						if (owner.equalsIgnoreCase("?sender")) {
							((SkullMeta) meta).setOwningPlayer(player);
						} else {
							//noinspection deprecation
							((SkullMeta) meta).setOwner(owner);
						}
					}
					if (isPVItem) {
						if (ViewPlayers.playersHiding.contains(player)) {
							meta.setDisplayName(Messages.getMessage(player, "PLAYER_VISIBILITY_NAME_OFF"));
						} else {
							meta.setDisplayName(Messages.getMessage(player, "PLAYER_VISIBILITY_NAME_ON"));
						}
					}
					stack.setItemMeta(meta);
					i.setItem(slot, stack);
				}
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			getSpawnItems(player);
			return true;
		} else {
			commandSender.sendMessage(Messages.NOT_A_PLAYER);
			return true;
		}
	}

}
