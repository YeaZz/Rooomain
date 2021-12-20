package com.gmail.perhapsitisyeazz.yeazzzsurvival.commands;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrivateMessageCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length != 0) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Utils.errorMessage("Cette personne n'est pas connecté."));
                return true;
            }
            if (sender == target) {
                sender.sendMessage(Utils.errorMessage("Vous ne pouvez pas envoyez de message a vous même."));
                return true;
            }
            if (args.length > 1) {
                TextComponent.Builder builder = Component.text();
                for (int i = 1; i < args.length; i++)
                    builder.append(Component.text(args[i] + " "));
                Component senderComponent = Component.text().append(
                        Component.text("[", Utils.HYPHEN_COLOR),
                        Component.text("Message pour ", NamedTextColor.GRAY),
                        Component.text(target.getName(), NamedTextColor.DARK_RED),
                        Component.text("] ", Utils.HYPHEN_COLOR)
                ).build();
                Component targetComponent = Component.text().append(
                        Component.text("[", Utils.HYPHEN_COLOR),
                        Component.text("Message de ", NamedTextColor.GRAY),
                        Component.text(sender.getName(), NamedTextColor.DARK_RED),
                        Component.text("] ", Utils.HYPHEN_COLOR)
                ).build();
                if (sender instanceof Player player) {
                    Component component = Utils.tchatUtil(builder.build(), player);
                    sender.sendMessage(senderComponent.append(component));
                    target.sendMessage(targetComponent.append(component));
                    PlayerData playerData = PlayerDataManager.getPlayerData(player);
                    PlayerData targetData = PlayerDataManager.getPlayerData(target);
                    playerData.setLastMessageSender(target);
                    targetData.setLastMessageSender(player);
                } else {
                    sender.sendMessage(senderComponent.append(builder));
                    target.sendMessage(senderComponent.append(builder));
                }
            }
        } else
            sender.sendMessage(Utils.errorMessage("Vous devez préciser a qui envoyer le message."));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> argCommand = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers())
                argCommand.add(player.getName());
            StringUtil.copyPartialMatches(args[0], argCommand, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
