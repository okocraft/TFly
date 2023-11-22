package net.okocraft.tfly.message;

import com.github.siroshun09.messages.minimessage.arg.Arg1;
import com.github.siroshun09.messages.minimessage.arg.Arg2;
import com.github.siroshun09.messages.minimessage.arg.Arg3;
import com.github.siroshun09.messages.minimessage.base.MiniMessageBase;
import com.github.siroshun09.messages.minimessage.base.Placeholder;
import com.github.siroshun09.messages.minimessage.source.MiniMessageSource;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.siroshun09.messages.minimessage.arg.Arg1.arg1;
import static com.github.siroshun09.messages.minimessage.arg.Arg2.arg2;
import static com.github.siroshun09.messages.minimessage.arg.Arg3.arg3;
import static com.github.siroshun09.messages.minimessage.base.MiniMessageBase.withTagResolverBase;
import static com.github.siroshun09.messages.minimessage.base.Placeholder.component;
import static com.github.siroshun09.messages.minimessage.base.Placeholder.componentWithSource;
import static com.github.siroshun09.messages.minimessage.base.Placeholder.messageBase;

public final class MessageKeys {

    private static final Map<String, String> DEFAULT_MESSAGES = new LinkedHashMap<>();

    private static final Placeholder<String> PERMISSION_PLACEHOLDER = component("permission", Component::text);
    private static final Placeholder<String> ARGUMENT_PLACEHOLDER = component("argument", Component::text);
    private static final Placeholder<String> PLAYER_PLACEHOLDER = component("player", Component::text);
    private static final Placeholder<Long> SECONDS_PLACEHOLDER = component("seconds", Component::text);
    private static final Placeholder<Long> REMAINING_TIME_PLACEHOLDER = componentWithSource("remaining_time", MessageKeys::formatTime);
    private static final Placeholder<String> COMMANDLINE_PLACEHOLDER = messageBase("commandline", MiniMessageBase::messageKey);
    private static final Placeholder<String> HELP_PLACEHOLDER = messageBase("help", MiniMessageBase::messageKey);

    public static final Arg1<String> NO_PERMISSION = arg1(def("no-permission", "<gray>You don't have the permission: <aqua><permission>"), PERMISSION_PLACEHOLDER);

    public static final MiniMessageBase NOTIFICATION_MESSAGE_STARTED = withTagResolverBase(def("notification.message.started", "<gray>Flight mode started."));
    public static final MiniMessageBase NOTIFICATION_MESSAGE_STOPPED = withTagResolverBase(def("notification.message.stopped", "<gray>Flight mode stopped."));
    public static final Arg1<Long> NOTIFICATION_ACTIONBAR_REMAINING_TIME = arg1(def("notification.actionbar.remaining-time", "<yellow>Flight Mode: <aqua><remaining_time><yellow> left"), REMAINING_TIME_PLACEHOLDER);
    public static final Arg1<Long> NOTIFICATION_MESSAGE_REMAINING = arg1(def("notification.message.remaining-time", "<gray>Your flight mode has <aqua><remaining_time><gray> remaining."), REMAINING_TIME_PLACEHOLDER);

    public static final Arg1<String> COMMAND_GENERAL_PLAYER_NOT_FOUND = arg1(def("command.general.player-not-found", "<red>The player named <aqua><player><red> cannot be found."), ARGUMENT_PLACEHOLDER);
    public static final Arg1<String> COMMAND_GENERAL_INVALID_NUMBER = arg1(def("command.general.invalid-number", "<aqua><argument><red> is not valid number."), ARGUMENT_PLACEHOLDER);

    public static final MiniMessageBase COMMAND_HELP_HEADER = withTagResolverBase(def("command.help.header", "<dark_gray><st>=========================<reset><gold><b> TFly <reset><dark_gray><st>========================="));
    private static final String COMMAND_HELP_LINE_KEY = def("command.help.line", "<aqua><commandline><dark_gray>: <help>");
    public static final MiniMessageBase COMMAND_HELP_HELP = help(def("command.help.commandline", "/tfly help"), def("command.help.help", "<gray>Shows this help."));

    public static final MiniMessageBase COMMAND_RELOAD_SUCCESS = withTagResolverBase(def("command.reload.success", "<gray>config.yml and languages have been reloaded!"));
    public static final MiniMessageBase COMMAND_RELOAD_FAILURE = withTagResolverBase(def("command.reload.failure", "<red>Some errors are occurred while reloading. Please check your console."));
    public static final MiniMessageBase COMMAND_RELOAD_HELP = help(def("command.reload.commandline", "/tfly reload"), def("command.reload.help", "<gray>Reloads config.yml and languages."));

