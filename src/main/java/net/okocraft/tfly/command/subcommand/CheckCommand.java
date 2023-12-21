package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.minimessage.base.MiniMessageBase;
import com.github.siroshun09.messages.minimessage.localization.MiniMessageLocalization;
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

    private final MiniMessageLocalization localization;
    private final TFlyDataProvider dataProvider;

    public CheckCommand(@NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        this.localization = localization;
        this.dataProvider = dataProvider;
    }

    @Override
    public @NotNull MiniMessageBase help() {
        return MessageKeys.COMMAND_CHECK_HELP;
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.check";
    }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var source = localization.findSource(sender);

        if (args.length < 2) {
            help().source(source).send(sender);
            return;
        }

        var target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            MessageKeys.COMMAND_GENERAL_PLAYER_NOT_FOUND.apply(args[1]).source(source).send(sender);
            return;
        }

        var data = dataProvider.getIfLoaded(target.getUniqueId());

        if (data != null && data.status() == TFlyData.Status.RUNNING) {
            MessageKeys.COMMAND_CHECK_FLYING.apply(target.getName(), data.remainingTime()).source(source).send(sender);
            return;
        }

        if (target.isFlying()) {
            MessageKeys.COMMAND_CHECK_FLYING_BUT_NOT_TFLY.apply(target.getName()).source(source).send(sender);
        } else {
            MessageKeys.COMMAND_CHECK_NOT_FLYING.apply(target.getName()).source(source).send(sender);
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
