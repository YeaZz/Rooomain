package com.gmail.perhapsitisyeazz.rooomain;

import com.gmail.perhapsitisyeazz.rooomain.commands.DeathCountCommand;
import com.gmail.perhapsitisyeazz.rooomain.commands.TeamChatCommand;
import com.gmail.perhapsitisyeazz.rooomain.commands.TeamCommand;
import com.gmail.perhapsitisyeazz.rooomain.listeners.ChatEvent;
import com.gmail.perhapsitisyeazz.rooomain.listeners.DamageEvent;
import com.gmail.perhapsitisyeazz.rooomain.listeners.DeathEvent;
import com.gmail.perhapsitisyeazz.rooomain.listeners.JoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Rooomain extends JavaPlugin {

	private static Rooomain instance;
	public static Rooomain getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
		getServer().getPluginManager().registerEvents(new DamageEvent(this), this);
		getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
		getServer().getPluginManager().registerEvents(new DeathEvent(this), this);
		getCommand("deathcount").setExecutor(new DeathCountCommand());
		getCommand("teamchat").setExecutor(new TeamChatCommand());
		getCommand("team").setExecutor(new TeamCommand());
	}

	@Override
	public void onDisable() {
		instance = null;
	}
}
