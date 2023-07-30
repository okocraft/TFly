package net.okocraft.tfly.data;

import net.okocraft.tfly.TFlyPlugin;
import net.okocraft.tfly.data.storage.DatFileStorage;
import org.jetbrains.annotations.NotNull;

public sealed interface TFlyDataStorage permits TFlyDataFlatFileStorage {

    static @NotNull TFlyDataStorage create(@NotNull TFlyPlugin plugin) {
        return new DatFileStorage(plugin.getDataFolder().toPath().resolve("players.dat"));
    }

    record TFlyDataRecord(long remainingTime, boolean paused) {
    }
}
