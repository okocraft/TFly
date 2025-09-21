package net.okocraft.tfly.message;

import dev.siroshun.mcmsgdef.DefaultMessageDefiner;
import dev.siroshun.mcmsgdef.MessageKey;
import dev.siroshun.mcmsgdef.Placeholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Map;

public final class MessageKeys {

    private static final DefaultMessageDefiner DEFINER = DefaultMessageDefiner.create();

    private static final Placeholder<String> PERMISSION_PLACEHOLDER = node -> Argument.string("permission", node);
    private static final Placeholder<String> ARGUMENT_PLACEHOLDER = arg -> Argument.string("argument", arg);
    private static final Placeholder<String> PLAYER_PLACEHOLDER = player -> Argument.string("player", player);
    private static final Placeholder<Long> SECONDS_PLACEHOLDER = seconds -> Argument.numeric("seconds", seconds);
    private static final Placeholder<Long> REMAINING_TIME_PLACEHOLDER = seconds -> Argument.component("remaining_time", formatTime(seconds));
    private static final Placeholder<MessageKey> COMMANDLINE_PLACEHOLDER = key -> Argument.component("commandline", key.asComponent());
    private static final Placeholder<MessageKey> HELP_PLACEHOLDER = key -> Argument.component("help", key.asComponent());

    public static final MessageKey.Arg1<String> NO_PERMISSION = DEFINER.define("no-permission", "<gray>You don't have the permission: <aqua><permission>").with(PERMISSION_PLACEHOLDER);

    public static final MessageKey NOTIFICATION_MESSAGE_STARTED = DEFINER.define("notification.message.started", "<gray>Flight mode started.");
    public static final MessageKey NOTIFICATION_MESSAGE_STOPPED = DEFINER.define("notification.message.stopped", "<gray>Flight mode stopped.");
    public static final MessageKey.Arg1<Long> NOTIFICATION_ACTIONBAR_REMAINING_TIME = DEFINER.define("notification.actionbar.remaining-time", "<yellow>Flight Mode: <aqua><remaining_time><yellow> left").with(REMAINING_TIME_PLACEHOLDER);
    public static final MessageKey.Arg1<Long> NOTIFICATION_MESSAGE_REMAINING = DEFINER.define("notification.message.remaining-time", "<gray>Your flight mode has <aqua><remaining_time><gray> remaining.").with(REMAINING_TIME_PLACEHOLDER);

    public static final MessageKey.Arg1<String> COMMAND_GENERAL_PLAYER_NOT_FOUND = DEFINER.define("command.general.player-not-found", "<red>The player named <aqua><player><red> cannot be found.").with(ARGUMENT_PLACEHOLDER);
    public static final MessageKey.Arg1<String> COMMAND_GENERAL_INVALID_NUMBER = DEFINER.define("command.general.invalid-number", "<aqua><argument><red> is not valid number.").with(ARGUMENT_PLACEHOLDER);

    public static final MessageKey COMMAND_HELP_HEADER = DEFINER.define("command.help.header", "<dark_gray><st>=========================<reset><gold><b> TFly <reset><dark_gray><st>=========================");
    private static final MessageKey.Arg2<MessageKey, MessageKey> COMMAND_HELP_LINE_KEY = DEFINER.define("command.help.line", "<aqua><commandline><dark_gray>: <help>").with(COMMANDLINE_PLACEHOLDER, HELP_PLACEHOLDER);
    public static final Component COMMAND_HELP_HELP = help(DEFINER.define("command.help.commandline", "/tfly help"), DEFINER.define("command.help.help", "<gray>Shows this help."));

    public static final MessageKey COMMAND_RELOAD_SUCCESS = DEFINER.define("command.reload.success", "<gray>config.yml and languages have been reloaded!");
    public static final MessageKey COMMAND_RELOAD_FAILURE = DEFINER.define("command.reload.failure", "<red>Some errors are occurred while reloading. Please check your console.");
    public static final Component COMMAND_RELOAD_HELP = help(DEFINER.define("command.reload.commandline", "/tfly reload"), DEFINER.define("command.reload.help", "<gray>Reloads config.yml and languages."));

