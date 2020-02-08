package me.naptie.bukkit.lobby.objects;

public class LobbyServer {

	private int id, max, players;
	private String type, server, name;
	private boolean on;

	public LobbyServer(int id, String type, String server, String name, int max, int players, boolean on) {
		this.id = id;
		this.type = type;
		this.server = server;
		this.name = name;
		this.max = max;
		this.players = players;
		this.on = on;
	}

	public int getId() {
		return id;
	}

	public int getMaxPlayers() {
		return max;
	}

	public int getOnlinePlayers() {
		return players;
	}

	public String getType() {
		return type;
	}

	public String getServerName() {
		return server;
	}

	public String getDisplayName() {
		return name;
	}

	public boolean isOn() {
		return on;
	}
}
