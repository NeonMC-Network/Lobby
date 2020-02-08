package me.naptie.bukkit.lobby.commands;

import me.naptie.bukkit.lobby.Main;
import me.naptie.bukkit.lobby.Messages;
import me.naptie.bukkit.lobby.objects.LobbyServer;
import me.naptie.bukkit.lobby.utils.CU;
import me.naptie.bukkit.player.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectLobby implements CommandExecutor {

	private static List<LobbyServer> lobbies = new ArrayList<>();
	private static Map<Player, List<ItemStack>> serverItems = new HashMap<>();

	public static LobbyServer getServer(Player player, ItemStack stack) {
		for (ItemStack reference : serverItems.get(player)) {
			if (stack.isSimilar(reference)) {
				for (LobbyServer server : lobbies) {
					if (Integer.parseInt(server.getDisplayName().replaceAll("\\D+", "")) == stack.getAmount()) {
						return server;
					}
				}
			}
		}
		for (LobbyServer server : lobbies) {
			if (Integer.parseInt(server.getDisplayName().replaceAll("\\D+", "")) == stack.getAmount()) {
				return server;
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			lobbies.clear();
			List<ItemStack> serverItems = new ArrayList<>();
			for (int i : Main.mysql.editor.getAllKeys()) {
				if (Main.mysql.editor.get(i).get("type").equals(Main.type) && ((String) Main.mysql.editor.get(i).get("name")).contains("Lobby")) {
					LobbyServer lobby = new LobbyServer(i, (String) Main.mysql.editor.get(i).get("type"), (String) Main.mysql.editor.get(i).get("server"), (String) Main.mysql.editor.get(i).get("name"), (int) Main.mysql.editor.get(i).get("max"), (int) Main.mysql.editor.get(i).get("players"), !Main.mysql.editor.get(i).get("state").equals("ENDING"));
					lobbies.add(lobby);
				}
			}
			int serverCount = lobbies.size();
			int lineCount = serverCount / 9;
			int remainder = serverCount % 9;
			int extraLineCount = remainder == 0 ? 0 : 1;
			Inventory inv = Bukkit.createInventory(null, 9 * (lineCount + extraLineCount + 2), Messages.getMessage(player, "LOBBY_SELECTOR").replace("%type%", Messages.translate(Main.serverDisplayName.split(" Lobby")[0], "en-US", ConfigManager.getLanguageName(player))));
			for (int i = 0; i <= serverCount; i++) {
				serverItems.add(new ItemStack(Material.AIR));
			}
			for (LobbyServer server : lobbies) {
				ItemStack serverItem;
				int number = Integer.parseInt(server.getDisplayName().replaceAll("\\D+", ""));
				if (server.isOn()) {
					if (server.getId() == Main.id) {
						serverItem = new ItemStack(Material.RED_TERRACOTTA);
					} else {
						serverItem = new ItemStack(Material.QUARTZ_BLOCK, number);
					}
				} else {
					serverItem = new ItemStack(Material.BLACK_TERRACOTTA);
				}
				ItemMeta meta = serverItem.getItemMeta();
				meta.setDisplayName(CU.t(server.isOn() ? (server.getId() == Main.id ? "&c" : "&a") : "&c") + Messages.getMessage(player, "LOBBY").replace("%type%", Messages.translate(Main.serverDisplayName.split(" Lobby")[0], "en-US", ConfigManager.getLanguageName(player))).replace("%number%", number + ""));
				List<String> lore = new ArrayList<>();
				lore.add(Messages.getMessage(player, "ONLINE_PLAYER_COUNT").replace("%online%", server.getOnlinePlayers() + "").replace("%max%", server.getMaxPlayers() + ""));
				lore.add("");
				lore.add(Messages.getMessage(player, server.isOn() ? (server.getId() == Main.id ? "ALREADY_CONNECTED" : "CLICK_TO_CONNECT") : "SERVER_OFFLINE"));
				meta.setLore(lore);
				serverItem.setItemMeta(meta);
				serverItems.set(number, serverItem);
			}
			for (int i = 0; i < serverItems.size(); i++) {
				inv.setItem(8 + i, serverItems.get(i));
			}
			player.openInventory(inv);
			SelectLobby.serverItems.put(player, serverItems);
			return true;
		} else {
			commandSender.sendMessage(Messages.NOT_A_PLAYER);
			return true;
		}
	}

}
