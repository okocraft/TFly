package net.okocraft.tfly.checker;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LocationChecker implements Predicate<Player> {

    private final List<Predicate<Player>> predicates = new ArrayList<>();
    private @Nullable Predicate<Player> cache;

    @Override
    public boolean test(Player player) {
        return cache == null || cache.test(player);
    }

    public void addChecker(@NotNull Predicate<Player> locationChecker) {
        synchronized (predicates) {
            this.predicates.add(locationChecker);
            buildCache();
        }
    }

    public void removeChecker(@NotNull Predicate<Player> locationPredicate) {
        synchronized (predicates) {
            this.predicates.remove(locationPredicate);
            buildCache();
        }
    }

    private void buildCache() {
        Predicate<Player> result = null;
        for (var predicate : predicates) {
            result = result != null ? result.and(predicate) : predicate;
        }
        this.cache = result;
    }
}
