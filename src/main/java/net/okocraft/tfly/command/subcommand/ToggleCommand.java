package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.minimessage.base.MiniMessageBase;
import com.github.siroshun09.messages.minimessage.localization.MiniMessageLocalization;
import com.github.siroshun09.messages.minimessage.source.MiniMessageSource;
import net.okocraft.tfly.checker.LocationChecker;
import net.okocraft.tfly.command.TFlyCommand;
import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.player.TFlyController;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public @NotNull MiniMessageBase help() {
        return switch (type) {
            case PAUSE -> MessageKeys.COMMAND_PAUSE_HELP;
            case RESUME -> MessageKeys.COMMAND_RESUME_HELP;
            case TOGGLE -> MessageKeys.COMMAND_TOGGLE_HELP;
        };
    }

    @Override
    public @NotNull String permissionNode() {
        return "tfly.command.toggle";
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        boolean self = args.length == 1;
        var source = localization.findSource(sender);

        if (!self && !sender.hasPermission(otherPermissionNode())) {
            MessageKeys.NO_PERMISSION.apply(otherPermissionNode()).source(source).send(sender);
            return;
        }

        var target = getTargetFromSenderOrArgument(sender, self ? null : args[1], source);

        if (target == null) {
            return;
        }

        var data = dataProvider.getOrLoad(target.getUniqueId());
        var status = data.status();

        if (status == TFlyData.Status.STARTING || status == TFlyData.Status.STOPPING) {
            MessageKeys.COMMAND_TOGGLE_CANNOT_TOGGLE_NOW.source(source).send(sender);
            return;
        }

        boolean pause = switch (type) {
            case PAUSE -> true;
            case RESUME -> false;
            case TOGGLE -> status == TFlyData.Status.RUNNING;
        };

        if (pause) {
            pause(sender, target, data, source);
        } else {
            resume(sender, target, data, source);
        }
    }

    protected @Nullable Player getTargetFromSenderOrArgument(@NotNull CommandSender sender, @Nullable String playerName, @NotNull MiniMessageSource source) {
        if (playerName == null || playerName.isEmpty()) { // /tfly <cmd>
            if (sender instanceof Player player) {
                return player;
            } else {
                help().source(source).send(sender);
                return null;
            }
        } else { // /tfly <cmd> <player>
            var target = Bukkit.getPlayer(playerName);

            if (target != null) {
                return target;
            } else {
                MessageKeys.COMMAND_GENERAL_PLAYER_NOT_FOUND.apply(playerName).source(source).send(sender);
                return null;
            }
        }
    }

    private void pause(@NotNull CommandSender sender, @NotNull Player target, @NotNull TFlyData data, @NotNull MiniMessageSource source) {
        if (data.statusIf(status -> status == TFlyData.Status.RUNNING || status == TFlyData.Status.STARTING, TFlyData.Status.STOPPING)) {
            controller.stop(target, true);
        } else {
            if (sender == target) {
                MessageKeys.COMMAND_PAUSE_ALREADY_STOPPED_SELF.source(source).send(sender);
            } else {
                MessageKeys.COMMAND_PAUSE_ALREADY_STOPPED_OTHER.apply(target.getName()).source(source).send(sender);
            }
        }
    }

    private void resume(@NotNull CommandSender sender, @NotNull Player target, @NotNull TFlyData data, @NotNull MiniMessageSource source) {
        if (!locationChecker.test(target)) {
            if (sender == target) {
                MessageKeys.COMMAND_RESUME_CANNOT_FLY_SELF.source(source).send(sender);
            } else {
                MessageKeys.COMMAND_RESUME_CANNOT_FLY_OTHER.apply(target.getName()).source(source).send(sender);
            }
            return;
        }

        if (data.remainingTime() < 1) {
            if (sender == target) {
                MessageKeys.COMMAND_RESUME_NO_REMAINING_TIME_SELF.source(source).send(sender);
            } else {
                MessageKeys.COMMAND_RESUME_NO_REMAINING_TIME_OTHER.apply(target.getName()).source(source).send(sender);
            }
            return;
        }

        if (data.statusIf(status -> status == TFlyData.Status.STOPPED, TFlyData.Status.STARTING)) {
            controller.start(target);
        } else {
            if (sender == target) {
                MessageKeys.COMMAND_RESUME_ALREADY_RUNNING_SELF.source(source).send(sender);
            } else {
                MessageKeys.COMMAND_RESUME_ALREADY_RUNNING_OTHER.apply(target.getName()).source(source).send(sender);
            }
        }
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
