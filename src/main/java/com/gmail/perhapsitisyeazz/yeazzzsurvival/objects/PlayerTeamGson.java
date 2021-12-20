package com.gmail.perhapsitisyeazz.yeazzzsurvival.objects;

import java.util.List;
import java.util.UUID;

public final class PlayerTeamGson {

    private final String name;
    private final String color;
    private final UUID ownerUUID;
    private final List<UUID> membersUUID;

    public PlayerTeamGson(String name, String color,
                          UUID ownerUUID, List<UUID> membersUUID) {
        this.name = name;
        this.color = color;
        this.ownerUUID = ownerUUID;
        this.membersUUID = membersUUID;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public List<UUID> getMembersUUID() {
        return membersUUID;
    }
}
