package com.gmail.perhapsitisyeazz.rooomain.commands;

import com.gmail.perhapsitisyeazz.rooomain.manager.TeamChatManager;
import com.gmail.perhapsitisyeazz.rooomain.manager.TeamManager;
import com.gmail.perhapsitisyeazz.rooomain.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class TeamChatCommand implements CommandExecutor {

	private final TeamManager teamManager = new TeamManager();
	private final TeamChatManager teamChatManager = new TeamChatManager();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Team team = teamManager.teamOfPlayer(player);
			if (team == null) {
				player.sendMessage(Utils.errorMessage("Vous n'êtes pas dans une team."));
				return true;
			}
			boolean inTeamChat = teamChatManager.isInTeamChat(player);
			TextComponent component = Component.text().color(NamedTextColor.GRAY)
					.append(Component.text("» ", NamedTextColor.DARK_GRAY),
							Component.text("Tchat de team "),
							Component.text(inTeamChat ? "désactivé" : "activé", inTeamChat ? NamedTextColor.RED : NamedTextColor.GREEN),
							Component.text(".")).build();
			teamChatManager.setTeamChat(player, !inTeamChat);
			player.sendMessage(component);
		} else
			sender.sendMessage(Utils.errorMessage("Cette commande est spécialement pour les joueurs."));
		return true;
	}
}
