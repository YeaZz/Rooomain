package com.gmail.perhapsitisyeazz.yeazzzsurvival.commands;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.BoardManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.PlayerDataManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.PlayerData;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Mod;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModCommand implements CommandExecutor, TabCompleter {

    private final BoardManager boardManager = new BoardManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            Mod mod = playerData.getMod();
            if(args.length == 0)
                player.sendMessage(Component.text("Tu es en mode ", NamedTextColor.GRAY)
                        .append(Component.text(mod.toString().toLowerCase())));
            else {
                String arg = args[0];
                if (arg.equals("agressif") || arg.equals("a")) {
                    if (mod == Mod.AGRESSIF) {
                        player.sendMessage(Utils.errorMessage("Tu es déjà en mode Agressif"));
                        return true;
                    }
                    playerData.setMod(Mod.AGRESSIF);
                    boardManager.updateScoreboardPlayerMod(player);
                } else if (arg.equals("pacifique") || arg.equals("p")) {
                    if (mod == Mod.PACIFIQUE) {
                        player.sendMessage(Utils.errorMessage("Tu es déjà en mode Pacifique"));
                        return true;
                    }
                    playerData.setMod(Mod.PACIFIQUE);
                    boardManager.updateScoreboardPlayerMod(player);
                } else
                    player.sendMessage(Utils.helpComponent(command.getName(), subCommands, subCommandsUsage, subCommandsDescription, subCommandsClickActionType));
            }
        }else
            sender.sendMessage(Utils.errorMessage("Cette commande est réservée pour les joueurs."));

        return true;
    }

    private final String[] subCommands = {"agressif", "pacifique"},
            subCommandsUsage = {"", ""},
            subCommandsDescription = {"Mode agressif", "Mode pacifique"};

    private final ClickEvent.Action[] subCommandsClickActionType = {ClickEvent.Action.RUN_COMMAND, ClickEvent.Action.RUN_COMMAND};

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> argCommands;
        if (args.length == 1) {
            argCommands = Arrays.asList(subCommands);
            StringUtil.copyPartialMatches(args[0], argCommands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
