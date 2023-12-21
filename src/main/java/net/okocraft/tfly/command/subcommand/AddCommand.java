package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.minimessage.base.MiniMessageBase;
import com.github.siroshun09.messages.minimessage.localization.MiniMessageLocalization;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.TabCompletionUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class AddCommand extends AbstractTFlyDataCommand {

    public AddCommand(@NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        super(localization, dataProvider);
    }

    @Override
    public @NotNull MiniMessageBase help() {
        return MessageKeys.COMMAND_ADD_HELP;
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.add";
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var source = localization.findSource(sender);

        if (args.length < 3) {
            help().source(source).send(sender);
            return;
        }

        Long adding = parseNumber(sender, args[2], source);

        if (adding == null) {
            return;
        }

        var data = getTFlyDataFromSenderOrArgument(sender, args[1]);

        if (data == null) {
            return;
        }

        long now = data.remainingTime(current -> current + Math.max(adding, 0));
        MessageKeys.COMMAND_ADD_SUCCESS.apply(Math.max(adding, 0), args[1], now).source(source).send(sender);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        return args.length == 2 ? TabCompletionUtils.players(args[1]) : Collections.emptyList();
    }
}