    public static final MessageKey.Arg1<Long> COMMAND_REMAINING_TIME_SELF = DEFINER.define("command.remaining.time.self", "<gray>You have <aqua><remaining_time><gray> left.").with(REMAINING_TIME_PLACEHOLDER);
    public static final MessageKey.Arg2<String, Long> COMMAND_REMAINING_TIME_OTHER = DEFINER.define("command.remaining.time.other", "<aqua><player><gray> has <aqua><remaining_time><gray> left.").with(PLAYER_PLACEHOLDER, REMAINING_TIME_PLACEHOLDER);
    public static final MessageKey COMMAND_REMAINING_NO_TIME_SELF = DEFINER.define("command.remaining.no-time.self", "<gray>You have no remaining time for flight mode.");
    public static final MessageKey.Arg1<String> COMMAND_REMAINING_NO_TIME_OTHER = DEFINER.define("command.remaining.no-time.other", "<aqua><player><gray> has no remaining time for flight mode.").with(PLAYER_PLACEHOLDER);
    public static final Component COMMAND_REMAINING_HELP = help(DEFINER.define("command.remaining.commandline", "/tfly remaining {player}"), DEFINER.define("command.remaining.help", "<gray>Shows own or specified player's remaining time."));

    public static final MessageKey.Arg3<Long, String, Long> COMMAND_ADD_SUCCESS = DEFINER.define("command.add.success", "<aqua><seconds><gray> second(s) added to <aqua><player><aqua><gray> (Now: <aqua><remaining_time><gray>).").with(SECONDS_PLACEHOLDER, PLAYER_PLACEHOLDER, REMAINING_TIME_PLACEHOLDER);
    public static final Component COMMAND_ADD_HELP = help(DEFINER.define("command.add.commandline", "/tfly add <player> <seconds>"), DEFINER.define("command.add.help", "<gray>Adds flight time to the player."));

    public static final MessageKey.Arg2<String, Long> COMMAND_CHECK_FLYING = DEFINER.define("command.check.flying", "<aqua><player><aqua><gray> is flying by TFly. (Remaining: <aqua><remaining_time><gray>).").with(PLAYER_PLACEHOLDER, REMAINING_TIME_PLACEHOLDER);
    public static final MessageKey.Arg1<String> COMMAND_CHECK_FLYING_BUT_NOT_TFLY = DEFINER.define("command.check.flying-but-not-tfly", "<aqua><player><aqua><gray> is flying, but not using TFly.").with(PLAYER_PLACEHOLDER);
    public static final MessageKey.Arg1<String> COMMAND_CHECK_NOT_FLYING = DEFINER.define("command.check.not-flying", "<aqua><player><aqua><gray> is not flying.").with(PLAYER_PLACEHOLDER);
    public static final Component COMMAND_CHECK_HELP = help(DEFINER.define("command.check.commandline", "/tfly check <player>"), DEFINER.define("command.check.help", "<gray>Checks if the player is flying by TFly."));

    public static final MessageKey.Arg2<String, Long> COMMAND_SET_SUCCESS = DEFINER.define("command.set.success", "<gray>Set <aqua><player><aqua><gray>'s flight time to <aqua><remaining_time><gray>.").with(PLAYER_PLACEHOLDER, REMAINING_TIME_PLACEHOLDER);
    public static final Component COMMAND_SET_HELP = help(DEFINER.define("command.set.commandline", "/tfly set <player> <seconds>"), DEFINER.define("command.set.help", "<gray>Sets the player's flight time."));

    public static final Component COMMAND_PAUSE_HELP = help(DEFINER.define("command.pause.commandline", "/tfly pause {player}"), DEFINER.define("command.pause.help", "<gray>Pauses flight mode."));
    public static final MessageKey COMMAND_PAUSE_ALREADY_STOPPED_SELF = DEFINER.define("command.pause.already-stopped.self", "<red>Flight mode is already stopped.");
    public static final MessageKey.Arg1<String> COMMAND_PAUSE_ALREADY_STOPPED_OTHER = DEFINER.define("command.pause.already-stopped.other", "<aqua><player><red>'s flight mode is already stopped.").with(PLAYER_PLACEHOLDER);

