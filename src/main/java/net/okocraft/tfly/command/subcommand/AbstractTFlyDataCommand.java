package net.okocraft.tfly.command.subcommand;

import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.message.Placeholders;
import net.okocraft.tfly.util.LocaleUtils;
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
        UUID target;

        if (playerName == null || playerName.isEmpty()) { // /tfly <cmd>
            if (sender instanceof Player player) {
                target = player.getUniqueId();
            } else {
                help(LocaleUtils.getFrom(sender)).send(sender);
                return null;
            }
        } else { // /tfly <cmd> <player>
            target = Bukkit.getPlayerUniqueId(playerName);

            if (target == null) {
                localization.findSource(LocaleUtils.getFrom(sender))
                        .builder()
                        .key(MessageKeys.COMMAND_GENERAL_PLAYER_NOT_FOUND)
                        .tagResolvers(Placeholders.player(playerName))
                        .send(sender);
                return null;
            }
        }

        return dataProvider.getOrLoad(target);
    }

    protected @Nullable Long parseNumber(@NotNull CommandSender sender, @NotNull String num) {
        try {
            return Long.parseLong(num);
        } catch (NumberFormatException e) {
            localization.findSource(LocaleUtils.getFrom(sender))
                    .builder()
                    .key(MessageKeys.COMMAND_GENERAL_INVALID_NUMBER)
                    .tagResolver(Placeholders.argument(num))
                    .send(sender);
            return null;
        }
    }
}
