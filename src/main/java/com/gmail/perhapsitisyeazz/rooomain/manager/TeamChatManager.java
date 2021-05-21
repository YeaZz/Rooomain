package com.gmail.perhapsitisyeazz.rooomain.manager;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class TeamChatManager {

	private final Rooomain instance = Rooomain.getInstance();

	private final NamespacedKey teamChatKey = new NamespacedKey(instance, "team_chat");

	public void initialize(Player player) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		container.set(teamChatKey, PersistentDataType.INTEGER, 0);
	}

	public void setTeamChat(Player player, boolean teamChat) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		int i = teamChat ? 1 : 0;
		container.set(teamChatKey, PersistentDataType.INTEGER, i);
	}

	public boolean teamChatIsNotSet(Player player) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		return !container.has(teamChatKey, PersistentDataType.INTEGER);
	}

	public boolean isInTeamChat(Player player) {
		PersistentDataContainer container = player.getPersistentDataContainer();
		int i = container.get(teamChatKey, PersistentDataType.INTEGER);
		return i == 1;
	}
}
