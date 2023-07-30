package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public interface SubCommand {

    @NotNull MiniMessageBuilder help(@NotNull Locale locale);

    @NotNull String permissionNode();

    void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args);

    default @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        return Collections.emptyList();
    }
}
