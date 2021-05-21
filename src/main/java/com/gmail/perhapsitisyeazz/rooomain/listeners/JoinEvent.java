package com.gmail.perhapsitisyeazz.rooomain.listeners;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import com.gmail.perhapsitisyeazz.rooomain.manager.DeathCountManager;
import com.gmail.perhapsitisyeazz.rooomain.manager.DirectionManager;
import com.gmail.perhapsitisyeazz.rooomain.manager.ScoreboardManager;
import com.gmail.perhapsitisyeazz.rooomain.manager.TeamChatManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvent implements Listener {

	private final TeamChatManager teamChatManager = new TeamChatManager();
	private final DirectionManager directionManager = new DirectionManager();
	private final DeathCountManager deathCountManager = new DeathCountManager();

	public Rooomain main;
	public JoinEvent(Rooomain main) {
		this.main = main;
	}

	@EventHandler
	private void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ScoreboardManager.setScoreboard(player);
		if (Bukkit.getOnlinePlayers().size() == 1)
			ScoreboardManager.updateScoreboardTime();
		ScoreboardManager.updateScoreboardOnlineCounter();
		if (teamChatManager.teamChatIsNotSet(player))
			teamChatManager.initialize(player);
		if (deathCountManager.deathCountIsNotSet(player))
			deathCountManager.initialize(player);
		if (deathCountManager.getDeathCount(player) > 1) {
			event.joinMessage(Component.text().color(NamedTextColor.GRAY)
					.append(Component.text("» ", NamedTextColor.DARK_GRAY),
							Component.text(player.getName(), NamedTextColor.AQUA),
							Component.text(" a rejoint en tant que spectateur")).build());
			TextComponent component = Component.text().color(NamedTextColor.GRAY)
					.append(Component.text("» ", NamedTextColor.DARK_GRAY),
							Component.text("Vous ne faite plus parti du monde des mortels."), Component.newline(),
							Component.text("»", NamedTextColor.DARK_GRAY),
							Component.text("Cependant, vous pouvez toujours les observer.")).build();
			player.sendMessage(component);
			player.setGameMode(GameMode.SPECTATOR);
			return;
		}
		event.joinMessage(Component.text().color(NamedTextColor.GRAY)
				.append(Component.text("» ", NamedTextColor.DARK_GRAY),
						Component.text(player.getName(), NamedTextColor.AQUA),
						Component.text(" a rejoint l'aventure")).build());
		directionManager.teamDirection(player);
	}

	@EventHandler
	private void playerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.quitMessage(Component.text().color(NamedTextColor.GRAY)
				.append(Component.text("» ", NamedTextColor.DARK_GRAY),
						Component.text(player.getName(), NamedTextColor.AQUA),
						Component.text(" a quitté l'aventure")).build());
	}
}
