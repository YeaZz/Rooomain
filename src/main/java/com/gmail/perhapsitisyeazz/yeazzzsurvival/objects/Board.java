package com.gmail.perhapsitisyeazz.yeazzzsurvival.objects;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Board {

    private final Player player;
    private final Scoreboard oldScoreboard;
    private final Scoreboard scoreboard;
    private final Objective board;
    private final Team[] lines = new Team[15];
    private final String[] entries = new String[]{"&1&r", "&2&r", "&3&r", "&4&r", "&5&r", "&6&r", "&7&r", "&8&r", "&9&r", "&0&r", "&a&r", "&b&r", "&c&r", "&d&r", "&e&r"};
    private boolean on;

    public Board(Player player) {
        this.player = player;
        this.on = true;
        this.oldScoreboard = player.getScoreboard();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.player.setScoreboard(scoreboard);
        board = scoreboard.registerNewObjective(
                "Board",
                "dummy",
                Component.text("Survie Minecraft", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)
        );
        board.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i = 0; i < 15; i++) {
            lines[i] = this.scoreboard.registerNewTeam("line" + (i + 1));
        }
        for (int i = 0; i < 15; i++) {
            lines[i].addEntry(Utils.getColString(entries[i]));
        }
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public void setLine(int line, Component content) {
        Team t = lines[line - 1];
        t.prefix(content);
        t.suffix(Component.empty());
        board.getScore(Utils.getColString(entries[line - 1])).setScore(line);
    }

    public void deleteLine(int line) {
        this.scoreboard.resetScores(Utils.getColString(entries[line - 1]));
        lines[line - 1].unregister();
    }

    public void clearBoard() {
        for (int i = 1; i < 15; i++) {
            deleteLine(i);
        }
    }

    public void toggle(boolean on) {
        if (on)
            player.setScoreboard(this.scoreboard);
        else
            player.setScoreboard(this.oldScoreboard);
        this.on = on;
    }

    public boolean isOn() {
        return this.on;
    }
}
