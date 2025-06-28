package net.okocraft.tfly.command.subcommand;

import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

abstract class AbstractTFlyDataCommand implements SubCommand {

    protected final TFlyDataProvider dataProvider;

    AbstractTFlyDataCommand(@NotNull TFlyDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    protected @Nullable TFlyData getTFlyDataFromSenderOrArgument(@NotNull CommandSender sender, @Nullable String playerName) {
        UUID target;

        if (playerName == null || playerName.isEmpty()) { // /tfly <cmd>
            if (sender instanceof Player player) {
                target = player.getUniqueId();
            } else {
                sender.sendMessage(this.help());
                return null;
            }
        } else { // /tfly <cmd> <player>
            target = Bukkit.getPlayerUniqueId(playerName);

            if (target == null) {
                sender.sendMessage(MessageKeys.COMMAND_GENERAL_PLAYER_NOT_FOUND.apply(playerName));
                return null;
            }
        }

        return dataProvider.getOrLoad(target);
    }

    protected static @Nullable Long parseNumber(@NotNull CommandSender sender, @NotNull String num) {
        try {
            return Long.parseLong(num);
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageKeys.COMMAND_GENERAL_INVALID_NUMBER.apply(num));
            return null;
        }
    }
}
