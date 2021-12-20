package com.gmail.perhapsitisyeazz.yeazzzsurvival.utils;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerListManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {

	public final static NamedTextColor HYPHEN_COLOR = NamedTextColor.RED;
	public final static NamedTextColor TEXT_COLOR = NamedTextColor.WHITE;

	public static String getColString(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static void sendToAllPlayers(Component component) {
		Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(component));
	}

	public static Component errorMessage(String msg) {
		return Component.text().append(
				Component.text("» ", HYPHEN_COLOR),
				Component.text("[Erreur] ", NamedTextColor.DARK_RED),
				Component.text(msg, Utils.TEXT_COLOR)
		).build();
	}

	public static Component locationToString(Player player) {
		Location loc = player.getLocation();
		return Component.text().color(TEXT_COLOR).append(
				Component.text("[", HYPHEN_COLOR),
				Component.text((int)loc.getX()),
				Component.text(", ", HYPHEN_COLOR),
				Component.text((int)loc.getY()),
				Component.text(", ", HYPHEN_COLOR),
				Component.text((int)loc.getZ()),
				Component.text("]", HYPHEN_COLOR)
		).build();
	}

	public static Component helpComponent(String command, String[] subCommands, String[] subCommandsUsage, String[] subCommandDescription, ClickEvent.Action[] subCommandsClickActionType) {
		TextComponent.Builder component = Component.text().append(
				Component.text("» ", HYPHEN_COLOR),
				Component.text("Usage correct :", NamedTextColor.GRAY)
		);
		for (int i = 0; i < subCommands.length; i++) {
			String usage = "/" + command + " " + subCommands[i] + " " + subCommandsUsage[i], description = subCommandDescription[i],
					camelCaseCommand = usage.split(" ")[0].substring(1, 2).toUpperCase() + usage.split(" ")[0].substring(2);
			Component hoverComponent = Component.text().color(NamedTextColor.GRAY).append(
					Component.text("Commande: "),
					Component.text(camelCaseCommand, TEXT_COLOR), Component.newline(),
					Component.text("Description: "),
					Component.text(description, TEXT_COLOR), Component.newline(),
					Component.text("Usage: "),
					Component.text(usage, TEXT_COLOR), Component.newline(), Component.newline(),
					Component.text("Cliques pour éxécuter", NamedTextColor.RED)
			).build();
			component.append(Component.newline(),
					Component.text(" - ", HYPHEN_COLOR),
					Component.text(usage, TEXT_COLOR).hoverEvent(hoverComponent)
							.clickEvent(ClickEvent.clickEvent(subCommandsClickActionType[i], "/" + command + " " + subCommands[i])));
		}
		return component.build();
	}

	public static Component tchatUtil(Component component, Player player) {
		String stringComponent = PlainTextComponentSerializer.plainText().serialize(component);
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
			component = component.replaceText(textReplacement);
		}
		if (stringComponent.contains("[C]")) {
			TextReplacementConfig textReplacement = TextReplacementConfig.builder().matchLiteral("[C]")
					.replacement(Utils.locationToString(player)).build();
			component = component.replaceText(textReplacement);
		}
		return component;
	}
}
