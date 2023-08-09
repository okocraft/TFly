package net.okocraft.tfly.player;

import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.event.TFlyProgressEvent;
import net.okocraft.tfly.event.TFlyStartedEvent;
import net.okocraft.tfly.event.TFlyStoppedEvent;
import net.okocraft.tfly.scheduler.CancellableTask;
import net.okocraft.tfly.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TFlyController {

    private static final Duration ONE_SECOND = Duration.ofSeconds(1);

    private final Map<UUID, CancellableTask> taskMap = new ConcurrentHashMap<>();

    private final Scheduler scheduler;
    private final TFlyDataProvider dataProvider;

    public TFlyController(@NotNull Scheduler scheduler, @NotNull TFlyDataProvider dataProvider) {
        this.scheduler = scheduler;
        this.dataProvider = dataProvider;
    }

    public void start(@NotNull Player player) {
        scheduler.runPlayerTask(player, this::start0);
    }

    private void start0(@NotNull Player player) {
        var uuid = player.getUniqueId();
        var data = dataProvider.getIfLoaded(uuid);

        if (data == null || data.status(TFlyData.Status.RUNNING) != TFlyData.Status.STARTING) { // status already changed to RUNNING by another operation
            return;
        }

        data.stoppedOnQuit(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        var previous = taskMap.put(uuid, scheduler.scheduleRepeatingAsyncTask(() -> tick(uuid, data), ONE_SECOND));

        if (previous != null) {
            previous.cancel();
        }

        new TFlyStartedEvent(uuid, data).callEvent();
    }

    public void stop(@NotNull Player player, boolean disableFlight) {
        scheduler.runPlayerTask(player, p -> stop0(p, disableFlight));
    }

    private void stop0(@NotNull Player player, boolean disableFlight) {
        var uuid = player.getUniqueId();
        var data = dataProvider.getIfLoaded(uuid);

        if (data != null && data.status(TFlyData.Status.STOPPED) == TFlyData.Status.STOPPED) {
            return;
        }

        if (disableFlight) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }

        if (data != null) {
            data.stoppedOnQuit(true);
        }

        var task = taskMap.remove(uuid);

        if (task != null) {
            task.cancel();
        }

        if (data != null) {
            new TFlyStoppedEvent(uuid, data).callEvent();
        }
    }

    public void stopAll(@NotNull Collection<? extends Player> onlinePlayers) {
        for (var player : onlinePlayers) {
            var data = dataProvider.getIfLoaded(player.getUniqueId());

            if (data == null) {
                continue;
            }

            data.stoppedOnQuit(true);

            var status = data.status();

            if (status == TFlyData.Status.RUNNING || status == TFlyData.Status.STOPPING) {
                player.setFlying(false);
                player.setAllowFlight(false);
                data.status(TFlyData.Status.STOPPED);
            }
        }

        taskMap.values().forEach(CancellableTask::cancel);
        taskMap.clear();
    }

    private void tick(@NotNull UUID uuid, @NotNull TFlyData data) {
        if (data.status() == TFlyData.Status.RUNNING) {
            var player = Bukkit.getPlayer(uuid);

            if (player == null || !player.getAllowFlight()) {
                data.status(TFlyData.Status.STOPPED);
                taskMap.remove(uuid).cancel();
                new TFlyStoppedEvent(uuid, data).callEvent();
                return;
            }

            var remaining = data.decrementTime();
            if (0 < remaining) {
                new TFlyProgressEvent(uuid, data, remaining).callEvent();
            } else {
                data.status(TFlyData.Status.STOPPING);
                stop(player, true);
            }
        }
    }
}
