package com.gmail.perhapsitisyeazz.yeazzzsurvival.commands;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerTeamManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerTeam;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeamMsgCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            PlayerTeam team = playerData.getTeam();
            if (team == null) {
                player.sendMessage(Utils.errorMessage("Vous n'êtes pas dans une équipe."));
                return true;
            }
            if (args.length == 0) {
                player.sendMessage(Utils.errorMessage("Écris un message à la suite de la commande."));
                return true;
            }
            TextComponent.Builder builder = Component.text().append(Component.text().color(NamedTextColor.WHITE).decorate(TextDecoration.BOLD),
                    Component.text("[", Utils.HYPHEN_COLOR),
                    Component.text("Équipe"),
                    Component.text("] ", Utils.HYPHEN_COLOR),
                    Component.text(player.getName() + ": "));
            for (String string : args)
                    builder.append(Component.text(string + " "));
            Component component = Utils.tchatUtil(builder.build(), player);
            List<OfflinePlayer> players = PlayerTeamManager.getPlayersFromPlayerTeam(team, true);
            for (OfflinePlayer offlinePlayer : players) {
                Player onlinePlayer = offlinePlayer.getPlayer();
                if (onlinePlayer != null)
                    onlinePlayer.sendMessage(component);
            }
        } else
            sender.sendMessage(Utils.errorMessage("Cette commande est réservée pour les joueurs."));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
