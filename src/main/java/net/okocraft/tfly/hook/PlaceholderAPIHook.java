package net.okocraft.tfly.hook;

import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import io.papermc.paper.plugin.configuration.PluginMeta;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.Placeholders;
import net.okocraft.tfly.util.LocaleUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public final class PlaceholderAPIHook {

    private static Expansion expansion = null;

    public static void register(@NotNull PluginMeta pluginMeta, @NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
        if (expansion == null) {
            expansion = new Expansion(pluginMeta, localization, dataProvider);
            expansion.register();
        }
    }

    public static void unregister() {
        if (expansion != null) {
            expansion.unregister();
        }
    }

    private PlaceholderAPIHook() {
        throw new UnsupportedOperationException();
    }

    private static class Expansion extends PlaceholderExpansion {

        private final PluginMeta pluginMeta;
        private final MiniMessageLocalization localization;
        private final TFlyDataProvider dataProvider;

        private Expansion(@NotNull PluginMeta pluginMeta, @NotNull MiniMessageLocalization localization, @NotNull TFlyDataProvider dataProvider) {
            this.pluginMeta = pluginMeta;
            this.localization = localization;
            this.dataProvider = dataProvider;
        }

        @Override
        public @NotNull String getIdentifier() {
            return "tfly";
        }

        @Override
        public @NotNull String getAuthor() {
            return String.join(", ", pluginMeta.getAuthors());
        }

        @Override
        public @NotNull String getVersion() {
            return pluginMeta.getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public boolean canRegister() {
            return true;
        }

        @Override
        public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
            long remainingTime;

            if (player != null) {
                var data = dataProvider.getIfLoaded(player.getUniqueId());
                remainingTime = data != null ? data.remainingTime() : 0;
            } else {
                remainingTime = 0;
            }

            return PlainTextComponentSerializer.plainText().serialize(
                    Placeholders.remainingTimeComponent(
                            remainingTime,
                            localization.findSource(LocaleUtils.getFrom(player))
                    )
            );
        }
    }
}
