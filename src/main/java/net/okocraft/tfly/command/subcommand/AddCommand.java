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

public class AddCommand extends AbstractTFlyDataCommand {

    public AddCommand(@NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        super(localization, dataProvider);
    }

    @Override
    public @NotNull MiniMessageBuilder help(@NotNull Locale locale) {
        return HelpFactory.create(
                localization.findSource(locale),
                MessageKeys.COMMAND_ADD_HELP,
                MessageKeys.COMMAND_ADD_COMMANDLINE
        );
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.add";
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var locale = LocaleUtils.getFrom(sender);

        if (args.length < 3) {
            help(locale).send(sender);
            return;
        }

        Long adding = parseNumber(sender, args[2]);

        if (adding == null) {
            return;
        }

        var data = getTFlyDataFromSenderOrArgument(sender, args[1]);

        if (data == null) {
            return;
        }

        long now = data.remainingTime(current -> current + Math.max(adding, 0));
        var source = localization.findSource(locale);
        source.builder()
                .key(MessageKeys.COMMAND_ADD_SUCCESS)
                .tagResolvers(
                        Placeholders.player(args[1]),
                        Placeholders.seconds(Math.max(adding, 0)),
                        Placeholders.remainingTime(now, source)
                ).send(sender);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        return args.length == 2 ? TabCompletionUtils.players(args[1]) : Collections.emptyList();
    }
}
