package net.okocraft.tfly.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.function.Consumer;

public interface Scheduler {

    static @NotNull Scheduler create(@NotNull Plugin plugin) {
        try {
            Bukkit.class.getDeclaredMethod("getAsyncScheduler");
            return new FoliaScheduler(plugin);
        } catch (NoSuchMethodException e) {
            return new BukkitScheduler(plugin);
        }
    }

    void runPlayerTask(@NotNull Player target, @NotNull Consumer<Player> task);

    void runDelayedTask(@NotNull Runnable task, long delay);

    void runAsyncTask(@NotNull Runnable task);

    @NotNull CancellableTask scheduleRepeatingAsyncTask(@NotNull Runnable task, @NotNull Duration interval);

    void close();

}
