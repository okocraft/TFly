package net.okocraft.tfly.command.subcommand;

import net.kyori.adventure.text.Component;
import net.okocraft.tfly.message.MessageKeys;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

public class ReloadCommand implements SubCommand {

    private final BooleanSupplier reloading;

    public ReloadCommand(@NotNull BooleanSupplier reloading) {
        this.reloading = reloading;
    }

    @Override
    public @NotNull Component help() {
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

        sender.sendMessage(success ? MessageKeys.COMMAND_RELOAD_SUCCESS : MessageKeys.COMMAND_RELOAD_FAILURE);
    }
}
