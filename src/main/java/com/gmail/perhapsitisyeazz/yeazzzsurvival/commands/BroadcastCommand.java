package com.gmail.perhapsitisyeazz.yeazzzsurvival.commands;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if (args.length > 0) {
                TextComponent.Builder builder = Component.newline().toBuilder().append(
                        Component.text("[", Utils.HYPHEN_COLOR),
                        Component.text("Broadcast", Utils.TEXT_COLOR),
                        Component.text("] ", Utils.HYPHEN_COLOR)
                );
                for (String string : args)
                    builder.append(Component.text(string + " "));
                Utils.sendToAllPlayers(builder.append(Component.newline()).build());
            } else
                sender.sendMessage(Utils.errorMessage("Vous n'avez pas saisis de texte."));
        } else
            sender.sendMessage(Utils.errorMessage("Vous n'avez pas la permission."));
        return true;
    }
}
