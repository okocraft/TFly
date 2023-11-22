package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.minimessage.base.MiniMessageBase;
import com.github.siroshun09.messages.minimessage.localization.MiniMessageLocalization;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.LocaleUtils;
import net.okocraft.tfly.util.TabCompletionUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RemainingCommand extends AbstractTFlyDataCommand {

    public RemainingCommand(@NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        super(localization, dataProvider);
    }

    @Override
    public @NotNull MiniMessageBase help() {
        return MessageKeys.COMMAND_REMAINING_HELP;
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.remaining";
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        boolean self = args.length == 1;
        var source = this.localization.findSource(LocaleUtils.getFrom(sender));

        if (!self && !sender.hasPermission(otherPermissionNode())) {
            MessageKeys.NO_PERMISSION.apply(otherPermissionNode()).source(source).send(sender);
            return;
        }

        var data = getTFlyDataFromSenderOrArgument(sender, self ? null : args[1]);

        if (data == null) {
            return;
        }

        long remainingTime = data.remainingTime();

        if (remainingTime < 1) {
            if (self) {
                MessageKeys.COMMAND_REMAINING_NO_TIME_SELF.source(source).send(sender);
            } else {
                MessageKeys.COMMAND_REMAINING_NO_TIME_OTHER.apply(args[1]).source(source).send(sender);
            }
        } else {
            if (self) {
                MessageKeys.COMMAND_REMAINING_TIME_SELF.apply(remainingTime).source(source).send(sender);
            } else {
                MessageKeys.COMMAND_REMAINING_TIME_OTHER.apply(args[1], remainingTime).source(source).send(sender);
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
