package com.gmail.perhapsitisyeazz.yeazzzsurvival.commands;

import com.gmail.perhapsitisyeazz.yeazzzsurvival.manager.BoardManager;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.objects.Board;
import com.gmail.perhapsitisyeazz.yeazzzsurvival.utils.Utils;
import net.kyori.adventure.text.event.ClickEvent;
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

public class BoardCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            Board board = BoardManager.getBoard(player);
            if (args.length > 0) {
                String arg = args[0];
                if (arg.equals("on")) {
                    if (board.isOn()) {
                        player.sendMessage(Utils.errorMessage("Le scoreboard est déjà activé."));
                        return true;
                    }
                    board.toggle(true);
                } else if (arg.equals("off")) {
                    if (!board.isOn()) {
                        player.sendMessage(Utils.errorMessage("Le scoreboard est déjà désactivé."));
                        return true;
                    }
                    board.toggle(false);
                } else
                    player.sendMessage(Utils.helpComponent(command.getName(), subCommands, subCommandsUsage, subCommandsDescription, subCommandsClickActionType));
            }
        }else
            sender.sendMessage(Utils.errorMessage("Cette commande est réversée pour les joueurs."));

        return true;
    }

    private final String[] subCommands = {"on", "off"},
            subCommandsUsage = {"", ""},
            subCommandsDescription = {"Activer le scoreboard", "Désactivé le scoreboard"};

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
