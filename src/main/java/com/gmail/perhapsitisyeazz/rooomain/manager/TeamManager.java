package com.gmail.perhapsitisyeazz.rooomain.manager;

import com.gmail.perhapsitisyeazz.rooomain.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TeamManager {

	private final TeamChatManager teamChatManager = new TeamChatManager();

	private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

	public void createTeam(Player player, String teamName, NamedTextColor teamColor) {
		Team team = scoreboard.registerNewTeam(teamName);
		team.addEntry(player.getName());
		team.color(teamColor);
		TextComponent component = Component.text().color(NamedTextColor.GRAY)
				.append(Component.text("» ", NamedTextColor.DARK_GRAY),
				Component.text(player.getName(), NamedTextColor.AQUA),
				Component.text(" a créé la team "),
				Component.text(team.getName(), teamColor),
				Component.text(".")).build();
		Utils.sendToAllPlayers(component);
	}

	public void joinTeam(Player player, Team team) {
		team.addEntry(player.getName());
		TextComponent component = Component.text().color(NamedTextColor.GRAY)
				.append(Component.text("» ", NamedTextColor.DARK_GRAY),
				Component.text(player.getName(), NamedTextColor.AQUA),
				Component.text(" a rejoint la team "),
				Component.text(team.getName(), team.color()),
				Component.text(".")).build();
		Utils.sendToAllPlayers(component);
	}

	public void leaveTeam(Player player, Team team) {
		team.removeEntry(player.getName());
		teamChatManager.setTeamChat(player, false);
		TextComponent.Builder component = Component.text().color(NamedTextColor.GRAY)
				.append(Component.text("» ", NamedTextColor.DARK_GRAY),
				Component.text(player.getName(), NamedTextColor.AQUA),
				Component.text(" a quitté la team "),
				Component.text(team.getName(), team.color()),
				Component.text("."));
		if (playersFromTeam(team).isEmpty()) {
			component.append(Component.newline(),
					Component.text("» ", NamedTextColor.DARK_GRAY),
					Component.text("Étant vide, "),
					Component.text(team.getName(), team.color()),
					Component.text(" a été supprimé."));
			team.unregister();
		}
		Utils.sendToAllPlayers(component.build());
	}

	public boolean teamExists(String string) {
		Team team = scoreboard.getTeam(string);
		return team != null;
	}

	@Nullable
	public Team teamOfPlayer(Player player) {
		for (Team team : scoreboard.getTeams())
			for (String players : team.getEntries())
				if (players.equals(player.getName()))
					return team;
		return null;
	}

	public List<Team> allTeams() {
		return new ArrayList<>(scoreboard.getTeams());
	}

	public List<OfflinePlayer> playersFromTeam(Team team) {
		List<OfflinePlayer> players = new ArrayList<>();
		if (team == null)
			return players;
		for (String player : team.getEntries()) {
			OfflinePlayer p = Bukkit.getOfflinePlayerIfCached(player);
			if (p != null)
				players.add(p);
		}
		return players;
	}

	public List<Player> onlinePlayersInTeam(Team team) {
		List<Player> players = new ArrayList<>();
		for (OfflinePlayer offlinePlayer : playersFromTeam(team)) {
			if (offlinePlayer.isOnline())
				players.add(offlinePlayer.getPlayer());
		}
		return players;
	}

	public List<OfflinePlayer> offlinePlayersInTeam(Team team) {
		List<OfflinePlayer> players = new ArrayList<>();
		for (OfflinePlayer offlinePlayer : playersFromTeam(team)) {
			if (!offlinePlayer.isOnline())
				players.add(offlinePlayer);
		}
		return players;
	}
}
