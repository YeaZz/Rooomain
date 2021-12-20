package com.gmail.perhapsitisyeazz.yeazzzsurvival.manager;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeam;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerListManager {

    public static void reload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = BoardManager.getBoard(player).getScoreboard();
            for (Team team : scoreboard.getTeams()) {
                if (!team.getName().contains("line"))
                    team.unregister();
            }
            Team defaultTeam = scoreboard.registerNewTeam("default");
            defaultTeam.color(NamedTextColor.GRAY);
            for (PlayerTeam playerTeam : PlayerTeamManager.allTeams()) {
                Team team = scoreboard.registerNewTeam(playerTeam.getName());
                team.color(NamedTextColor.GRAY);
                team.prefix(playerTeam.getDisplayName());
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                PlayerData playersData = PlayerDataManager.getPlayerData(player);
                PlayerTeam playersTeam = playersData.getTeam();
                if (playersTeam == null)
                    defaultTeam.addEntry(players.getName());
                else {
                    Team minecraftTeam = scoreboard.getTeam(playersTeam.getName());
                    if (minecraftTeam == null)
                        continue;
                    minecraftTeam.addEntry(players.getName());
                }
            }
        }
    }
}
