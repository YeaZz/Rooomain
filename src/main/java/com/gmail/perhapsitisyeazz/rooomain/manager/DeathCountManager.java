package com.gmail.perhapsitisyeazz.rooomain.manager;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class DeathCountManager {

	private final Rooomain instance = Rooomain.getInstance();

	private final NamespacedKey deathKey = new NamespacedKey(instance, "death");

	public static final int maxDeath = 2;

	public void initialize(Player player) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		container.set(deathKey, PersistentDataType.INTEGER, 0);
	}

	public void addDeathToCounter(Player player) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		int death = getDeathCount(player);
		container.set(deathKey, PersistentDataType.INTEGER, death+1);
	}

	public int getDeathCount(Player player) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		return container.get(deathKey, PersistentDataType.INTEGER);
	}

	public boolean deathCountIsNotSet(Player player) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		return !container.has(deathKey, PersistentDataType.INTEGER);
	}
}
