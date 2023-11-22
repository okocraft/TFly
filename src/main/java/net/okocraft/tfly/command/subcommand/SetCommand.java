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

public class SetCommand extends AbstractTFlyDataCommand {

    public SetCommand(@NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        super(localization, dataProvider);
    }

    @Override
    public @NotNull MiniMessageBase help() {
        return MessageKeys.COMMAND_SET_HELP;
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.set";
    }


    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var source = localization.findSource(LocaleUtils.getFrom(sender));

        if (args.length < 3) {
            help().source(source).send(sender);
            return;
        }

        Long seconds = parseNumber(sender, args[2], source);

        if (seconds == null) {
            return;
        }

        var data = getTFlyDataFromSenderOrArgument(sender, args[1]);

        if (data == null) {
            return;
        }

        long now = data.remainingTime(current -> Math.max(seconds, 0));

        MessageKeys.COMMAND_SET_SUCCESS
                .apply(args[1], now)
                .source(source)
                .send(sender);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        return args.length == 2 ? TabCompletionUtils.players(args[1]) : Collections.emptyList();
    }
}
