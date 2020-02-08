package me.naptie.bukkit.lobby.utils;

import java.util.*;

public class MapUtil {

	public static List<Map<UUID, Integer>> sort(Map<UUID, Integer> data) {
		final List<Map<UUID, Integer>> comparison = new ArrayList<>();
		for (UUID uuid : data.keySet()) {
			Map<UUID, Integer> each = new HashMap<>();
			each.put(uuid, data.get(uuid));
			comparison.add(each);
		}
		comparison.sort((o1, o2) -> {
			int comparison1 = 0, comparison2 = 0;
			for (UUID uuid : o1.keySet()) {
				comparison1 = o1.get(uuid);
			}
			for (UUID uuid : o2.keySet()) {
				comparison2 = o2.get(uuid);
			}
			return comparison2 - comparison1;
		});
		return comparison;
	}

}
