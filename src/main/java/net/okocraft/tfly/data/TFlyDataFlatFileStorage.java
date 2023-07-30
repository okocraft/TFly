package net.okocraft.tfly.data;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public non-sealed interface TFlyDataFlatFileStorage extends TFlyDataStorage {

    void loadAll(@NotNull Map<UUID, TFlyData> dataMap);

    void saveAll(@NotNull Map<UUID, TFlyDataStorage.TFlyDataRecord> dataMap);

}
