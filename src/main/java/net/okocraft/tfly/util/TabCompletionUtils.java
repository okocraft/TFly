package net.okocraft.tfly.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public final class TabCompletionUtils {

    public static @NotNull List<String> players(@NotNull String arg) {
        var filter = arg.toLowerCase(Locale.ENGLISH);
        return Bukkit.getOnlinePlayers().stream()
                .map(CommandSender::getName)
                .filter(name -> name.toLowerCase(Locale.ENGLISH).startsWith(filter))
                .toList();
    }

    private TabCompletionUtils() {
        throw new UnsupportedOperationException();
    }
}
