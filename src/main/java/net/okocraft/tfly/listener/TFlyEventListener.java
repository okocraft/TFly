package net.okocraft.tfly.listener;

import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import net.okocraft.tfly.config.TFlyConfig;
import net.okocraft.tfly.event.TFlyProgressEvent;
import net.okocraft.tfly.event.TFlyStartedEvent;
import net.okocraft.tfly.event.TFlyStoppedEvent;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.message.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class TFlyEventListener implements Listener {

    private final TFlyConfig config;
    private final MiniMessageLocalization localization;

    public TFlyEventListener(@NotNull TFlyConfig config, @NotNull MiniMessageLocalization localization) {
        this.config = config;
        this.localization = localization;
    }

    @EventHandler
    public void onProgress(@NotNull TFlyProgressEvent event) {
        var player = Bukkit.getPlayer(event.getPlayerUuid());

        if (player == null) {
            return;
        }

        long remaining = event.getCurrentRemainingTime();
        var source = localization.findSource(player.locale());
        var placeholder = Placeholders.remainingTime(remaining, source);

        if (config.enableActionbarNotification()) {
            player.sendActionBar(
                    source.builder()
                            .key(MessageKeys.NOTIFICATION_ACTIONBAR_REMAINING_TIME)
                            .tagResolver(placeholder)
                            .build()
            );
        }

        if (config.notificationTime().contains(remaining)) {
            source.builder()
                    .key(MessageKeys.NOTIFICATION_MESSAGE_REMAINING)
                    .tagResolver(placeholder)
                    .send(player);
        }
    }

    @EventHandler
    public void onStart(@NotNull TFlyStartedEvent event) {
        var player = Bukkit.getPlayer(event.getPlayerUuid());

        if (player != null) {
            localization.findSource(player.locale())
                    .builder()
                    .key(MessageKeys.NOTIFICATION_MESSAGE_STARTED)
                    .send(player);
        }
    }

    @EventHandler
    public void onStop(@NotNull TFlyStoppedEvent event) {
        var player = Bukkit.getPlayer(event.getPlayerUuid());

        if (player != null) {
            localization.findSource(player.locale())
                    .builder()
                    .key(MessageKeys.NOTIFICATION_MESSAGE_STOPPED)
                    .send(player);
        }
    }
}
