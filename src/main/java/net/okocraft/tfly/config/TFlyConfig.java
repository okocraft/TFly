package net.okocraft.tfly.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

public class TFlyConfig {

    private Set<Long> notificationTime = Collections.emptySet();
    private boolean enableActionbarNotification = true;
    private @NotNull Predicate<Player> worldChecker = player -> true;

    public @NotNull Set<Long> notificationTime() {
        return notificationTime;
    }

    public boolean enableActionbarNotification() {
        return enableActionbarNotification;
    }

    public @NotNull Predicate<Player> getWorldChecker() {
        return worldChecker;
    }

    public void loadFrom(@NotNull Configuration source) {
        notificationTime = Set.copyOf(source.getLongList("notification.time-to-notify"));
        enableActionbarNotification = source.getBoolean("notification.enable-action-bar", true);

        var enabledWorlds = Set.copyOf(source.getStringList("enabled-worlds"));
        worldChecker = player -> enabledWorlds.contains(player.getWorld().getName()) || enabledWorlds.contains(player.getWorld().getKey().asString());
    }
}
