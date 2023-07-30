package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.HelpFactory;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.message.Placeholders;
import net.okocraft.tfly.util.LocaleUtils;
import net.okocraft.tfly.util.TabCompletionUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RemainingCommand extends AbstractTFlyDataCommand {

    public RemainingCommand(@NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        super(localization, dataProvider);
    }

    @Override
    public @NotNull MiniMessageBuilder help(@NotNull Locale locale) {
        return HelpFactory.create(
                () -> localization.findSource(locale).builder(),
                MessageKeys.COMMAND_REMAINING_HELP,
                MessageKeys.COMMAND_REMAINING_COMMANDLINE
        );
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.remaining";
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        boolean self = args.length == 1;
        var locale = LocaleUtils.getFrom(sender);

        if (!self && !sender.hasPermission(otherPermissionNode())) {
            localization.findSource(locale)
                    .builder()
                    .key(MessageKeys.NO_PERMISSION)
                    .tagResolver(Placeholders.permission(otherPermissionNode()))
                    .send(sender);
            return;
        }

        var data = getTFlyDataFromSenderOrArgument(sender, self ? null : args[1]);

        if (data == null) {
            return;
        }

        long remainingTime = data.remainingTime();
        var builder = localization.findSource(locale).builder();

        if (remainingTime < 1) {
            if (self) {
                builder.key(MessageKeys.COMMAND_REMAINING_NO_TIME_SELF);
            } else {
                builder.key(MessageKeys.COMMAND_REMAINING_NO_TIME_OTHER).tagResolver(Placeholders.player(args[1]));
            }
        } else {
            if (self) {
                builder.key(MessageKeys.COMMAND_REMAINING_TIME_SELF);
            } else {
                builder.key(MessageKeys.COMMAND_REMAINING_TIME_OTHER).tagResolver(Placeholders.player(args[1]));
            }
            builder.tagResolver(Placeholders.remainingTime(data.remainingTime(), localization.findSource(locale)::builder));
        }

        builder.send(sender);
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
