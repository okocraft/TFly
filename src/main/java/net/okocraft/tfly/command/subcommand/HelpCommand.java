package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.minimessage.base.MiniMessageBase;
import com.github.siroshun09.messages.minimessage.localization.MiniMessageLocalization;
import net.kyori.adventure.text.Component;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.LocaleUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull MiniMessageBase help() {
        return MessageKeys.COMMAND_HELP_HELP;
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.help";
    }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var source = localization.findSource(LocaleUtils.getFrom(sender));
        var result = Component.text().append(MessageKeys.COMMAND_HELP_HEADER.create(source));

        subCommandStreamSupplier.get()
                .filter(cmd -> sender.hasPermission(cmd.permissionNode()))
                .map(SubCommand::help)
                .forEach(help -> result.append(Component.newline()).append(help.create(source)));

        sender.sendMessage(result);
    }
}
