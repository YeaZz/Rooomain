package com.gmail.perhapsitisyeazz.yeazzzsurvival.listeners;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.YeazzzSurvival;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerTeamManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeam;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public record ChatEvent(YeazzzSurvival main) implements Listener, ChatRenderer {

	@EventHandler
	private void playerChatEvent(AsyncChatEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = PlayerDataManager.getPlayerData(player);
		PlayerTeam team = playerData.getTeam();
		event.renderer(new ChatEvent(YeazzzSurvival.getInstance()));
		if (team != null && playerData.isTeamChat()) {
			event.viewers().clear();
			for (OfflinePlayer target : PlayerTeamManager.getPlayersFromPlayerTeam(team, true)) {
				Player onlinePlayer = target.getPlayer();
				assert onlinePlayer != null;
				event.viewers().add(onlinePlayer);
			}
		}
		event.viewers().add(player);
	}

	@EventHandler
	private void playerChat(AsyncPlayerChatEvent event) {
		if (event.getPlayer().isOp())
			event.setMessage(Utils.getColString(event.getMessage()));
	}

	@EventHandler
	private void playCommandEvent(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().split(" ")[0].equalsIgnoreCase("/teammsg"))
			event.setCancelled(true);
	}

	@Override
	public @NotNull
	Component render(@NotNull Player player, @NotNull Component component, @NotNull Component component1, @NotNull Audience audience) {
		PlayerData playerData = PlayerDataManager.getPlayerData(player);
		component1 = Utils.tchatUtil(component1, player);
		if (playerData.isTeamChat())
			return Component.text().append(
					Component.text().color(NamedTextColor.WHITE).decorate(TextDecoration.BOLD),
					Component.text("[", NamedTextColor.DARK_GREEN),
					Component.text("Équipe"),
					Component.text("] ", NamedTextColor.DARK_GREEN),
					Component.text(player.getName() + ": "),
					component1
			).build();
		else {
			PlayerTeam team = playerData.getTeam();
			if (team == null)
				return Component.text().color(NamedTextColor.GRAY).append(
						Component.text(player.getName()),
						Component.text(": "),
						component1
				).build();
			else
				return Component.text().color(NamedTextColor.GRAY).append(
						Component.text("["),
						Component.text(team.getName(), team.getColor()),
						Component.text("] "),
						Component.text(player.getName()),
						Component.text(": "),
						component1
				).build();
		}
	}
}
