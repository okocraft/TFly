package net.okocraft.tfly.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

class FoliaScheduler implements Scheduler {

    private final Plugin plugin;

    FoliaScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runPlayerTask(@NotNull Player target, @NotNull Consumer<Player> task) {
        target.getScheduler().run(plugin, $ -> task.accept(target), null);
    }

    @Override
    public void runDelayedTask(@NotNull Runnable task, long delay) {
        Bukkit.getGlobalRegionScheduler().runDelayed(plugin, $ -> task.run(), delay);
    }

    @Override
    public void runAsyncTask(@NotNull Runnable task) {
        Bukkit.getAsyncScheduler().runNow(plugin, $ -> task.run());
    }

    @Override
    public @NotNull CancellableTask scheduleRepeatingAsyncTask(@NotNull Runnable task, @NotNull Duration interval) {
        return Bukkit.getAsyncScheduler().runAtFixedRate(plugin, $ -> task.run(), interval.toMillis(), interval.toMillis(), TimeUnit.MILLISECONDS)::cancel;
    }

    @Override
    public void close() {
        Bukkit.getAsyncScheduler().cancelTasks(plugin);
    }
}
