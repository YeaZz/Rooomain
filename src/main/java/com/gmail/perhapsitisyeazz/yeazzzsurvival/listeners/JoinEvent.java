package com.gmail.perhapsitisyeazz.yeazzzsurvival.listeners;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.YeazzzSurvival;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public record JoinEvent(YeazzzSurvival main) implements Listener {

	@EventHandler
	private void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (PlayerDataManager.noPlayerData(player)) {
			PlayerDataManager.createPlayerData(player);
			player.displayName(Component.text(player.getName(), NamedTextColor.GRAY));
		}
		BoardManager.createBoard(player);
		BoardManager.loadScoreboard(player);
		PlayerListManager.reload();
		DirectionManager.teamDirection(player);
		event.joinMessage(Component.text().color(NamedTextColor.DARK_GREEN).append(
				Component.text("[+] "),
				Component.text(player.getName())
		).build());
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				BoardManager.updateScoreboardOnlineCounter();
			}
		};
		runnable.runTaskLater(YeazzzSurvival.getInstance(), 1L);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(YeazzzSurvival.getInstance(), () -> {
			if (!player.isOnline())
				return;
			BoardManager.updateScoreboardPlayerTime(player);
		}, 1L, 5L);
	}

	@EventHandler
	private void playerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		BoardManager.removeBoard(player);
		event.quitMessage(Component.text().color(NamedTextColor.RED).append(
				Component.text("[-] "),
				Component.text(player.getName())
		).build());
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				BoardManager.updateScoreboardOnlineCounter();
			}
		};
		runnable.runTaskLater(YeazzzSurvival.getInstance(), 1L);
	}
}
