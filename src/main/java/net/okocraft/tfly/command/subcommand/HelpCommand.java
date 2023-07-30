package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import net.kyori.adventure.text.Component;
import net.okocraft.tfly.message.HelpFactory;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.LocaleUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HelpCommand implements SubCommand {

    private final MiniMessageLocalization localization;
    private final Supplier<Stream<? extends SubCommand>> subCommandStreamSupplier;

    public HelpCommand(@NotNull MiniMessageLocalization localization,
                       @NotNull Supplier<Stream<? extends SubCommand>> subCommandStreamSupplier) {
        this.localization = localization;
        this.subCommandStreamSupplier = subCommandStreamSupplier;
    }

    @Override
    public @NotNull MiniMessageBuilder help(@NotNull Locale locale) {
        return HelpFactory.create(
                () -> localization.findSource(locale).builder(),
                MessageKeys.COMMAND_HELP_HELP,
                MessageKeys.COMMAND_HELP_COMMANDLINE
        );
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.help";
    }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var locale = LocaleUtils.getFrom(sender);
        var result = Component.text().append(localization.findSource(locale).builder().key(MessageKeys.COMMAND_HELP_HEADER).build());

        subCommandStreamSupplier.get()
                .filter(cmd -> sender.hasPermission(cmd.permissionNode()))
                .map(cmd -> cmd.help(locale))
                .forEach(help -> result.append(Component.newline()).append(help.build()));

        sender.sendMessage(result);
    }
}
