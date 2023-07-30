package net.okocraft.tfly.event;

import net.okocraft.tfly.data.TFlyData;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TFlyStartedEvent extends TFlyEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public TFlyStartedEvent(@NotNull UUID playerUuid, @NotNull TFlyData data) {
        super(playerUuid, data, !Bukkit.isPrimaryThread());
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
