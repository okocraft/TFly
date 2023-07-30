package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import net.okocraft.tfly.message.HelpFactory;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.LocaleUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.BooleanSupplier;

public class ReloadCommand implements SubCommand {

    private final MiniMessageLocalization localization;
    private final BooleanSupplier reloading;

    public ReloadCommand(@NotNull MiniMessageLocalization localization, @NotNull BooleanSupplier reloading) {
        this.localization = localization;
        this.reloading = reloading;
    }

    @Override
    public @NotNull MiniMessageBuilder help(@NotNull Locale locale) {
        return HelpFactory.create(
                () -> localization.findSource(locale).builder(),
                MessageKeys.COMMAND_RELOAD_HELP,
                MessageKeys.COMMAND_RELOAD_COMMANDLINE
        );
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.reload";
    }

    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        boolean success;

        synchronized (this) {
            success = reloading.getAsBoolean();
        }

        var messageKey = success ? MessageKeys.COMMAND_RELOAD_SUCCESS : MessageKeys.COMMAND_RELOAD_FAILURE;
        sender.sendMessage(localization.findSource(LocaleUtils.getFrom(sender)).builder().key(messageKey).build());
    }
}
