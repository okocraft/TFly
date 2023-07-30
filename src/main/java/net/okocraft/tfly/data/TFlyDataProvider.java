package net.okocraft.tfly.data;

import com.google.common.collect.Maps;
import net.okocraft.tfly.scheduler.CancellableTask;
import net.okocraft.tfly.scheduler.Scheduler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TFlyDataProvider {

    private final Map<UUID, TFlyData> dataMap = new ConcurrentHashMap<>();

    private TFlyDataStorage storage;
    private CancellableTask saveTask;

    public void init(@NotNull TFlyDataStorage storage) {
        dataMap.clear();

        if (storage instanceof TFlyDataFlatFileStorage fileStorage) {
            fileStorage.loadAll(dataMap);
        }

        this.storage = storage;
    }

    public @Nullable TFlyData getIfLoaded(@NotNull UUID uuid) {
        return dataMap.get(uuid);
    }

    public @NotNull TFlyData getOrLoad(@NotNull UUID uuid) {
        return dataMap.computeIfAbsent(uuid, $ -> new TFlyData());
    }

    public void close() {
        if (saveTask != null) {
            saveTask.cancel();
        }

        if (storage instanceof TFlyDataFlatFileStorage fileStorage) {
            fileStorage.saveAll(getRecordMap());
        }
    }

    public void scheduleSaveTask(@NotNull Scheduler scheduler) {
        if (storage instanceof TFlyDataFlatFileStorage fileStorage) {
            saveTask = scheduler.scheduleRepeatingAsyncTask(() -> fileStorage.saveAll(getRecordMap()), Duration.ofSeconds(60));
        }
    }

    @Contract(pure = true)
    private @NotNull Map<UUID, TFlyDataStorage.TFlyDataRecord> getRecordMap() {
        return Maps.transformValues(dataMap, TFlyData::asRecord);
    }
}
