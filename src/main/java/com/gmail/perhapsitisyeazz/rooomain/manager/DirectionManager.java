package com.gmail.perhapsitisyeazz.rooomain.manager;

import com.gmail.perhapsitisyeazz.rooomain.Rooomain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class DirectionManager {

	private final Rooomain instance = Rooomain.getInstance();
	private final TeamManager teamManager = new TeamManager();
	private final DeathCountManager deathCountManager = new DeathCountManager();

	public Direction playerDirection(Player player) {
		double yaw = player.getLocation().getYaw();
		int i = (int) (((yaw+180)+22.5F)%360)/45;
		if (i >= 0)
			return Direction.directions[i];
		else
			return Direction.NOT_FOUND;
	}

	public void teamDirection(Player player) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
			if (!player.isOnline())
				return;
			Team team = teamManager.teamOfPlayer(player);
			if (team == null)
				return;
			List<Player> playersInTeam = teamManager.onlinePlayersInTeam(team);
			TextComponent.Builder builder = Component.text();
			for (Player target : playersInTeam) {
				if (target != player && deathCountManager.getDeathCount(target) < 2)
					builder.append(sendTeamDirection(player, target));
			}
			TextComponent component = builder.build();
			if (!PlainComponentSerializer.plain().serialize(component).equals(""))
				player.sendActionBar(component);
		}, 0L, 20L);
	}

	private Component sendTeamDirection(Player player, Player target) {
		int distance = flatDistance(player, target);
		Direction playerCP = playerDirection(player),
				targetCP = cardinalDirection(player, target);
		return Component.text().color(NamedTextColor.AQUA)
				.append(Component.text(target.getName(), NamedTextColor.DARK_AQUA),
						Component.text(" "+(distance > 1 ? distance+" " : "")),
						Component.text(distance > 1 ? arrow(playerCP, targetCP) : "✔")).build();
	}

	private String arrow(Direction cp1, Direction cp2) {
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

	private Direction cardinalDirection(Player player, Player target) {
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

	public int flatDistance(Player player, Player target) {
		Location from = player.getLocation(),
				to = target.getLocation();
		if (from.getWorld() != to.getWorld())
			return 0;
		from.setY(to.getY());
		return (int) from.distance(to);
	}
}