    public static final Arg1<Long> COMMAND_REMAINING_TIME_SELF = arg1(def("command.remaining.time.self", "<gray>You have <aqua><remaining_time><gray> left."), REMAINING_TIME_PLACEHOLDER);
    public static final Arg2<String, Long> COMMAND_REMAINING_TIME_OTHER = arg2(def("command.remaining.time.other", "<aqua><player><gray> has <aqua><remaining_time><gray> left."), PLAYER_PLACEHOLDER, REMAINING_TIME_PLACEHOLDER);
    public static final MiniMessageBase COMMAND_REMAINING_NO_TIME_SELF = withTagResolverBase(def("command.remaining.no-time.self", "<gray>You have no remaining time for flight mode."));
    public static final Arg1<String> COMMAND_REMAINING_NO_TIME_OTHER = arg1(def("command.remaining.no-time.other", "<aqua><player><gray> has no remaining time for flight mode."), PLAYER_PLACEHOLDER);
    public static final MiniMessageBase COMMAND_REMAINING_HELP = help(def("command.remaining.commandline", "/tfly remaining {player}"), def("command.remaining.help", "<gray>Shows own or specified player's remaining time."));

    public static final Arg3<Long, String, Long> COMMAND_ADD_SUCCESS = arg3(def("command.add.success", "<aqua><seconds><gray> second(s) added to <aqua><player><aqua><gray> (Now: <aqua><remaining_time><gray>)."), SECONDS_PLACEHOLDER, PLAYER_PLACEHOLDER, REMAINING_TIME_PLACEHOLDER);
    public static final MiniMessageBase COMMAND_ADD_HELP = help(def("command.add.commandline", "/tfly add <player> <seconds>"), def("command.add.help", "<gray>Adds flight time to the player."));

    public static final Arg2<String, Long> COMMAND_CHECK_FLYING = arg2(def("command.check.flying", "<aqua><player><aqua><gray> is flying by TFly. (Remaining: <aqua><remaining_time><gray>)."), PLAYER_PLACEHOLDER, REMAINING_TIME_PLACEHOLDER);
    public static final Arg1<String> COMMAND_CHECK_FLYING_BUT_NOT_TFLY = arg1(def("command.check.flying-but-not-tfly", "<aqua><player><aqua><gray> is flying, but not using TFly."), PLAYER_PLACEHOLDER);
    public static final Arg1<String> COMMAND_CHECK_NOT_FLYING = arg1(def("command.check.not-flying", "<aqua><player><aqua><gray> is not flying."), PLAYER_PLACEHOLDER);
    public static final MiniMessageBase COMMAND_CHECK_HELP = help(def("command.check.commandline", "/tfly check <player>"), def("command.check.help", "<gray>Checks if the player is flying by TFly."));

    public static final Arg2<String, Long> COMMAND_SET_SUCCESS = arg2(def("command.set.success", "<gray>Set <aqua><player><aqua><gray>'s flight time to <aqua><remaining_time><gray>."), PLAYER_PLACEHOLDER, REMAINING_TIME_PLACEHOLDER);
    public static final MiniMessageBase COMMAND_SET_HELP = help(def("command.set.commandline", "/tfly set <player> <seconds>"), def("command.set.help", "<gray>Sets the player's flight time."));

    public static final MiniMessageBase COMMAND_PAUSE_HELP = help(def("command.pause.commandline", "/tfly pause {player}"), def("command.pause.help", "<gray>Pauses flight mode."));
    public static final MiniMessageBase COMMAND_PAUSE_ALREADY_STOPPED_SELF = withTagResolverBase(def("command.pause.already-stopped.self", "<red>Flight mode is already stopped."));
    public static final Arg1<String> COMMAND_PAUSE_ALREADY_STOPPED_OTHER = arg1(def("command.pause.already-stopped.other", "<aqua><player><red>'s flight mode is already stopped."), PLAYER_PLACEHOLDER);

