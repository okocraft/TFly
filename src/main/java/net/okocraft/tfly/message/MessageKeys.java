package net.okocraft.tfly.message;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MessageKeys {

    private static final Map<String, String> DEFAULT_MESSAGES = new LinkedHashMap<>();

    public static final String NO_PERMISSION = def("no-permission", "<gray>You don't have the permission: <aqua><permission>");
    public static final String NOTIFICATION_MESSAGE_STARTED = def("notification.message.started", "<gray>Flight mode started.");
    public static final String NOTIFICATION_MESSAGE_STOPPED = def("notification.message.stopped", "<gray>Flight mode stopped.");
    public static final String NOTIFICATION_ACTIONBAR_REMAINING_TIME = def("notification.actionbar.remaining-time", "<yellow>Flight Mode: <aqua><remaining_time><yellow> left");
    public static final String NOTIFICATION_MESSAGE_REMAINING = def("notification.message.remaining-time", "<gray>Your flight mode has <aqua><remaining_time><gray> remaining.");

    public static final String COMMAND_GENERAL_PLAYER_NOT_FOUND = def("command.general.player-not-found", "<red>The player named <aqua><player><red> cannot be found.");
    public static final String COMMAND_GENERAL_INVALID_NUMBER = def("command.general.invalid-number", "<aqua><argument><red> is not valid number.");

    public static final String COMMAND_HELP_HEADER = def("command.help.header", "<dark_gray><st>=========================<reset><gold><b> TFly <reset><dark_gray><st>=========================");
    public static final String COMMAND_HELP_LINE = def("command.help.line", "<aqua><commandline><dark_gray>: <help>");
    public static final String COMMAND_HELP_HELP = def("command.help.help", "<gray>Shows this help.");
    public static final String COMMAND_HELP_COMMANDLINE = def("command.help.commandline", "/tfly help");

    public static final String COMMAND_RELOAD_SUCCESS = def("command.reload.success", "<gray>config.yml and languages have been reloaded!");
    public static final String COMMAND_RELOAD_FAILURE = def("command.reload.failure", "<red>Some errors are occurred while reloading. Please check your console.");
    public static final String COMMAND_RELOAD_HELP = def("command.reload.help", "<gray>Reloads config.yml and languages.");
    public static final String COMMAND_RELOAD_COMMANDLINE = def("command.reload.commandline", "/tfly reload");

    public static final String COMMAND_REMAINING_TIME_SELF = def("command.remaining.time.self", "<gray>You have <aqua><remaining_time><gray> left.");
    public static final String COMMAND_REMAINING_TIME_OTHER = def("command.remaining.time.other", "<aqua><player><gray> has <aqua><remaining_time><gray> left.");
    public static final String COMMAND_REMAINING_NO_TIME_SELF = def("command.remaining.no-time.self", "<gray>You have no remaining time for flight mode.");
    public static final String COMMAND_REMAINING_NO_TIME_OTHER = def("command.remaining.no-time.other", "<aqua><player><gray> has no remaining time for flight mode.");
    public static final String COMMAND_REMAINING_HELP = def("command.remaining.help", "<gray>Shows own or specified player's remaining time.");
    public static final String COMMAND_REMAINING_COMMANDLINE = def("command.remaining.commandline", "/tfly remaining {player}");

    public static final String COMMAND_ADD_SUCCESS = def("command.add.success", "<aqua><seconds><gray> second(s) added to <aqua><player><aqua><gray> (Now: <aqua><remaining_time><gray>).");
    public static final String COMMAND_ADD_HELP = def("command.add.help", "<gray>Adds flight time to the player.");
    public static final String COMMAND_ADD_COMMANDLINE = def("command.add.commandline", "/tfly add <player> <seconds>");

    public static final String COMMAND_CHECK_FLYING = def("command.check.flying", "<aqua><player><aqua><gray> is flying by TFly. (Remaining: <aqua><remaining_time><gray>).");
    public static final String COMMAND_CHECK_FLYING_BUT_NOT_TFLY = def("command.check.flying-but-not-tfly", "<aqua><player><aqua><gray> is flying, but not using TFly.");
    public static final String COMMAND_CHECK_NOT_FLYING = def("command.check.not-flying", "<aqua><player><aqua><gray> is not flying.");
    public static final String COMMAND_CHECK_HELP = def("command.check.help", "<gray>Checks if the player is flying by TFly.");
    public static final String COMMAND_CHECK_COMMANDLINE = def("command.check.commandline", "/tfly check <player>");

    public static final String COMMAND_SET_SUCCESS = def("command.set.success", "<gray>Set <aqua><player><aqua><gray>'s flight time to <aqua><remaining_time><gray>.");
    public static final String COMMAND_SET_HELP = def("command.set.help", "<gray>Sets the player's flight time.");
    public static final String COMMAND_SET_COMMANDLINE = def("command.set.commandline", "/tfly set <player> <seconds>");

    public static final String COMMAND_PAUSE_HELP = def("command.pause.help", "<gray>Pauses flight mode.");
    public static final String COMMAND_PAUSE_COMMANDLINE = def("command.pause.commandline", "/tfly pause {player}");
    public static final String COMMAND_PAUSE_ALREADY_STOPPED_SELF = def("command.pause.already-stopped.self", "<red>Flight mode is already stopped.");
    public static final String COMMAND_PAUSE_ALREADY_STOPPED_OTHER = def("command.pause.already-stopped.other", "<aqua><player><red>'s flight mode is already stopped.");

    public static final String COMMAND_RESUME_HELP = def("command.resume.help", "<gray>Resumes flight mode.");
    public static final String COMMAND_RESUME_COMMANDLINE = def("command.resume.commandline", "/tfly resume {player}");
    public static final String COMMAND_RESUME_ALREADY_RUNNING_SELF = def("command.resume.already-running.self", "<red>Flight mode is already running.");
    public static final String COMMAND_RESUME_ALREADY_RUNNING_OTHER = def("command.resume.already-running.other", "<aqua><player><red>'s flight mode is already running.");
    public static final String COMMAND_RESUME_NO_REMAINING_TIME_SELF = def("command.resume.no-remaining-time.self", "<red>You don't have remaining time for flight mode.");
    public static final String COMMAND_RESUME_NO_REMAINING_TIME_OTHER = def("command.resume.no-remaining-time.other", "<aqua><player><red> does not have remaining time for flight mode.");
    public static final String COMMAND_RESUME_CANNOT_FLY_SELF = def("command.resume.cannot-fly.self", "<red>You can't start flight mode there.");
    public static final String COMMAND_RESUME_CANNOT_FLY_OTHER = def("command.resume.cannot-fly.other", "<red>Flight mode can't be started at <aqua><player><red>'s location.");

    public static final String COMMAND_TOGGLE_HELP = def("command.toggle.help", "<gray>Toggles flight mode.");
    public static final String COMMAND_TOGGLE_COMMANDLINE = def("command.toggle.commandline", "/tfly toggle {player}");
    public static final String COMMAND_TOGGLE_CANNOT_TOGGLE_NOW = def("command.toggle.cannot-toggle-now", "<red>You can't toggle flight mode now. Please try again.");

    static {
        // for placeholders
        def("remaining-time.none", "None");
        def("remaining-time.hours.singular", "<hours> hour");
        def("remaining-time.hours.plural", "<hours> hours");
        def("remaining-time.minutes.singular", "<minutes> minute");
        def("remaining-time.minutes.plural", "<minutes> minutes");
        def("remaining-time.seconds.singular", "<seconds> second");
        def("remaining-time.seconds.plural", "<seconds> seconds");
    }

    @Contract("_, _ -> param1")
    private static @NotNull String def(@NotNull String key, @NotNull String msg) {
        DEFAULT_MESSAGES.put(key, msg);
        return key;
    }

    @Contract(pure = true)
    public static @NotNull @UnmodifiableView Map<String, String> defaultMessages() {
        return Collections.unmodifiableMap(DEFAULT_MESSAGES);
    }

    private MessageKeys() {
        throw new UnsupportedOperationException();
    }
}
