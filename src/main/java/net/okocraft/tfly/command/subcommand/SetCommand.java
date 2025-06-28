package net.okocraft.tfly.command.subcommand;

import net.kyori.adventure.text.Component;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.TabCompletionUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SetCommand extends AbstractTFlyDataCommand {

    public SetCommand(@NotNull TFlyDataProvider dataProvider) {
        super(dataProvider);
    }

    @Override
    public @NotNull Component help() {
        return MessageKeys.COMMAND_SET_HELP;
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.set";
    }


    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length < 3) {
            sender.sendMessage(this.help());
            return;
        }

        Long seconds = parseNumber(sender, args[2]);

        if (seconds == null) {
            return;
        }

        var data = getTFlyDataFromSenderOrArgument(sender, args[1]);

        if (data == null) {
            return;
        }

        long now = data.remainingTime(current -> Math.max(seconds, 0));
        sender.sendMessage(MessageKeys.COMMAND_SET_SUCCESS.apply(args[1], now));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        return args.length == 2 ? TabCompletionUtils.players(args[1]) : Collections.emptyList();
    }
}
