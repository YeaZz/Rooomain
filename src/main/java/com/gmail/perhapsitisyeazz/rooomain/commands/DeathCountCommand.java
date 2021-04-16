package com.gmail.perhapsitisyeazz.rooomain.commands;

import com.gmail.perhapsitisyeazz.rooomain.manager.DeathCountManager;
import com.gmail.perhapsitisyeazz.rooomain.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeathCountCommand implements CommandExecutor, TabCompleter {

	private final DeathCountManager deathCountManager = new DeathCountManager();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.isOp()) {
			sender.sendMessage(Component.text("Vous n'avez pas la permission.", NamedTextColor.RED));
			return true;
		}
		if (args.length > 1) {
			List<Player> targets = new ArrayList<>();
			if (args[1].equalsIgnoreCase("all")) {
				targets.addAll(Bukkit.getOnlinePlayers());
			} else {
				String stringPlayer = args[1];
				Player player = Bukkit.getPlayer(stringPlayer);
				if (player == null) {
					sender.sendMessage(Utils.errorMessage("Joueur déconnecté ou inéxistant."));
					return true;
				}
				targets.add(player);
			}
			if (args[0].equalsIgnoreCase("reset")) {
				for (Player player : targets) {
					deathCountManager.initialize(player);
					player.teleport(player.getWorld().getSpawnLocation());
					player.setGameMode(GameMode.SURVIVAL);
					TextComponent component = Component.text().color(NamedTextColor.GRAY)
							.append(Component.text("» ", NamedTextColor.DARK_GRAY),
									Component.text("Bon retour parmis nous ! Tâchez de rester en vie.", NamedTextColor.GRAY)).build();
					player.sendMessage(component);
				}
			} else if (args[0].equalsIgnoreCase("show")) {
				for (Player player : targets) {
					TextComponent component = Component.text().color(NamedTextColor.GRAY)
							.append(Component.text("» ", NamedTextColor.DARK_GRAY),
									Component.text(player.getName(), NamedTextColor.AQUA),
									Component.text(" est à "),
									Component.text(deathCountManager.getDeathCount(player), NamedTextColor.AQUA),
									Component.text(" mort(s).")).build();
					sender.sendMessage(component);
				}
			}
		} else
			sender.sendMessage(Utils.helpComponent(command.getName(), subCommands, subCommandsUsage, subCommandDescription, subCommandsClickActionType));
		return true;
	}

	private final String[] subCommands = {"reset", "show"},
			subCommandsUsage = {"(all|<joueur>)", "(all|<joueur>)"},
			subCommandDescription = {"Réinitialise les morts d'un joueur.", "Montre le nombre de mort d'un joueur."};

	private final ClickEvent.Action[] subCommandsClickActionType = {ClickEvent.Action.SUGGEST_COMMAND, ClickEvent.Action.SUGGEST_COMMAND};

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		final List<String> completions = new ArrayList<>();
		final List<String> argCommands;
		if (sender.isOp()) {
			if (args.length == 1) {
				argCommands = Arrays.asList(subCommands);
				StringUtil.copyPartialMatches(args[0], argCommands, completions);
			} else if (args.length == 2) {
				argCommands = new ArrayList<>();
				argCommands.add("all");
				Bukkit.getOnlinePlayers().forEach(player -> argCommands.add(player.getName()));
				StringUtil.copyPartialMatches(args[1], argCommands, completions);
			}
			Collections.sort(completions);
		}
		return completions;
	}
}
