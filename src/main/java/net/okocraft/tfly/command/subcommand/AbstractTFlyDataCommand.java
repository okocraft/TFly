package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.minimessage.localization.MiniMessageLocalization;
import com.github.siroshun09.messages.minimessage.source.MiniMessageSource;
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

    protected final MiniMessageLocalization localization;
    protected final TFlyDataProvider dataProvider;

    AbstractTFlyDataCommand(@NotNull MiniMessageLocalization localization,
                            @NotNull TFlyDataProvider dataProvider) {
        this.localization = localization;
        this.dataProvider = dataProvider;
    }

    protected @Nullable TFlyData getTFlyDataFromSenderOrArgument(@NotNull CommandSender sender, @Nullable String playerName) {
        var source = localization.findSource(sender);

        UUID target;

        if (playerName == null || playerName.isEmpty()) { // /tfly <cmd>
            if (sender instanceof Player player) {
                target = player.getUniqueId();
            } else {
                help().source(source).send(sender);
                return null;
            }
        } else { // /tfly <cmd> <player>
            target = Bukkit.getPlayerUniqueId(playerName);

            if (target == null) {
                MessageKeys.COMMAND_GENERAL_PLAYER_NOT_FOUND.apply(playerName).source(source).send(sender);
                return null;
            }
        }

        return dataProvider.getOrLoad(target);
    }

    protected @Nullable Long parseNumber(@NotNull CommandSender sender, @NotNull String num, @NotNull MiniMessageSource source) {
        try {
            return Long.parseLong(num);
        } catch (NumberFormatException e) {
            MessageKeys.COMMAND_GENERAL_INVALID_NUMBER.apply(num).source(source).send(sender);
            return null;
        }
    }
}
