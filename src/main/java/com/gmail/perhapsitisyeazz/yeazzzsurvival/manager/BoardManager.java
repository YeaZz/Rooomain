package com.gmail.perhapsitisyeazz.yeazzzsurvival.manager;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.Board;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeam;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BoardManager {

	private static final Map<Player, Board> BOARD_MAP = new HashMap<>();

	public static Board getBoard(Player player) {
		return BOARD_MAP.get(player);
	}

	public static void createBoard(Player player) {
		Board board = new Board(player);
		BOARD_MAP.put(player, board);
	}

	public static void removeBoard(Player player) {
		if (BOARD_MAP.containsKey(player)) {
			BOARD_MAP.get(player).clearBoard();
		}
		BOARD_MAP.remove(player);
	}

	private static final TimeManager timeManager = new TimeManager();

	public static void loadScoreboard(Player player) {
		Board board = getBoard(player);
		board.setLine(14, Component.space());
		board.setLine(13, Component.text("» ", Utils.HYPHEN_COLOR)
				.append(Component.text("Team", NamedTextColor.GRAY)));
		board.setLine(12, playerTeamLine(player));
		board.setLine(11, Component.space());
		board.setLine(10, Component.text("» ", Utils.HYPHEN_COLOR)
				.append(Component.text("Mode", NamedTextColor.GRAY)));
		board.setLine(9, playerModLine(player));
		board.setLine(8, Component.space());
		board.setLine(7, Component.text("» ", Utils.HYPHEN_COLOR)
				.append(Component.text("Joueurs", NamedTextColor.GRAY)));
		board.setLine(6, onlineCounterLine());
		board.setLine(5, Component.space());
		board.setLine(4, Component.text("» ", Utils.HYPHEN_COLOR)
				.append(Component.text("Temps", NamedTextColor.GRAY)));
		board.setLine(2, Component.space());
		board.setLine(1, Component.text("surviedejoel.ouimc.fr", NamedTextColor.GOLD).decorate(TextDecoration.ITALIC));
	}

	public static Component playerTeamLine(Player player) {
		PlayerData playerData = PlayerDataManager.getPlayerData(player);
		TextComponent.Builder builder = Component.text().content(" - ").color(Utils.HYPHEN_COLOR);
		PlayerTeam team = playerData.getTeam();
		return builder.append(Component.text((team == null ? "Aucune" : team.getName()), (team == null ? Utils.TEXT_COLOR : team.getColor()))).build();
	}

	public static Component playerModLine(Player player) {
		PlayerData playerData = PlayerDataManager.getPlayerData(player);
		TextComponent.Builder builder = Component.text().content(" - ").color(Utils.HYPHEN_COLOR);
		String mode = playerData.getMod().toString().toLowerCase();
		mode = mode.substring(0, 1).toUpperCase() + mode.substring(1);
		return builder.append(Component.text(mode, Utils.TEXT_COLOR)).build();
	}

	public static Component onlineCounterLine() {
		int actual = Bukkit.getOnlinePlayers().size();
		int max = Bukkit.getMaxPlayers();
		return Component.text().content(" - ").color(Utils.HYPHEN_COLOR).append(
				Component.text(actual, (actual == max ? NamedTextColor.GREEN : Utils.TEXT_COLOR)),
				Component.text("/", NamedTextColor.GRAY),
				Component.text(max, Utils.HYPHEN_COLOR)
		).build();
	}

	public static Component inGameTimeLine(World world) {
		return Component.text().color(NamedTextColor.GRAY).append(
				Component.text(" - ", Utils.HYPHEN_COLOR),
				Component.text(timeManager.formattedTimeFromTick(world.getTime()), Utils.TEXT_COLOR),
				Component.space(),
				timeManager.getMCDayPart(world.getTime()),
				Component.space(),
				timeManager.getMCWeather(world)
		).build();
	}

	public static void updateScoreboardPlayerTeam(Player player) {
		Board board = getBoard(player);
		if (board != null)
			board.setLine(12, playerTeamLine(player));
	}

	public void updateScoreboardPlayerMod(Player player) {
		Board board = getBoard(player);
		if (board != null)
			board.setLine(9, playerModLine(player));
	}

	public static void updateScoreboardOnlineCounter() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Board board = getBoard(player);
			if (board != null)
				board.setLine(6, onlineCounterLine());
		}
	}

	public static void updateScoreboardPlayerTime(Player player) {
		Board board = getBoard(player);
		if (board != null)
			board.setLine(3, inGameTimeLine(player.getWorld()));
	}
}
