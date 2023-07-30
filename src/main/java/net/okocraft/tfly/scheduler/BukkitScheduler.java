package net.okocraft.tfly.scheduler;

import io.papermc.paper.util.Tick;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.function.Consumer;

class BukkitScheduler implements Scheduler {

    private final Plugin plugin;

    BukkitScheduler(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runPlayerTask(@NotNull Player target, @NotNull Consumer<Player> task) {
        Bukkit.getScheduler().runTask(plugin, () -> task.accept(target));
    }

    @Override
    public void runDelayedTask(@NotNull Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    @Override
    public void runAsyncTask(@NotNull Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    @Override
    public @NotNull CancellableTask scheduleRepeatingAsyncTask(@NotNull Runnable task, @NotNull Duration interval) {
        var ticks = Tick.tick().fromDuration(interval);
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, ticks, ticks)::cancel;
    }

    @Override
    public void close() {
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
