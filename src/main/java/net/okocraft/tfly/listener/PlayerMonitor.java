package net.okocraft.tfly.listener;

import net.okocraft.tfly.checker.LocationChecker;
import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.player.TFlyController;
import net.okocraft.tfly.scheduler.Scheduler;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerMonitor implements Listener {

    private final LocationChecker locationChecker;
    private final Scheduler scheduler;
    private final TFlyDataProvider dataProvider;
    private final TFlyController controller;

    public PlayerMonitor(@NotNull Scheduler scheduler,
                         @NotNull TFlyDataProvider dataProvider,
                         @NotNull TFlyController controller,
                         @NotNull LocationChecker locationChecker) {
        this.scheduler = scheduler;
        this.dataProvider = dataProvider;
        this.controller = controller;
        this.locationChecker = locationChecker;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpawn(@NotNull PlayerSpawnLocationEvent event) {
        scheduler.runPlayerTask(event.getPlayer(), this::startIfNotPaused);
    }

    private void startIfNotPaused(@NotNull Player player) {
        if (!player.isOnline()) {
            return;
        }

        var gameMode = player.getGameMode();

        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) {
            return;
        }

        var uuid = player.getUniqueId();
        var data = dataProvider.getIfLoaded(uuid);

        if (data == null) {
            scheduler.runAsyncTask(() -> startIfNotPaused(player, dataProvider.getOrLoad(uuid)));
        } else {
            startIfNotPaused(player, data);
        }
    }

    private void startIfNotPaused(@NotNull Player player, @NotNull TFlyData data) {
        if (data.paused() || data.remainingTime() < 1 || !locationChecker.test(player)) {
            return;
        }

        if (data.statusIf(status -> status == TFlyData.Status.STOPPED, TFlyData.Status.STARTING)) {
            controller.start(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(@NotNull PlayerChangedWorldEvent event) {
        checkLocation(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(@NotNull PlayerRespawnEvent event) {
        checkLocation(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGameModeChange(@NotNull PlayerGameModeChangeEvent event) {
        var gameMode = event.getNewGameMode();

        if (gameMode != GameMode.CREATIVE && gameMode != GameMode.SPECTATOR) {
            return;
        }

        var player = event.getPlayer();
        var data = dataProvider.getIfLoaded(player.getUniqueId());

        if (data != null && data.statusIf(status -> status == TFlyData.Status.STARTING || status == TFlyData.Status.RUNNING, TFlyData.Status.STOPPING)) {
            controller.stop(player, false);
        }
    }

    private void checkLocation(@NotNull Player player) {
        var data = dataProvider.getIfLoaded(player.getUniqueId());

        if (data != null && data.status() != TFlyData.Status.STOPPED && !locationChecker.test(player)) {
            data.status(TFlyData.Status.STOPPING);
            controller.stop(player, true);
        }
    }
}
