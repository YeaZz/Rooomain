package com.gmail.perhapsitisyeazz.rooomain.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Utils {

	public static void sendToAllPlayers(Component component) {
		Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(component));
	}

	public static Component errorMessage(String msg) {
		return Component.text()
				.append(Component.text("» ", NamedTextColor.DARK_GRAY),
						Component.text("[Erreur] ", NamedTextColor.RED),
						Component.text(msg, NamedTextColor.GRAY)).build();
	}

	public static Component locationToString(Player player) {
		Location loc = player.getLocation();
		return Component.text().color(NamedTextColor.AQUA)
				.append(Component.text("[", NamedTextColor.DARK_AQUA),
				Component.text((int)loc.getX()),
				Component.text(", ", NamedTextColor.DARK_AQUA),
				Component.text((int)loc.getY()),
				Component.text(", ", NamedTextColor.DARK_AQUA),
				Component.text((int)loc.getZ()),
				Component.text("]", NamedTextColor.DARK_AQUA)).build();
	}

	public static Component helpComponent(String command, String[] subCommands, String[] subCommandsUsage, String[] subCommandDescription, ClickEvent.Action[] subCommandsClickActionType) {
		TextComponent.Builder component = Component.text()
				.append(Component.text("» ", NamedTextColor.DARK_GRAY),
				Component.text("Usage correct :", NamedTextColor.GRAY));
		for (int i = 0; i < subCommands.length; i++) {
			String usage = "/" + command + " " + subCommands[i] + " " + subCommandsUsage[i], description = subCommandDescription[i],
					camelCaseCommand = usage.split(" ")[0].substring(1, 2).toUpperCase() + usage.split(" ")[0].substring(2);
			Component hoverComponent = Component.text().color(NamedTextColor.GRAY)
					.append(Component.text("Commande: "),
					Component.text(camelCaseCommand, NamedTextColor.AQUA),
					Component.newline(),
					Component.text("Description: "),
					Component.text(description, NamedTextColor.AQUA),
					Component.newline(),
					Component.text("Usage: "),
					Component.text(usage, NamedTextColor.AQUA),
					Component.newline(), Component.newline(),
					Component.text("Cliques pour éxécuter", NamedTextColor.DARK_GRAY)).build();
			component.append(Component.newline(),
					Component.text(" - ", NamedTextColor.DARK_AQUA),
					Component.text(usage, NamedTextColor.AQUA).hoverEvent(hoverComponent)
							.clickEvent(ClickEvent.clickEvent(subCommandsClickActionType[i], "/" + command + " " + subCommands[i])));
		}
		return component.build();
	}
}
