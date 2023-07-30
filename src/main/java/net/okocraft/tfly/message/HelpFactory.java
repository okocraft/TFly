package net.okocraft.tfly.message;

import com.github.siroshun09.messages.api.builder.MiniMessageBuilder;
import com.github.siroshun09.messages.api.util.MessageBuilderFactory;
import org.jetbrains.annotations.NotNull;

public final class HelpFactory {

    public static @NotNull MiniMessageBuilder create(@NotNull MessageBuilderFactory<MiniMessageBuilder> builderFactory,
                                                     @NotNull String helpKey, @NotNull String commandLineKey) {
        return builderFactory.create()
                .key(MessageKeys.COMMAND_HELP_LINE)
                .tagResolvers(
                        builderFactory.create()
                                .key(helpKey)
                                .asPlaceholder("help"),
                        builderFactory.create()
                                .key(commandLineKey)
                                .asPlaceholder("commandline")
                );
    }

    private HelpFactory() {
        throw new UnsupportedOperationException();
    }
}
