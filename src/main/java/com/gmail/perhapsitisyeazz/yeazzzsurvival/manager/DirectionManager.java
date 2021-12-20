package com.gmail.perhapsitisyeazz.yeazzzsurvival.manager;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.YeazzzSurvival;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeam;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Direction;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DirectionManager {

	public static Direction playerDirection(Player player) {
		double yaw = player.getLocation().getYaw();
		int i = (int) (((yaw+180)+22.5F)%360)/45;
		if (i >= 0)
			return Direction.directions[i];
		else
			return Direction.NOT_FOUND;
	}

	public static void teamDirection(Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(YeazzzSurvival.getInstance(), () -> {
			if (!player.isOnline())
				return;
			PlayerData playerData = PlayerDataManager.getPlayerData(player);
			PlayerTeam team = playerData.getTeam();
			if (team == null)
				return;
			TextComponent.Builder builder = Component.text();
			for (OfflinePlayer target : PlayerTeamManager.getPlayersFromPlayerTeam(team, true)) {
				Player onlinePlayer = target.getPlayer();
				if (onlinePlayer != null && !onlinePlayer.getUniqueId().equals(target.getUniqueId()))
					builder.append(
							Component.space(),
							sendTeamDirection(player, onlinePlayer)
					);
			}
			TextComponent component = builder.build();
			if (!PlainTextComponentSerializer.plainText().serialize(component).equals(""))
				player.sendActionBar(component);
		}, 0L, 20L);
	}

	private static Component sendTeamDirection(Player player, Player target) {
		int distance = flatDistance(player, target);
		Direction playerCP = playerDirection(player),
				targetCP = cardinalDirection(player, target);
		if (distance > 50)
			return Component.text().color(Utils.TEXT_COLOR)
				.append(Component.text(target.getName(), Utils.HYPHEN_COLOR),
						Component.text(" " + distance + " "),
						Component.text(arrow(playerCP, targetCP))).build();
		else
			return Component.text("");
	}

	private static String arrow(Direction cp1, Direction cp2) {
		if (cp1 == Direction.NOT_FOUND) return "✘";
		else if (cp1 == cp2) return "↑";
		else if (Direction.isOpposite(cp1, cp2)) return "↓";
		else if (Direction.isAtRight(cp1, cp2)) return "→";
		else if (Direction.isAtLeft(cp1, cp2)) return "←";
		else if (Direction.isAtHalfRight(cp1, cp2)) return "⬈";
		else if (Direction.isAtHalfLeft(cp1, cp2)) return "⬉";
		else if (Direction.isAtOneAndAHalfRight(cp1, cp2)) return "⬊";
		else if (Direction.isAtOneAndAHalfLeft(cp1, cp2)) return "⬋";
		else return "✔";
	}

	private static Direction cardinalDirection(Player player, Player target) {
		Location from = player.getLocation(),
				to = target.getLocation();
		double dx = from.getX() - to.getX(),
				dz = from.getZ() - to.getZ();
		boolean isNS = (Math.abs(dz) > Math.abs(dx) * 3),
				isEW = (Math.abs(dx) > Math.abs(dz) * 3),
				xPos = dx > 0,
				zPos = dz > 0;
		if (isNS) return zPos ? Direction.NORTH : Direction.SOUTH;
		if (isEW) return xPos ? Direction.WEST : Direction.EAST;
		if (zPos) return xPos ? Direction.NORTH_WEST : Direction.NORTH_EAST;
		return xPos ? Direction.SOUTH_WEST : Direction.SOUTH_EAST;
	}

	public static int flatDistance(Player player, Player target) {
		Location from = player.getLocation(),
				to = target.getLocation();
		if (from.getWorld() != to.getWorld())
			return 0;
		from.setY(to.getY());
		return (int) from.distance(to);
	}
}