    public static final Component COMMAND_RESUME_HELP = help(DEFINER.define("command.resume.commandline", "/tfly resume {player}"), DEFINER.define("command.resume.help", "<gray>Resumes flight mode."));
    public static final MessageKey COMMAND_RESUME_ALREADY_RUNNING_SELF = DEFINER.define("command.resume.already-running.self", "<red>Flight mode is already running.");
    public static final MessageKey.Arg1<String> COMMAND_RESUME_ALREADY_RUNNING_OTHER = DEFINER.define("command.resume.already-running.other", "<aqua><player><red>'s flight mode is already running.").with(PLAYER_PLACEHOLDER);
    public static final MessageKey COMMAND_RESUME_NO_REMAINING_TIME_SELF = DEFINER.define("command.resume.no-remaining-time.self", "<red>You don't have remaining time for flight mode.");
    public static final MessageKey.Arg1<String> COMMAND_RESUME_NO_REMAINING_TIME_OTHER = DEFINER.define("command.resume.no-remaining-time.other", "<aqua><player><red> does not have remaining time for flight mode.").with(PLAYER_PLACEHOLDER);
    public static final MessageKey COMMAND_RESUME_CANNOT_FLY_SELF = DEFINER.define("command.resume.cannot-fly.self", "<red>You can't start flight mode there.");
    public static final MessageKey.Arg1<String> COMMAND_RESUME_CANNOT_FLY_OTHER = DEFINER.define("command.resume.cannot-fly.other", "<red>Flight mode can't be started at <aqua><player><red>'s location.").with(PLAYER_PLACEHOLDER);

    public static final Component COMMAND_TOGGLE_HELP = help(DEFINER.define("command.toggle.commandline", "/tfly toggle {player}"), DEFINER.define("command.toggle.help", "<gray>Toggles flight mode."));
    public static final MessageKey COMMAND_TOGGLE_CANNOT_TOGGLE_NOW = DEFINER.define("command.toggle.cannot-toggle-now", "<red>You can't toggle flight mode now. Please try again.");

    public static final MessageKey REMAINING_TIME_NONE = DEFINER.define("remaining-time.none", "None");
    private static final MessageKey.Arg1<Long> REMAINING_TIME_HOUR = DEFINER.define("remaining-time.hours.singular", "<hours> hour").with(hour -> Argument.numeric("hours", hour));
    private static final MessageKey.Arg1<Long> REMAINING_TIME_HOURS = DEFINER.define("remaining-time.hours.plural", "<hours> hours").with(hours -> Argument.numeric("hours", hours));
    private static final MessageKey.Arg1<Long> REMAINING_TIME_MINUTE = DEFINER.define("remaining-time.minutes.singular", "<minutes> minute").with(minute -> Argument.numeric("minutes", minute));
    private static final MessageKey.Arg1<Long> REMAINING_TIME_MINUTES = DEFINER.define("remaining-time.minutes.plural", "<minutes> minutes").with(minutes -> Argument.numeric("minutes", minutes));
    private static final MessageKey.Arg1<Long> REMAINING_TIME_SECOND = DEFINER.define("remaining-time.seconds.singular", "<seconds> second").with(second -> Argument.numeric("seconds", second));
    private static final MessageKey.Arg1<Long> REMAINING_TIME_SECONDS = DEFINER.define("remaining-time.seconds.plural", "<seconds> seconds").with(seconds -> Argument.numeric("seconds", seconds));

    private static @NotNull Component help(MessageKey commandlineKey, MessageKey helpKey) {
        return COMMAND_HELP_LINE_KEY.apply(commandlineKey, helpKey);
    }

    @Contract(pure = true)
    public static @NotNull @UnmodifiableView Map<String, String> defaultMessages() {
        return DEFINER.getCollectedMessages();
    }

    public static @NotNull ComponentLike formatTime(long sec) {
        if (sec < 1) {
            return MessageKeys.REMAINING_TIME_NONE;
        }

        TextComponent.Builder result = Component.text();

        // For someone: if you need a larger unit of time, e.g. days, see https://github.com/LuckPerms/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/util/DurationFormatter.java
        long hours = sec / 3600;
        long minutes = sec % 3600 / 60;
        long seconds = sec % 60;

        if (hours != 0) {
            var hourArg = hours == 1 ? REMAINING_TIME_HOUR : REMAINING_TIME_HOURS;
            result.append(hourArg.apply(hours));
        }

        if (minutes != 0) {
            if (hours != 0) result.appendSpace();
            var minuteArg = hours == 1 ? REMAINING_TIME_MINUTE : REMAINING_TIME_MINUTES;
            result.append(minuteArg.apply(minutes));
        }

        if (seconds != 0) {
            if (hours != 0 || minutes != 0) result.appendSpace();
            var secondArg = hours == 1 ? REMAINING_TIME_SECOND : REMAINING_TIME_SECONDS;
            result.append(secondArg.apply(seconds));
        }

        return result.build();
    }

    private MessageKeys() {
        throw new UnsupportedOperationException();
    }
}
