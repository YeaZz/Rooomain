package com.gmail.perhapsitisyeazz.yeazzzsurvival.manager;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.YeazzzSurvival;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerDataGson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    public final static Map<OfflinePlayer, PlayerData> PLAYER_DATA_LIST = new HashMap<>();

    public static void loadPlayersData() {
        Gson gson = new Gson();
        File file = new File(YeazzzSurvival.PLAYER_DATA_FILE_PATH);
        if (!file.exists())
            return;
        File[] listFiles = file.listFiles();
        if (listFiles == null)
            return;
        for (File f : listFiles) {
            try {
                Reader reader = Files.newBufferedReader(f.toPath());
                PlayerDataGson playerDataGson = gson.fromJson(reader, PlayerDataGson.class);
                reader.close();
                UUID uuid = UUID.fromString(f.getName().split("\\.")[0]);
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                PlayerData playerData = new PlayerData();
                playerData.setTeam(PlayerTeamManager.getPlayerTeam(playerDataGson.getTeamName()));
                playerData.setTeamChat(playerDataGson.isTeamChat());
                playerData.setMod(playerDataGson.getMod());
                playerData.setLastMessageSender(Bukkit.getPlayer(playerDataGson.getLastMessageSenderUUID()));
                PLAYER_DATA_LIST.put(player, playerData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void savePlayersData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        for (Map.Entry<OfflinePlayer, PlayerData> entry : PLAYER_DATA_LIST.entrySet()) {
            File file = new File(YeazzzSurvival.PLAYER_DATA_FILE_PATH + entry.getKey().getUniqueId() + ".json");
            if (!file.exists()) {
                try {
                    if (!file.createNewFile())
                        System.out.println("Cannot create " + file.getAbsolutePath() + " file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PlayerData playerData = entry.getValue();
            String teamName;
            UUID lastMessageSenderUUID;
            if (playerData.getTeam() != null)
                teamName = playerData.getTeam().getName();
            else
                teamName = "";
            if (playerData.getLastMessageSender() != null)
                lastMessageSenderUUID = playerData.getLastMessageSender().getUniqueId();
            else
                lastMessageSenderUUID = null;
            PlayerDataGson playerDataGson = new PlayerDataGson(teamName, playerData.isTeamChat(), playerData.getMod(), lastMessageSenderUUID);
            try {
                Writer writer = Files.newBufferedWriter(file.toPath());
                gson.toJson(playerDataGson, writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static PlayerData getPlayerData(OfflinePlayer player) {
        return PLAYER_DATA_LIST.get(player);
    }

    public static void createPlayerData(Player player) {
        PlayerData playerData = new PlayerData();
        PLAYER_DATA_LIST.put(player, playerData);
    }

    public static boolean noPlayerData(Player player) {
        return getPlayerData(player) == null;
    }
}
