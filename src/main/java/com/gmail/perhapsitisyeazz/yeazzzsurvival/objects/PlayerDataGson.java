package com.gmail.perhapsitisyeazz.yeazzzsurvival.objects;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Mod;

import java.util.UUID;

public final class PlayerDataGson {

    private final String teamName;
    private final boolean teamChat;
    private final Mod mod;
    private final UUID lastMessageSenderUUID;

    public PlayerDataGson(String teamName, boolean teamChat,
                          Mod mod,
                          UUID lastMessageSenderUUID) {
        this.teamName = teamName;
        this.teamChat = teamChat;
        this.mod = mod;
        this.lastMessageSenderUUID = lastMessageSenderUUID;
    }

    public String getTeamName() {
        return teamName;
    }

    public boolean isTeamChat() {
        return teamChat;
    }

    public Mod getMod() {
        return mod;
    }

    public UUID getLastMessageSenderUUID() {
        return lastMessageSenderUUID;
    }
}
