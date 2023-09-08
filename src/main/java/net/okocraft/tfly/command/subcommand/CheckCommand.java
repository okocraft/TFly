package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.HelpFactory;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.message.Placeholders;
import net.okocraft.tfly.util.LocaleUtils;
import net.okocraft.tfly.util.TabCompletionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CheckCommand implements SubCommand {

    private final MiniMessageLocalization localization;
    private final TFlyDataProvider dataProvider;

    public CheckCommand(@NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        this.localization = localization;
        this.dataProvider = dataProvider;
    }

    @Override
    public @NotNull MiniMessageBuilder help(@NotNull Locale locale) {
        return HelpFactory.create(
                localization.findSource(locale),
                MessageKeys.COMMAND_CHECK_HELP,
                MessageKeys.COMMAND_CHECK_COMMANDLINE
        );
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.check";
    }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var locale = LocaleUtils.getFrom(sender);

        if (args.length < 2) {
            help(locale).send(sender);
            return;
        }

        var target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            localization.findSource(locale)
                    .builder()
                    .key(MessageKeys.COMMAND_GENERAL_PLAYER_NOT_FOUND)
                    .tagResolver(Placeholders.player(args[1]))
                    .send(sender);
            return;
        }

        var data = dataProvider.getIfLoaded(target.getUniqueId());
        var source = localization.findSource(locale);
        var builder = source.builder().tagResolver(Placeholders.player(target.getName()));

        if (data != null && data.status() == TFlyData.Status.RUNNING) {
            builder.key(MessageKeys.COMMAND_CHECK_FLYING).tagResolver(Placeholders.remainingTime(data.remainingTime(), source));
            return;
        }

        if (target.isFlying()) {
            builder.key(MessageKeys.COMMAND_CHECK_FLYING_BUT_NOT_TFLY);
        } else {
            builder.key(MessageKeys.COMMAND_CHECK_NOT_FLYING);
        }

        builder.send(sender);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length == 2) {
            return TabCompletionUtils.players(args[1]);
        }
        return Collections.emptyList();
    }
}
