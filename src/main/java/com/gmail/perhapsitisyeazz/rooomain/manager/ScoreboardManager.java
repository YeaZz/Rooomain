package com.gmail.perhapsitisyeazz.rooomain.manager;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import com.gmail.perhapsitisyeazz.rooomain.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

	private final static TeamManager teamManager = new TeamManager();

	private final static Team[] lines = new Team[15];
	private final static String[] entries = new String[] {"&1&r", "&2&r", "&3&r", "&4&r", "&5&r", "&6&r", "&7&r", "&8&r", "&9&r", "&0&r", "&a&r", "&b&r", "&c&r", "&d&r", "&e&r"};

	public static void setScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective board = scoreboard.registerNewObjective("DidierScoreboard", "dummy", Component.text("Survie", NamedTextColor.AQUA));
		board.setDisplaySlot(DisplaySlot.SIDEBAR);

		for (int i = 0; i < 15; i++) {
			lines[i] = scoreboard.registerNewTeam("line" + (i + 1));
			lines[i].addEntry(Utils.getColString(entries[i]));
		}

		World world = player.getWorld();
		setLine(board, 1, Component.space());
		setLine(board, 10, Component.text("» ", NamedTextColor.DARK_GRAY)
				.append(Component.text("Team", NamedTextColor.GRAY)));
		setLine(board, 9, playerTeamLine(player));
		setLine(board, 8, Component.space());
		setLine(board, 7, Component.text("» ", NamedTextColor.DARK_GRAY)
				.append(Component.text("En ligne", NamedTextColor.GRAY)));
		setLine(board, 6, onlineCounterLine());
		setLine(board, 5, Component.space());
		setLine(board, 4, Component.text("» ", NamedTextColor.DARK_GRAY)
				.append(Component.text("Temps", NamedTextColor.GRAY)));
		setLine(board, 3, inGameTimeLine(world));
		setLine(board, 2, Component.space());
		setLine(board, 1, Component.text("network-romain.craft.gg", NamedTextColor.GOLD));
		player.setScoreboard(scoreboard);
	}

	private static Component playerTeamLine(Player player) {
		TextComponent.Builder builder = Component.text().content(" - ").color(NamedTextColor.DARK_AQUA);
		Team team = teamManager.teamOfPlayer(player);
		return builder.append(Component.text((team == null ? "Aucune" : team.getName()), (team == null ? NamedTextColor.AQUA : team.color()))).build();
	}

	private static Component onlineCounterLine() {
		int actual = Bukkit.getOnlinePlayers().size();
		int max = Bukkit.getMaxPlayers();
		return Component.text().content(" - ").color(NamedTextColor.DARK_AQUA)
				.append(Component.text(actual, (actual == max ? NamedTextColor.GREEN : NamedTextColor.AQUA)),
						Component.text("/", NamedTextColor.GRAY),
						Component.text(max, NamedTextColor.GREEN)).build();
	}

	private static Component inGameTimeLine(World world) {
		return Component.text().color(NamedTextColor.GRAY)
				.append(Component.text(" - ", NamedTextColor.DARK_AQUA),
						Component.text(TimeManager.formattedTimeFromTick(world.getTime()), NamedTextColor.AQUA),
						Component.space(),
						TimeManager.getMCDayPart(world.getTime()),
						Component.space(),
						TimeManager.getMCWeather(world)).build();
	}

	public static void setLine(Objective board, int line, Component content) {
		lines[line - 1].prefix(content);
		board.getScore(Utils.getColString(entries[line - 1])).setScore(line);
	}

	public static void updateScoreboardPlayerTeam(Player player) {
		Objective board = player.getScoreboard().getObjective("DidierScoreboard");
		if (board == null)
			return;
		setLine(board, 9, playerTeamLine(player));
	}

	public static void updateScoreboardOnlineCounter() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Objective board = player.getScoreboard().getObjective("DidierScoreboard");
			if (board == null)
				return;
			setLine(board, 6, onlineCounterLine());
		}
	}

	public static void updateScoreboardTime() {
		final Rooomain instance = Rooomain.getInstance();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
			if (Bukkit.getOnlinePlayers().size() == 0)
				return;
			for (Player player : Bukkit.getOnlinePlayers()) {
				Objective board = player.getScoreboard().getObjective("DidierScoreboard");
				if (board == null)
					return;
				World world = player.getWorld();
				setLine(board, 3, inGameTimeLine(world));
			}
		}, 0L, 15L);
	}
}
