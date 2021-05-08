package com.gmail.perhapsitisyeazz.rooomain.listeners;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import com.gmail.perhapsitisyeazz.rooomain.manager.TeamChatManager;
import com.gmail.perhapsitisyeazz.rooomain.manager.TeamManager;
import com.gmail.perhapsitisyeazz.rooomain.utils.Utils;
import io.papermc.paper.chat.ChatComposer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class ChatEvent implements Listener, ChatComposer {

	public Rooomain main;
	public ChatEvent(Rooomain main) {
		this.main = main;
	}

	private final TeamManager teamManager = new TeamManager();
	private final TeamChatManager teamChatManager = new TeamChatManager();

	@EventHandler
	private void playerChatEvent(AsyncChatEvent event) {
		Player player = event.getPlayer();
		Team team = teamManager.teamOfPlayer(player);
		event.composer(new ChatEvent(Rooomain.getInstance()));
		if (team != null && teamChatManager.isInTeamChat(player)) {
			event.recipients().clear();
			for (Player target : teamManager.onlinePlayersInTeam(team)) {
				event.recipients().add(target);
			}
		}
	}

	@EventHandler
	private void playCommandEvent(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().replaceFirst("/", "").split(" ")[0].equalsIgnoreCase("teammsg"))
			event.setCancelled(true);
	}

	@Override
	public @NotNull Component composeChat(@NotNull Player player, @NotNull Component component, @NotNull Component component1) {
		String stringComponent = PlainComponentSerializer.plain().serialize(component1);
		player.sendMessage(stringComponent);
		if (stringComponent.contains("[I]")) {
			ItemStack item = player.getInventory().getItemInMainHand();
			ItemMeta meta = item.getItemMeta();
			Component itemName;
			TextReplacementConfig textReplacement;
			if (item.getType().isAir()) {
				itemName = Component.text("Main de " + player.getName());
				textReplacement = TextReplacementConfig.builder().match("[I]")
						.replacement(itemName.color(NamedTextColor.AQUA)).build();
			} else {
				Component displayName = meta.displayName();
				if (meta.hasDisplayName() && displayName != null)
					itemName = displayName;
				else
					itemName = item.getI18NDisplayName() != null ? Component.text(item.getI18NDisplayName()) : Component.text("NONE");
				HoverEvent<HoverEvent.ShowItem> itemComponent = item.asHoverEvent();
				textReplacement = TextReplacementConfig.builder().match("[I]")
						.replacement(itemName.color(NamedTextColor.AQUA).hoverEvent(itemComponent)).build();
			}
			component1 = component1.replaceText(textReplacement);
		}
		if (stringComponent.contains("[C]")) {
			TextReplacementConfig textReplacement = TextReplacementConfig.builder().matchLiteral("[C]")
					.replacement(Utils.locationToString(player)).build();
			component1 = component1.replaceText(textReplacement);
		}
		if (teamChatManager.isInTeamChat(player))
			return Component.text()
					.append(Component.text().color(NamedTextColor.WHITE).decorate(TextDecoration.BOLD)
							.append(Component.text("[", NamedTextColor.DARK_GREEN),
									Component.text("Team"),
									Component.text("] ", NamedTextColor.DARK_GREEN)),
							Component.text(player.getName() + ": "),
							component1).build();
		else
			return Component.text().color(NamedTextColor.GRAY)
					.append(Component.text(player.getName()),
							Component.text(": "),
							component1).build();
	}
}
