package com.gmail.perhapsitisyeazz.yeazzzsurvival.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerTeam {

    private final String name;
    private final NamedTextColor color;
    private final OfflinePlayer owner;
    private final List<OfflinePlayer> members;

    public PlayerTeam(String name, NamedTextColor color, OfflinePlayer player) {
        this.name = name;
        this.color = color;
        this.owner = player;
        this.members = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public Component getDisplayName() {
        return Component.text().color(NamedTextColor.DARK_GRAY).append(
                Component.text("["),
                Component.text(this.name, this.color),
                Component.text("] ")
        ).build();
    }

    public NamedTextColor getColor() {
        return this.color;
    }

    public OfflinePlayer getOwner() {
        return this.owner;
    }

    public List<OfflinePlayer> getMembers() {
        return this.members;
    }

    public void addMember(OfflinePlayer player) {
        members.add(player);
    }

    public void removeMember(OfflinePlayer player) {
        members.remove(player);
    }
}
