package com.gmail.perhapsitisyeazz.rooomain.listeners;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import com.gmail.perhapsitisyeazz.rooomain.manager.DeathCountManager;
import com.gmail.perhapsitisyeazz.rooomain.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.time.Duration;

public class DeathEvent implements Listener {

	public Rooomain main;
	public DeathEvent(Rooomain main) {
		this.main = main;
	}

	private final DeathCountManager deathCountManager = new DeathCountManager();

	@EventHandler
	private void playerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		deathCountManager.addDeathToCounter(player);
	}

	@EventHandler
	private void playerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		int deathCount = deathCountManager.getDeathCount(player);
		if (deathCount > DeathCountManager.maxDeath-1) {
			Title title = Title.title(Component.text("Fin de l'aventure pour vous.", NamedTextColor.GRAY),
					Component.text("☠ Vous ne pouvez plus respawn ☠", NamedTextColor.GRAY),
					Title.Times.of(Duration.ofMillis(20L), Duration.ofSeconds(1L), Duration.ofMillis(20L)));
			player.showTitle(title);
			player.setGameMode(GameMode.SPECTATOR);
			TextComponent component = Component.text().color(NamedTextColor.GRAY)
					.append(Component.text("» ", NamedTextColor.DARK_GRAY),
							Component.text("Fin de l'aventure pour "),
							Component.text(player.getName(), NamedTextColor.AQUA),
							Component.text(".")).build();
			Utils.sendToAllPlayers(component);
		} else {
			TextComponent component = Component.text().color(NamedTextColor.GRAY)
					.append(Component.text("» ", NamedTextColor.DARK_GRAY),
							Component.text("Il vous reste "),
							Component.text(DeathCountManager.maxDeath - deathCount, NamedTextColor.AQUA),
							Component.text(" vie."), Component.newline(),
							Component.text("» ", NamedTextColor.DARK_GRAY),
							Component.text("Faites plus attention, c'est votre dernière vie.")).build();
			player.sendMessage(component);
		}
	}
}
