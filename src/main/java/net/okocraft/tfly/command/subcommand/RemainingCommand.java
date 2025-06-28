package net.okocraft.tfly.command.subcommand;

import net.kyori.adventure.text.Component;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.TabCompletionUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RemainingCommand extends AbstractTFlyDataCommand {

    public RemainingCommand(@NotNull TFlyDataProvider dataProvider) {
        super(dataProvider);
    }

    @Override
    public @NotNull Component help() {
        return MessageKeys.COMMAND_REMAINING_HELP;
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.remaining";
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        boolean self = args.length == 1 || sender.getName().equalsIgnoreCase(args[1]);

        if (!self && !sender.hasPermission(otherPermissionNode())) {
            sender.sendMessage(MessageKeys.NO_PERMISSION.apply(otherPermissionNode()));
            return;
        }

        var data = getTFlyDataFromSenderOrArgument(sender, self ? null : args[1]);

        if (data == null) {
            return;
        }

        long remainingTime = data.remainingTime();

        if (remainingTime < 1) {
            if (self) {
                sender.sendMessage(MessageKeys.COMMAND_REMAINING_NO_TIME_SELF);
            } else {
                sender.sendMessage(MessageKeys.COMMAND_REMAINING_NO_TIME_OTHER.apply(args[1]));
            }
        } else {
            if (self) {
                sender.sendMessage(MessageKeys.COMMAND_REMAINING_TIME_SELF.apply(remainingTime));
            } else {
                sender.sendMessage(MessageKeys.COMMAND_REMAINING_TIME_OTHER.apply(args[1], remainingTime));
            }
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length == 2 && sender.hasPermission(otherPermissionNode())) {
            return TabCompletionUtils.players(args[1]);
        }
        return Collections.emptyList();
    }

    private @NotNull String otherPermissionNode() {
        return permissionNode() + ".other";
    }
}
