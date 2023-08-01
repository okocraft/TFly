package net.okocraft.tfly.message;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.source.MiniMessageSource;
import org.jetbrains.annotations.NotNull;

public final class HelpFactory {

    public static @NotNull MiniMessageBuilder create(@NotNull MiniMessageSource source,
                                                     @NotNull String helpKey, @NotNull String commandLineKey) {
        return source.builder()
                .key(MessageKeys.COMMAND_HELP_LINE)
                .tagResolvers(
                        source.builder()
                                .key(helpKey)
                                .asPlaceholder("help"),
                        source.builder()
                                .key(commandLineKey)
                                .asPlaceholder("commandline")
                );
    }

    private HelpFactory() {
        throw new UnsupportedOperationException();
    }
}
