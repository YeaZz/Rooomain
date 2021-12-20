package com.gmail.perhapsitisyeazz.yeazzzsurvival;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.commands.*;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.listeners.AntiTntEvent;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.listeners.ChatEvent;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.listeners.JoinEvent;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.listeners.ModRelatedEvent;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerTeamManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class YeazzzSurvival extends JavaPlugin {

	private static YeazzzSurvival instance;
	public static YeazzzSurvival getInstance() {
		return instance;
	}

	public final static String DATA_FILE_PATH = "plugins/YeazzzSurvival/";
	public final static String PLAYER_DATA_FILE_PATH = YeazzzSurvival.DATA_FILE_PATH + "playerData/";
	public final static String PLAYER_TEAM_FILE_PATH = YeazzzSurvival.DATA_FILE_PATH + "playerTeam/";

	@Override
	public void onEnable() {
		instance = this;
		File[] files = {new File(DATA_FILE_PATH), new File(PLAYER_DATA_FILE_PATH), new File(PLAYER_TEAM_FILE_PATH)};
		for (File file : files) {
			if (!file.exists() && !file.mkdirs())
				getLogger().warning("Cannot create " + file.getAbsolutePath());
		}
		PlayerTeamManager.loadPlayersTeam();
		PlayerDataManager.loadPlayersData();
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new AntiTntEvent(this), this);
		pluginManager.registerEvents(new ChatEvent(this), this);
		pluginManager.registerEvents(new JoinEvent(this), this);
		pluginManager.registerEvents(new ModRelatedEvent(this), this);
		PluginCommand board = getCommand("scoreboard"),
				broadcast = getCommand("broadcast"),
				mod = getCommand("mode"),
				playerTeam = getCommand("team"),
				privateMessage = getCommand("privatemessage"),
				replyPrivateMessage = getCommand("reply"),
				teamChat = getCommand("teamchat"),
				teamMsg = getCommand("teammsg");
		if (board != null)
			board.setExecutor(new BoardCommand());
		if (broadcast != null)
			broadcast.setExecutor(new BroadcastCommand());
		if (mod != null)
			mod.setExecutor(new ModCommand());
		if (playerTeam != null)
			playerTeam.setExecutor(new PlayerTeamCommand());
		if (privateMessage != null)
			privateMessage.setExecutor(new PrivateMessageCommand());
		if (replyPrivateMessage != null)
			replyPrivateMessage.setExecutor(new ReplyPrivateMessageCommand());
		if (teamChat != null)
			teamChat.setExecutor(new TeamChatCommand());
		if (teamMsg != null)
			teamMsg.setExecutor(new TeamMsgCommand());
	}

	@Override
	public void onDisable() {
		PlayerDataManager.savePlayersData();
		PlayerTeamManager.savePlayersTeam();
		instance = null;
	}
}
