package net.okocraft.tfly.util;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class LocaleUtils {

    public static @NotNull Locale getFrom(@Nullable Object obj) {
        if (obj instanceof Locale locale) {
            return locale;
        } else if (obj instanceof Player player) {
            return player.locale();
        } else {
            return Locale.getDefault();
        }
    }

    private LocaleUtils() {
        throw new UnsupportedOperationException();
    }
}
