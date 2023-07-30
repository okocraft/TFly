package net.okocraft.tfly.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class LocaleUtils {

    public static @NotNull Locale getFrom(@Nullable CommandSender sender) {
        return sender instanceof Player player ? player.locale() : Locale.getDefault();
    }

    private LocaleUtils() {
        throw new UnsupportedOperationException();
    }
}
