package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import com.github.siroshun09.messages.api.util.MessageBuilderFactory;
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

public class SetCommand extends AbstractTFlyDataCommand {

    public SetCommand(@NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        super(localization, dataProvider);
    }

    @Override
    public @NotNull MiniMessageBuilder help(@NotNull Locale locale) {
        return HelpFactory.create(
                () -> localization.findSource(locale).builder(),
                MessageKeys.COMMAND_SET_HELP,
                MessageKeys.COMMAND_SET_COMMANDLINE
        );
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.set";
    }


    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var locale = LocaleUtils.getFrom(sender);

        if (args.length < 3) {
            help(locale).send(sender);
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
        MessageBuilderFactory<MiniMessageBuilder> factory = () -> localization.findSource(locale).builder();
        factory.create()
                .key(MessageKeys.COMMAND_SET_SUCCESS)
                .tagResolvers(
                        Placeholders.player(args[1]),
                        Placeholders.remainingTime(now, factory)
                ).send(sender);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        return args.length == 2 ? TabCompletionUtils.players(args[1]) : Collections.emptyList();
    }
}
