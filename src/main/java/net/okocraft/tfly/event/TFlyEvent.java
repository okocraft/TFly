package net.okocraft.tfly.event;

import net.okocraft.tfly.data.TFlyData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class TFlyEvent extends Event {

    private final UUID playerUuid;
    private final TFlyData data;

    protected TFlyEvent(@NotNull UUID playerUuid, @NotNull TFlyData data, boolean async) {
        super(async);
        this.playerUuid = playerUuid;
        this.data = data;
    }

    public @NotNull UUID getPlayerUuid() {
        return playerUuid;
    }

    public @NotNull TFlyData getTFlyData() {
        return data;
    }
}
