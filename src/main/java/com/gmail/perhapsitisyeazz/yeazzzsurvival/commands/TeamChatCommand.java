package com.gmail.perhapsitisyeazz.yeazzzsurvival.commands;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeam;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamChatCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player player) {
			PlayerData playerData = PlayerDataManager.getPlayerData(player);
			PlayerTeam team = playerData.getTeam();
			if (team == null) {
				player.sendMessage(Utils.errorMessage("Vous n'êtes pas dans une équipe."));
				return true;
			}
			boolean inTeamChat = playerData.isTeamChat();
			TextComponent component = Component.text().color(NamedTextColor.GRAY).append(
					Component.text("» ", Utils.HYPHEN_COLOR),
					Component.text("Tchat d'équipe "),
					Component.text(inTeamChat ? "désactivé" : "activé", inTeamChat ? NamedTextColor.RED : NamedTextColor.DARK_GREEN),
					Component.text(".")
			).build();
			playerData.setTeamChat(!inTeamChat);
			player.sendMessage(component);
		} else
			sender.sendMessage(Utils.errorMessage("Cette commande est réservée pour les joueurs."));
		return true;
	}
}
