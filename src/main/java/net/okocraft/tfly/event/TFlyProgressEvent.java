package net.okocraft.tfly.event;

import net.okocraft.tfly.data.TFlyData;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TFlyProgressEvent extends TFlyEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final long currentRemainingTime;

    public TFlyProgressEvent(@NotNull UUID playerUuid, @NotNull TFlyData data, long currentRemainingTime) {
        super(playerUuid, data, true);
        this.currentRemainingTime = currentRemainingTime;
    }

    public long getCurrentRemainingTime() {
        return currentRemainingTime;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
