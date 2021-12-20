package com.gmail.perhapsitisyeazz.yeazzzsurvival.commands;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerListManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerTeamManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeam;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
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

public class PlayerTeamCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player player) {
			PlayerData playerData = PlayerDataManager.getPlayerData(player);
			PlayerTeam team = playerData.getTeam();
			if (args.length > 0 && args[0].equals("all")) {
				if (PlayerTeamManager.allTeams().isEmpty()) {
					player.sendMessage(Utils.errorMessage("Aucune team n'a été créé."));
					return true;
				}
				TextComponent.Builder component = Component.text().color(NamedTextColor.GRAY).append(
						Component.text("» ", Utils.HYPHEN_COLOR),
						Component.text("Voici la liste de toutes les teams :")
				);
				for (PlayerTeam teams : PlayerTeamManager.allTeams()) {
					assert teams.getOwner().getName() != null;
					TextComponent.Builder hoverComponent = Component.text().color(NamedTextColor.GRAY).append(
							Component.text("» ", Utils.HYPHEN_COLOR),
							Component.text("Joueurs :"),
							Component.newline(),
							Component.text(" - ", Utils.HYPHEN_COLOR),
							Component.text(teams.getOwner().getName(), NamedTextColor.DARK_GREEN)
					);
					final List<OfflinePlayer> players = new ArrayList<>(teams.getMembers());
					players.add(teams.getOwner());
					for (OfflinePlayer member : players) {
						assert member.getName() != null;
						hoverComponent.append(
								Component.newline(),
								Component.text(" - ", Utils.HYPHEN_COLOR),
								Component.text(member.getName(), NamedTextColor.DARK_GREEN)
						);
						if (member.isOnline())
							hoverComponent.append(Component.text(" (Connecté)", NamedTextColor.DARK_GREEN));
					}
					component.append(
							Component.newline(),
							Component.text(" - ", Utils.HYPHEN_COLOR),
							Component.text(teams.getName(), teams.getColor())
									.hoverEvent(HoverEvent.showText(hoverComponent))
					);
				}
				player.sendMessage(component.build());
			} else if (args.length > 2 && args[0].equals("create")) {
				if (team != null) {
					player.sendMessage(Utils.errorMessage("Vous êtes déjà dans une équipe."));
					return true;
				}
				if (PlayerTeamManager.playerTeamExists(args[1])) {
					player.sendMessage(Utils.errorMessage("Ce nom est déjà attribué."));
					return true;
				}
				if (!args[1].matches("[a-zA-Z0-9]+")) {
					player.sendMessage(Utils.errorMessage("Veuillez ne pas mettre de caractères spéciaux."));
					return true;
				}
				NamedTextColor color = NamedTextColor.NAMES.value(args[2]);
				if (color == null) {
					player.sendMessage(Utils.errorMessage("Choisissez une bonne couleur."));
					return true;
				}
				PlayerTeamManager.createPlayerTeam(args[1], color, player);
				PlayerListManager.reload();
			} else if (args.length > 1 && args[0].equals("join")) {
				if (!PlayerTeamManager.playerTeamExists(args[1])) {
					player.sendMessage(Utils.errorMessage("Cette équipe n'éxiste pas."));
					return true;
				}
				if (team != null) {
					player.sendMessage(Utils.errorMessage("Vous êtes déjà dans une équipe."));
					return true;
				}
				PlayerTeam playerTeam = PlayerTeamManager.getPlayerTeam(args[1]);
				PlayerTeamManager.joinTeam(player, playerTeam);
				PlayerListManager.reload();
			} else if (args.length > 0 && args[0].equals("leave")) {
				if (team == null) {
					player.sendMessage(Utils.errorMessage("Vous n'êtes pas dans une équipe."));
					return true;
				}
				if (team.getOwner().getUniqueId().equals(player.getUniqueId())) {
					PlayerTeamManager.deletePlayerTeam(team);
					PlayerListManager.reload();
					return true;
				}
				PlayerTeamManager.leaveTeam(player, team);
				PlayerListManager.reload();
			} else if (args.length > 0 && args[0].equals("info")) {
				if (team == null) {
					player.sendMessage(Utils.errorMessage("Vous n'êtes pas dans une équipe."));
					return true;
				}
				TextComponent.Builder component = Component.text().color(NamedTextColor.GRAY).append(
						Component.text("» ", Utils.HYPHEN_COLOR),
						Component.text("Information de la team "),
						Component.text(team.getName(), team.getColor()),
						Component.text(" :")
				);
				for (OfflinePlayer players : PlayerTeamManager.getPlayersFromPlayerTeam(team, true)) {
					Player onlinePlayer = players.getPlayer();
					assert onlinePlayer != null;
					component.append(
							Component.newline(),
							Component.text(" - ", Utils.HYPHEN_COLOR),
							Component.text(onlinePlayer.getName(), NamedTextColor.DARK_GREEN),
							Component.space(), Utils.locationToString(onlinePlayer)
					);
				}
				for (OfflinePlayer target : PlayerTeamManager.getPlayersFromPlayerTeam(team, false)) {
					assert target.getName() != null;
					component.append(
							Component.newline(),
							Component.text(" - ", Utils.HYPHEN_COLOR),
							Component.text(target.getName(), NamedTextColor.DARK_GREEN),
							Component.text(" (Déconnecté)", NamedTextColor.DARK_GREEN)
					);
				}
				player.sendMessage(component.build());
			} else
				player.sendMessage(Utils.helpComponent(command.getName(), subCommands, subCommandsUsage, subCommandsDescription, subCommandsClickActionType));
		} else
			sender.sendMessage(Utils.errorMessage("Cette commande est réservée pour les joueurs."));
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
			for (PlayerTeam team : PlayerTeamManager.allTeams())
				argCommands.add(team.getName());
			StringUtil.copyPartialMatches(args[1], argCommands, completions);
		} else if (args[0].equals("create")) {
			if (args.length == 2)
				completions.add("Nom de votre future équipe :");
			else if (args.length == 3)
				StringUtil.copyPartialMatches(args[2], colors, completions);
		}
		Collections.sort(completions);
		return completions;
	}
}
