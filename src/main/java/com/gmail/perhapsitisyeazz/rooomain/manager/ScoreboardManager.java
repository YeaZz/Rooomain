package com.gmail.perhapsitisyeazz.rooomain.manager;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import com.gmail.perhapsitisyeazz.rooomain.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

	private final static Team[] lines = new Team[15];
	private final static String[] entries = new String[]{"&1&r", "&2&r", "&3&r", "&4&r", "&5&r", "&6&r", "&7&r", "&8&r", "&9&r", "&0&r", "&a&r", "&b&r", "&c&r", "&d&r", "&e&r"};

	public void setScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective board = scoreboard.registerNewObjective("DidierScoreboard", "dummy", Component.text("Survie"));
		board.setDisplaySlot(DisplaySlot.SIDEBAR);

		for (int i = 0; i < 15; i++) {
			lines[i] = scoreboard.registerNewTeam("line" + (i + 1));
			lines[i].addEntry(Utils.getColString(entries[i]));
		}

		World world = player.getWorld();
		setLine(board, 15, Component.text("» Online", NamedTextColor.GRAY));
		setLine(board, 14, Component.text(Bukkit.getOnlinePlayers().size() + " / " + Bukkit.getMaxPlayers()));
		setLine(board, 13, Component.space());
		setLine(board, 12, Component.text("» ", NamedTextColor.DARK_GRAY).append(Component.text("Temps", NamedTextColor.GRAY)));
		setLine(board, 11, TimeManager.formattedTimeFromTick(world.getTime()));
		setLine(board, 4, Component.space());
		setLine(board, 3, Component.text(player.getName()));
		setLine(board, 2, Component.space());
		setLine(board, 1, Component.text("network-romain.ddns.net", NamedTextColor.GOLD));
		player.setScoreboard(scoreboard);
	}

	public static void setLine(Objective board, int line, Component content) {
		lines[line - 1].prefix(content);
		board.getScore(Utils.getColString(entries[line - 1])).setScore(line);
	}

	public void updateScoreboard() {
		final Rooomain instance = Rooomain.getInstance();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
			if (Bukkit.getOnlinePlayers().size() == 0)
				return;
			for (Player player : Bukkit.getOnlinePlayers()) {
				Objective board = player.getScoreboard().getObjective("DidierScoreboard");
				if (board == null)
					return;
				World world = player.getWorld();
				setLine(board, 11, TimeManager.formattedTimeFromTick(world.getTime()));
			}
		}, 0L, 15L);
	}
}