    public static final MiniMessageBase COMMAND_RESUME_HELP = help(def("command.resume.commandline", "/tfly resume {player}"), def("command.resume.help", "<gray>Resumes flight mode."));
    public static final MiniMessageBase COMMAND_RESUME_ALREADY_RUNNING_SELF = withTagResolverBase(def("command.resume.already-running.self", "<red>Flight mode is already running."));
    public static final Arg1<String> COMMAND_RESUME_ALREADY_RUNNING_OTHER = arg1(def("command.resume.already-running.other", "<aqua><player><red>'s flight mode is already running."), PLAYER_PLACEHOLDER);
    public static final MiniMessageBase COMMAND_RESUME_NO_REMAINING_TIME_SELF = withTagResolverBase(def("command.resume.no-remaining-time.self", "<red>You don't have remaining time for flight mode."));
    public static final Arg1<String> COMMAND_RESUME_NO_REMAINING_TIME_OTHER = arg1(def("command.resume.no-remaining-time.other", "<aqua><player><red> does not have remaining time for flight mode."), PLAYER_PLACEHOLDER);
    public static final MiniMessageBase COMMAND_RESUME_CANNOT_FLY_SELF = withTagResolverBase(def("command.resume.cannot-fly.self", "<red>You can't start flight mode there."));
    public static final Arg1<String> COMMAND_RESUME_CANNOT_FLY_OTHER = arg1(def("command.resume.cannot-fly.other", "<red>Flight mode can't be started at <aqua><player><red>'s location."), PLAYER_PLACEHOLDER);

    public static final MiniMessageBase COMMAND_TOGGLE_HELP = help(def("command.toggle.commandline", "/tfly toggle {player}"), def("command.toggle.help", "<gray>Toggles flight mode."));
    public static final MiniMessageBase COMMAND_TOGGLE_CANNOT_TOGGLE_NOW = withTagResolverBase(def("command.toggle.cannot-toggle-now", "<red>You can't toggle flight mode now. Please try again."));

    public static final MiniMessageBase REMAINING_TIME_NONE = withTagResolverBase(def("remaining-time.none", "None"));
    private static final Arg1<Long> REMAINING_TIME_HOUR = arg1(def("remaining-time.hours.singular", "<hours> hour"), Placeholder.component("hours", Component::text));
    private static final Arg1<Long> REMAINING_TIME_HOURS = arg1(def("remaining-time.hours.plural", "<hours> hours"), Placeholder.component("hours", Component::text));
    private static final Arg1<Long> REMAINING_TIME_MINUTE = arg1(def("remaining-time.minutes.singular", "<minutes> minute"), Placeholder.component("minutes", Component::text));
    private static final Arg1<Long> REMAINING_TIME_MINUTES = arg1(def("remaining-time.minutes.plural", "<minutes> minutes"), Placeholder.component("minutes", Component::text));
    private static final Arg1<Long> REMAINING_TIME_SECOND = arg1(def("remaining-time.seconds.singular", "<seconds> second"), Placeholder.component("seconds", Component::text));
    private static final Arg1<Long> REMAINING_TIME_SECONDS = arg1(def("remaining-time.seconds.plural", "<seconds> seconds"), Placeholder.component("seconds", Component::text));

    @Contract("_, _ -> param1")
    private static @NotNull String def(@NotNull String key, @NotNull String msg) {
        DEFAULT_MESSAGES.put(key, msg);
        return key;
    }

    private static @NotNull MiniMessageBase help(String commandlineKey, String helpKey) {
        return MiniMessageBase.withTagResolverBase(COMMAND_HELP_LINE_KEY, COMMANDLINE_PLACEHOLDER.apply(commandlineKey), HELP_PLACEHOLDER.apply(helpKey));
    }

    @Contract(pure = true)
    public static @NotNull @UnmodifiableView Map<String, String> defaultMessages() {
        return Collections.unmodifiableMap(DEFAULT_MESSAGES);
    }

    public static @NotNull Component formatTime(long sec, @NotNull MiniMessageSource source) {
        if (sec < 1) {
            return MessageKeys.REMAINING_TIME_NONE.create(source);
        }

        var result = Component.text();

        // For someone: if you need a larger unit of time, e.g. days, see https://github.com/LuckPerms/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/util/DurationFormatter.java
        long hours = sec / 3600;
        long minutes = sec % 3600 / 60;
        long seconds = sec % 60;

        if (hours != 0) {
            var hourArg = hours == 1 ? REMAINING_TIME_HOUR : REMAINING_TIME_HOURS;
            result.append(hourArg.apply(hours).create(source));
        }

        if (minutes != 0) {
            if (hours != 0) result.appendSpace();
            var minuteArg = hours == 1 ? REMAINING_TIME_MINUTE : REMAINING_TIME_MINUTES;
            result.append(minuteArg.apply(minutes).create(source));
        }

        if (seconds != 0) {
            if (hours != 0 || minutes != 0) result.appendSpace();
            var secondArg = hours == 1 ? REMAINING_TIME_SECOND : REMAINING_TIME_SECONDS;
            result.append(secondArg.apply(seconds).create(source));
        }

        return result.build();
    }

    private MessageKeys() {
        throw new UnsupportedOperationException();
    }
}
