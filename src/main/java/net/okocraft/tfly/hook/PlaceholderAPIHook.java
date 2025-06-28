package net.okocraft.tfly.hook;

import io.papermc.paper.plugin.configuration.PluginMeta;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.message.MessageKeys;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public final class PlaceholderAPIHook {

    private static Expansion expansion = null;

    public static void register(@NotNull PluginMeta pluginMeta, @NotNull TFlyDataProvider dataProvider) {
        if (expansion == null) {
            expansion = new Expansion(pluginMeta, dataProvider);
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
        private final TFlyDataProvider dataProvider;

        private Expansion(@NotNull PluginMeta pluginMeta, @NotNull TFlyDataProvider dataProvider) {
            this.pluginMeta = pluginMeta;
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
            if (player == null) {
                return "";
            }

            TFlyData data = this.dataProvider.getIfLoaded(player.getUniqueId());
            long remainingTime = data != null ? data.remainingTime() : 0;

            Component translated = GlobalTranslator.render(MessageKeys.formatTime(remainingTime).asComponent(), player.locale());
            return PlainTextComponentSerializer.plainText().serialize(translated);
        }
    }
}
