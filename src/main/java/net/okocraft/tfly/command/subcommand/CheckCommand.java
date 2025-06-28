package net.okocraft.tfly.command.subcommand;

import net.kyori.adventure.text.Component;
import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.TabCompletionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CheckCommand implements SubCommand {

    private final TFlyDataProvider dataProvider;

    public CheckCommand(@NotNull TFlyDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public @NotNull Component help() {
        return MessageKeys.COMMAND_CHECK_HELP;
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.check";
    }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length < 2) {
            sender.sendMessage(this.help());
            return;
        }

        var target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            sender.sendMessage(MessageKeys.COMMAND_GENERAL_PLAYER_NOT_FOUND.apply(args[1]));
            return;
        }

        var data = dataProvider.getIfLoaded(target.getUniqueId());

        if (data != null && data.status() == TFlyData.Status.RUNNING) {
            sender.sendMessage(MessageKeys.COMMAND_CHECK_FLYING.apply(target.getName(), data.remainingTime()));
            return;
        }

        if (target.isFlying()) {
            sender.sendMessage(MessageKeys.COMMAND_CHECK_FLYING_BUT_NOT_TFLY.apply(target.getName()));
        } else {
            sender.sendMessage(MessageKeys.COMMAND_CHECK_NOT_FLYING.apply(target.getName()));
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length == 2) {
            return TabCompletionUtils.players(args[1]);
        }
        return Collections.emptyList();
    }
}
