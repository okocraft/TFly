package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import net.okocraft.tfly.checker.LocationChecker;
import net.okocraft.tfly.command.TFlyCommand;
import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.HelpFactory;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.message.Placeholders;
import net.okocraft.tfly.player.TFlyController;
import net.okocraft.tfly.util.LocaleUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class ToggleCommand implements SubCommand {

    public static void registerAll(@NotNull TFlyCommand command, @NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider, @NotNull TFlyController controller, @NotNull LocationChecker locationChecker) {
        command.addSubCommand("toggle", new ToggleCommand(localization, dataProvider, controller, locationChecker, Type.TOGGLE))
                .addSubCommand("pause", new ToggleCommand(localization, dataProvider, controller, locationChecker, Type.PAUSE))
                .addSubCommand("resume", new ToggleCommand(localization, dataProvider, controller, locationChecker, Type.RESUME));
    }

    private final MiniMessageLocalization localization;
    private final TFlyDataProvider dataProvider;
    private final TFlyController controller;
    private final LocationChecker locationChecker;
    private final Type type;

    private ToggleCommand(@NotNull MiniMessageLocalization localization,
                          @NotNull TFlyDataProvider dataProvider,
                          @NotNull TFlyController controller,
                          @NotNull LocationChecker locationChecker,
                          @NotNull Type type) {
        this.localization = localization;
        this.dataProvider = dataProvider;
        this.controller = controller;
        this.locationChecker = locationChecker;
        this.type = type;
    }

    @Override
    public @NotNull MiniMessageBuilder help(@NotNull Locale locale) {
        return HelpFactory.create(localization.findSource(locale), helpKey(), commandlineKey());
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.toggle";
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        boolean self = args.length == 1;
        var locale = LocaleUtils.getFrom(sender);

        if (!self && !sender.hasPermission(otherPermissionNode())) {
            localization.findSource(locale)
                    .builder()
                    .key(MessageKeys.NO_PERMISSION)
                    .tagResolver(Placeholders.permission(otherPermissionNode()))
                    .send(sender);
            return;
        }

        var target = getTargetFromSenderOrArgument(sender, self ? null : args[1]);

        if (target == null) {
            return;
        }

        var data = dataProvider.getOrLoad(target.getUniqueId());
        var status = data.status();

        if (status == TFlyData.Status.STARTING || status == TFlyData.Status.STOPPING) {
            localization.findSource(locale)
                    .builder()
                    .key(MessageKeys.COMMAND_TOGGLE_CANNOT_TOGGLE_NOW)
                    .send(sender);
            return;
        }

        boolean pause = switch (type) {
            case PAUSE -> true;
            case RESUME -> false;
            case TOGGLE -> status == TFlyData.Status.RUNNING;
        };

        if (pause) {
            pause(sender, target, data);
        } else {
            resume(sender, target, data);
        }
    }

    protected @Nullable Player getTargetFromSenderOrArgument(@NotNull CommandSender sender, @Nullable String playerName) {
        if (playerName == null || playerName.isEmpty()) { // /tfly <cmd>
            if (sender instanceof Player player) {
                return player;
            } else {
                help(LocaleUtils.getFrom(sender)).send(sender);
                return null;
            }
        } else { // /tfly <cmd> <player>
            var target = Bukkit.getPlayer(playerName);

            if (target != null) {
                return target;
            } else {
                localization.findSource(LocaleUtils.getFrom(sender))
                        .builder()
                        .key(MessageKeys.COMMAND_GENERAL_PLAYER_NOT_FOUND)
                        .tagResolvers(Placeholders.player(playerName))
                        .send(sender);
                return null;
            }
        }
    }

    private void pause(@NotNull CommandSender sender, @NotNull Player target, @NotNull TFlyData data) {
        if (data.statusIf(status -> status == TFlyData.Status.RUNNING || status == TFlyData.Status.STARTING, TFlyData.Status.STOPPING)) {
            controller.stop(target, true);
        } else {
            var builder = localization.findSource(LocaleUtils.getFrom(sender)).builder();

            if (sender == target) {
                builder.key(MessageKeys.COMMAND_PAUSE_ALREADY_STOPPED_SELF);
            } else {
                builder.key(MessageKeys.COMMAND_PAUSE_ALREADY_STOPPED_OTHER).tagResolver(Placeholders.player(target.getName()));
            }

            builder.send(sender);
        }
    }

    private void resume(@NotNull CommandSender sender, @NotNull Player target, @NotNull TFlyData data) {
        if (!locationChecker.test(target)) {
            var builder = localization.findSource(LocaleUtils.getFrom(sender)).builder();

            if (sender == target) {
                builder.key(MessageKeys.COMMAND_RESUME_CANNOT_FLY_SELF);
            } else {
                builder.key(MessageKeys.COMMAND_RESUME_CANNOT_FLY_OTHER).tagResolver(Placeholders.player(target.getName()));
            }

            builder.send(sender);
            return;
        }

        if (data.remainingTime() < 1) {
            var builder = localization.findSource(LocaleUtils.getFrom(sender)).builder();

            if (sender == target) {
                builder.key(MessageKeys.COMMAND_RESUME_NO_REMAINING_TIME_SELF);
            } else {
                builder.key(MessageKeys.COMMAND_RESUME_NO_REMAINING_TIME_OTHER).tagResolver(Placeholders.player(target.getName()));
            }

            builder.send(sender);
            return;
        }

        if (data.statusIf(status -> status == TFlyData.Status.STOPPED, TFlyData.Status.STARTING)) {
            controller.start(target);
        } else {
            var builder = localization.findSource(LocaleUtils.getFrom(sender)).builder();

            if (sender == target) {
                builder.key(MessageKeys.COMMAND_RESUME_ALREADY_RUNNING_SELF);
            } else {
                builder.key(MessageKeys.COMMAND_RESUME_ALREADY_RUNNING_OTHER)
                        .tagResolver(Placeholders.player(target.getName()));
            }

            builder.send(sender);
        }
    }

    private @NotNull String helpKey() {
        return switch (type) {
            case PAUSE -> MessageKeys.COMMAND_PAUSE_HELP;
            case RESUME -> MessageKeys.COMMAND_RESUME_HELP;
            case TOGGLE -> MessageKeys.COMMAND_TOGGLE_HELP;
        };
    }

    private @NotNull String commandlineKey() {
        return switch (type) {
            case PAUSE -> MessageKeys.COMMAND_PAUSE_COMMANDLINE;
            case RESUME -> MessageKeys.COMMAND_RESUME_COMMANDLINE;
            case TOGGLE -> MessageKeys.COMMAND_TOGGLE_COMMANDLINE;
        };
    }

    private @NotNull String otherPermissionNode() {
        return permissionNode() + ".other";
    }

    private enum Type {
        PAUSE,
        RESUME,
        TOGGLE,
    }
}
