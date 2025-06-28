package net.okocraft.tfly.listener;

import net.okocraft.tfly.config.TFlyConfig;
import net.okocraft.tfly.event.TFlyProgressEvent;
import net.okocraft.tfly.event.TFlyStartedEvent;
import net.okocraft.tfly.event.TFlyStoppedEvent;
import net.okocraft.tfly.message.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class TFlyEventListener implements Listener {

    private final TFlyConfig config;

    public TFlyEventListener(@NotNull TFlyConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onProgress(@NotNull TFlyProgressEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayerUuid());

        if (player == null) {
            return;
        }

        long remaining = event.getCurrentRemainingTime();
        if (config.enableActionbarNotification()) {
            player.sendActionBar(MessageKeys.NOTIFICATION_ACTIONBAR_REMAINING_TIME.apply(remaining));
        }

        if (config.notificationTime().contains(remaining)) {
            player.sendMessage(MessageKeys.NOTIFICATION_MESSAGE_REMAINING.apply(remaining));
        }
    }

    @EventHandler
    public void onStart(@NotNull TFlyStartedEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayerUuid());

        if (player != null) {
            player.sendMessage(MessageKeys.NOTIFICATION_MESSAGE_STARTED);
        }
    }

    @EventHandler
    public void onStop(@NotNull TFlyStoppedEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayerUuid());

        if (player != null) {
            player.sendMessage(MessageKeys.NOTIFICATION_MESSAGE_STOPPED);
        }
    }
}
