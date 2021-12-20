package com.gmail.perhapsitisyeazz.yeazzzsurvival.objects;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Mod;
import org.bukkit.OfflinePlayer;

public class PlayerData {

    private PlayerTeam team;
    private boolean teamChat;
    private Mod mod;
    private OfflinePlayer lastMessageSender;

    public PlayerData() {
        this.team = null;
        this.teamChat = false;
        this.mod = Mod.PACIFIQUE;
        this.lastMessageSender = null;
    }

    public void setTeam(PlayerTeam team) {
        this.team = team;
    }

    public PlayerTeam getTeam() {
        return this.team;
    }

    public void setTeamChat(boolean bool) {
        this.teamChat = bool;
    }

    public boolean isTeamChat() {
        return this.teamChat;
    }

    public void setMod(Mod mod) {
        this.mod = mod;
    }

    public Mod getMod() {
        return this.mod;
    }

    public void setLastMessageSender(OfflinePlayer player) {
        this.lastMessageSender = player;
    }

    public OfflinePlayer getLastMessageSender() {
        return lastMessageSender;
    }
}
