package com.gmail.perhapsitisyeazz.yeazzzsurvival.manager;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.YeazzzSurvival;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeam;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeamGson;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerTeamManager {

	private static final List<PlayerTeam> TEAM_LIST = new ArrayList<>();

	public static void loadPlayersTeam() {
		Gson gson = new Gson();
		File file = new File(YeazzzSurvival.PLAYER_TEAM_FILE_PATH);
		if (!file.exists())
			return;
		File[] listFiles = file.listFiles();
		if (listFiles == null)
			return;
		for (File f : listFiles) {
			try {
				Reader reader = Files.newBufferedReader(f.toPath());
				PlayerTeamGson teamGson = gson.fromJson(reader, PlayerTeamGson.class);
				reader.close();
				OfflinePlayer player = Bukkit.getOfflinePlayer(teamGson.getOwnerUUID());
				PlayerTeam team = new PlayerTeam(teamGson.getName(), NamedTextColor.NAMES.value(teamGson.getColor()), player);
				for (UUID uuid : teamGson.getMembersUUID()) {
					OfflinePlayer member = Bukkit.getOfflinePlayer(uuid);
					team.addMember(member);
				}
				TEAM_LIST.add(team);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void savePlayersTeam() {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		for (PlayerTeam team : TEAM_LIST) {
			File file = new File(YeazzzSurvival.PLAYER_TEAM_FILE_PATH + team.getName() + ".json");
			if (!file.exists()) {
				try {
					if (!file.createNewFile())
						System.out.println("Cannot create " + file.getAbsolutePath() + " file");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				List<UUID> membersUUID = new ArrayList<>();
				for (OfflinePlayer member : team.getMembers()) {
					membersUUID.add(member.getUniqueId());
				}
				PlayerTeamGson playerTeamGson = new PlayerTeamGson(
						team.getName(), team.getColor().toString(), team.getOwner().getUniqueId(), membersUUID
				);
				Writer writer = Files.newBufferedWriter(file.toPath());
				gson.toJson(playerTeamGson, writer);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void createPlayerTeam(String name, NamedTextColor color, Player player) {
		PlayerTeam team = new PlayerTeam(name, color, player);
		TEAM_LIST.add(team);
		PlayerData playerData = PlayerDataManager.getPlayerData(player);
		playerData.setTeam(team);
		BoardManager.updateScoreboardPlayerTeam(player);
		TextComponent component = Component.text().color(NamedTextColor.GRAY).append(
				Component.text("» ", Utils.HYPHEN_COLOR),
				Component.text(player.getName(), Utils.TEXT_COLOR),
				Component.text(" a créé l'équipe "),
				Component.text(name, color),
				Component.text(".")
		).build();
		Utils.sendToAllPlayers(component);
	}

	public static void deletePlayerTeam(PlayerTeam team) {
		final List<OfflinePlayer> players = new ArrayList<>(team.getMembers());
		players.add(team.getOwner());
		for (OfflinePlayer player : players) {
			PlayerData playerData = PlayerDataManager.getPlayerData(player);
			playerData.setTeam(null);
			playerData.setTeamChat(false);
			if (player.isOnline()) {
				Player onlinePlayer = player.getPlayer();
				BoardManager.updateScoreboardPlayerTeam(onlinePlayer);
			}
		}
		Player player = team.getOwner().getPlayer();
		assert player != null;
		Component component = Component.text().color(NamedTextColor.GRAY).append(
				Component.text("» ", Utils.HYPHEN_COLOR),
				Component.text(player.getName(), Utils.TEXT_COLOR),
				Component.text(" a dissous "),
				Component.text(team.getName(), team.getColor()),
				Component.text(".")
		).build();
		Utils.sendToAllPlayers(component);
		TEAM_LIST.remove(team);
	}

	public static PlayerTeam getPlayerTeam(String name) {
		for (PlayerTeam team : TEAM_LIST) {
			if (team.getName().equals(name))
				return team;
		}
		return null;
	}

	public static void joinTeam(Player player, PlayerTeam team) {
		PlayerData playerData = PlayerDataManager.getPlayerData(player);
		playerData.setTeam(team);
		team.addMember(player);
		BoardManager.updateScoreboardPlayerTeam(player);
		TextComponent component = Component.text().color(NamedTextColor.GRAY).append(
				Component.text("» ", Utils.HYPHEN_COLOR),
				Component.text(player.getName(), Utils.TEXT_COLOR),
				Component.text(" a rejoint l'équipe "),
				Component.text(team.getName(), team.getColor()),
				Component.text(".")
		).build();
		Utils.sendToAllPlayers(component);

	}

	public static void leaveTeam(Player player, PlayerTeam team) {
		PlayerData playerData = PlayerDataManager.getPlayerData(player);
		team.removeMember(player);
		playerData.setTeam(null);
		playerData.setTeamChat(false);
		TextComponent.Builder component = Component.text().color(NamedTextColor.GRAY).append(
				Component.text("» ", Utils.HYPHEN_COLOR),
				Component.text(player.getName(), Utils.TEXT_COLOR),
				Component.text(" a quitté l'équipe "),
				Component.text(team.getName(), team.getColor()),
				Component.text(".")
		);
		BoardManager.updateScoreboardPlayerTeam(player);
		Utils.sendToAllPlayers(component.build());
	}

	public static boolean playerTeamExists(String string) {
		for (PlayerTeam playerTeam : TEAM_LIST) {
			if (playerTeam.getName().equals(string))
				return true;
		}
		return false;
	}

	public static List<PlayerTeam> allTeams() {
		return TEAM_LIST;
	}

	public static List<OfflinePlayer> getPlayersFromPlayerTeam(PlayerTeam team, boolean online) {
		final List<OfflinePlayer> result = new ArrayList<>();
		final List<OfflinePlayer> players = new ArrayList<>(team.getMembers());
		players.add(team.getOwner());
		for (OfflinePlayer player : players) {
			if (player.isOnline() == online)
				result.add(player);
		}
		return result;
	}
}
