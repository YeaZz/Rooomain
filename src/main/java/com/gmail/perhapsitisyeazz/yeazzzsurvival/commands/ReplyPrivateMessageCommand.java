package com.gmail.perhapsitisyeazz.yeazzzsurvival.commands;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReplyPrivateMessageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                PlayerData playerData = PlayerDataManager.getPlayerData(player);
                OfflinePlayer offlineTarget = playerData.getLastMessageSender();
                if (offlineTarget == null) {
                    player.sendMessage(Utils.errorMessage("Vous n'avez personne à qui répondre."));
                    return true;
                }
                if (!offlineTarget.isOnline()) {
                    assert offlineTarget.getName() != null;
                    player.sendMessage(Utils.errorMessage(offlineTarget.getName() + " n'est pas en ligne."));
                    return true;
                }
                Player target = offlineTarget.getPlayer();
                assert target != null;
                TextComponent.Builder builder = Component.text();
                for (String arg : args)
                    builder.append(Component.text(arg + " "));
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
                Component component = Utils.tchatUtil(builder.build(), player);
                sender.sendMessage(senderComponent.append(component));
                target.sendMessage(targetComponent.append(component));
            }
        } else
            sender.sendMessage(Utils.errorMessage("Cette commande est réservée pour les joueurs."));
        return true;
    }
}
