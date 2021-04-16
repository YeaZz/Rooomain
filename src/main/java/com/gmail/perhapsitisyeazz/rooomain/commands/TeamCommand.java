package com.gmail.perhapsitisyeazz.rooomain.commands;

import com.gmail.perhapsitisyeazz.rooomain.manager.TeamManager;
import com.gmail.perhapsitisyeazz.rooomain.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TeamCommand implements CommandExecutor, TabCompleter {

	private final TeamManager teamManager = new TeamManager();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
			if (args.length > 0 && args[0].equals("all")) {
				if (teamManager.allTeams().isEmpty()) {
					player.sendMessage(Utils.errorMessage("Aucune team a été créé."));
					return true;
				}
				TextComponent.Builder component = Component.text().color(NamedTextColor.GRAY)
						.append(Component.text("» ", NamedTextColor.DARK_GRAY),
								Component.text("Voici la liste de toutes les teams :"));
				for (Team team : teamManager.allTeams()) {
					List<OfflinePlayer> playersInTeam = teamManager.playersFromTeam(team);
					TextComponent.Builder hoverComponent = Component.text().color(NamedTextColor.GRAY)
							.append(Component.text("» ", NamedTextColor.DARK_GRAY),
									Component.text("Joueurs :"));
					for (OfflinePlayer target : playersInTeam) {
						assert target.getName() != null;
						hoverComponent.append(Component.newline(),
								Component.text(" - ", NamedTextColor.DARK_AQUA),
								Component.text(target.getName(), NamedTextColor.AQUA));
						if (target.isOnline())
							hoverComponent.append(Component.text(" (Connecté)", NamedTextColor.RED));
					}
					component.append(Component.newline(),
							Component.text(" - ", NamedTextColor.DARK_AQUA),
							Component.text(team.getName(), team.color())
									.hoverEvent(HoverEvent.showText(hoverComponent)));
				}
				player.sendMessage(component.build());
			} else if (args.length > 2 && args[0].equals("create")) {
				Team playerTeam = teamManager.teamOfPlayer(player);
				if (playerTeam != null) {
					player.sendMessage(Utils.errorMessage("Vous êtes déjà dans une team."));
					return true;
				}
				if (teamManager.teamExists(args[1])) {
					player.sendMessage(Utils.errorMessage("Ce nom est déjà attribué."));
					return true;
				}
				NamedTextColor namedTextColor = NamedTextColor.NAMES.value(args[2]);
				if (namedTextColor == null) {
					player.sendMessage(Utils.errorMessage("Choisissez une bonne couleur."));
					return true;
				}
				teamManager.createTeam(player, args[1], namedTextColor);
			} else if (args.length > 1 && args[0].equals("join")) {
				if (!teamManager.teamExists(args[1])) {
					player.sendMessage(Utils.errorMessage("Cette team n'éxiste pas."));
					return true;
				}
				Team playerTeam = teamManager.teamOfPlayer(player);
				if (playerTeam != null) {
					teamManager.leaveTeam(player, playerTeam);
				}
				Team newTeam = scoreboard.getTeam(args[1]);
				assert newTeam != null;
				if (playerTeam != newTeam)
					teamManager.joinTeam(player, newTeam);
				else
					player.sendMessage(Utils.errorMessage("Vous êtes déjà dans cette team."));
			} else if (args.length > 0 && args[0].equals("leave")) {
				Team team = teamManager.teamOfPlayer(player);
				if (team == null) {
					player.sendMessage(Utils.errorMessage("Vous n'êtes pas dans une team."));
					return true;
				}
				teamManager.leaveTeam(player, team);
			} else if (args.length > 0 && args[0].equals("info")) {
				Team team = teamManager.teamOfPlayer(player);
				if (team == null) {
					player.sendMessage(Utils.errorMessage("Vous n'êtes pas dans une team."));
					return true;
				}
				TextComponent.Builder component = Component.text().color(NamedTextColor.GRAY)
						.append(Component.text("» ", NamedTextColor.DARK_GRAY),
								Component.text("Information de la team "),
								Component.text(team.getName(), team.color()),
								Component.text(" :"));
				for (Player target : teamManager.onlinePlayersInTeam(team)) {
					component.append(Component.newline(),
							Component.text(" - ", NamedTextColor.DARK_AQUA),
							Component.text(target.getName(), NamedTextColor.AQUA),
							Component.space(), Utils.locationToString(target));
				}
				for (OfflinePlayer target : teamManager.offlinePlayersInTeam(team)) {
					assert target.getName() != null;
					component.append(Component.newline(),
							Component.text(" - ", NamedTextColor.DARK_AQUA),
							Component.text(target.getName(), NamedTextColor.AQUA),
							Component.text(" (Déconnecté)", NamedTextColor.RED));
				}
				player.sendMessage(component.build());
			} else
				player.sendMessage(Utils.helpComponent(command.getName(), subCommands, subCommandsUsage, subCommandsDescription, subCommandsClickActionType));
		} else
			sender.sendMessage(Utils.errorMessage("Cette commande est spécialement pour les joueurs."));
		return true;
	}

	private final String[] subCommands = {"all", "create", "join", "leave", "info"},
			subCommandsUsage = {"", "<texte> <couleur>", "<team>", "<team>", ""},
			subCommandsDescription = {"Voir toutes les teams", "Créé ta team", "Rejoins une team", "Quitte ta team", "Voir les membres de ta team"};

	private final ClickEvent.Action[] subCommandsClickActionType = {ClickEvent.Action.RUN_COMMAND, ClickEvent.Action.SUGGEST_COMMAND, ClickEvent.Action.SUGGEST_COMMAND,
			ClickEvent.Action.SUGGEST_COMMAND, ClickEvent.Action.RUN_COMMAND};

	private final List<String> colors = Arrays.asList("aqua", "black", "blue", "dark_aqua", "dark_blue", "dark_gray", "dark_green", "dark_purple",
			"dark_red", "gold", "gray", "green", "light_purple", "red", "white", "yellow");

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		final List<String> completions = new ArrayList<>();
		final List<String> argCommands;
		if (args.length == 1) {
			argCommands = Arrays.asList(subCommands);
			StringUtil.copyPartialMatches(args[0], argCommands, completions);
		} else if (args.length == 2 && args[0].equals("join")) {
			argCommands = new ArrayList<>();
			for (Team team : teamManager.allTeams())
				argCommands.add(team.getName());
			StringUtil.copyPartialMatches(args[1], argCommands, completions);
		} else if (args[0].equals("create")) {
			if (args.length == 2)
				completions.add("Nom de votre Team :");
			else if (args.length == 3)
				StringUtil.copyPartialMatches(args[2], colors, completions);
		}
		Collections.sort(completions);
		return completions;
	}
}
