package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.minimessage.base.MiniMessageBase;
import com.github.siroshun09.messages.minimessage.localization.MiniMessageLocalization;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.util.LocaleUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class ReloadCommand implements SubCommand {

    private final MiniMessageLocalization localization;
    private final BooleanSupplier reloading;

    public ReloadCommand(@NotNull MiniMessageLocalization localization, @NotNull BooleanSupplier reloading) {
        this.localization = localization;
        this.reloading = reloading;
    }

    @Override
    public @NotNull MiniMessageBase help() {
        return MessageKeys.COMMAND_RELOAD_HELP;
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

        var messageBase = success ? MessageKeys.COMMAND_RELOAD_SUCCESS : MessageKeys.COMMAND_RELOAD_FAILURE;
        messageBase.source(localization.findSource(LocaleUtils.getFrom(sender))).send(sender);
    }
}
