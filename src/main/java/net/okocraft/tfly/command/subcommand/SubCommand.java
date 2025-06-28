package net.okocraft.tfly.command.subcommand;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface SubCommand {

    @NotNull Component help();

    @NotNull String permissionNode();

    void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args);

    default @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        return Collections.emptyList();
    }
}
