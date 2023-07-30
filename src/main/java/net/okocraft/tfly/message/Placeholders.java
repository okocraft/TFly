package net.okocraft.tfly.message;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.util.MessageBuilderFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.TagPattern;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class Placeholders {

    public static @NotNull TagResolver argument(@NotNull String argument) {
        return Placeholder.component("argument", Component.text(argument));
    }

    public static @NotNull TagResolver player(@NotNull String playerName) {
        return Placeholder.component("player", Component.text(playerName));
    }

    public static @NotNull TagResolver seconds(long seconds) {
        return Placeholder.component("seconds", Component.text(seconds));
    }

    public static @NotNull TagResolver permission(@NotNull String node) {
        return Placeholder.component("permission", Component.text(node));
    }

    public static @NotNull TagResolver remainingTime(long sec, @NotNull MessageBuilderFactory<MiniMessageBuilder> builderFactory) {
        return Placeholder.component("remaining_time", remainingTimeComponent(sec, builderFactory));
    }

    public static @NotNull Component remainingTimeComponent(long sec, @NotNull MessageBuilderFactory<MiniMessageBuilder> builderFactory) {
        if (sec < 1) {
            return builderFactory.create().key("remaining-time.none").build();
        }

        var duration = Duration.ofSeconds(sec);
        var result = Component.text();

        // For someone: if you need a larger unit of time, e.g. days, see https://github.com/LuckPerms/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/util/DurationFormatter.java
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        if (hours != 0) {
            result.append(formatTime(hours, "hours", builderFactory));
        }

        if (minutes != 0) {
            if (hours != 0) {
                result.appendSpace();
            }
            result.append(formatTime(minutes, "minutes", builderFactory));
        }

        if (seconds != 0) {
            if (hours != 0 || minutes != 0) {
                result.appendSpace();
            }
            result.append(formatTime(seconds, "seconds", builderFactory));
        }

        return result.build();
    }

    private static @NotNull Component formatTime(long time, @TagPattern @NotNull String unitName, @NotNull MessageBuilderFactory<MiniMessageBuilder> factory) {
        boolean singular = time == 1;
        return factory.create()
                .key("remaining-time." + unitName + "." + (singular ? "singular" : "plural"))
                .tagResolver(Placeholder.component(unitName, Component.text(time)))
                .build();
    }

    private Placeholders() {
        throw new UnsupportedOperationException();
    }
}
