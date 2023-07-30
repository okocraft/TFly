package net.okocraft.tfly.message;

import com.github.siroshun09.messages.api.source.StringMessageMap;
import com.github.siroshun09.messages.api.util.Loader;
import com.github.siroshun09.messages.api.util.PropertiesFile;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class MessageLoader {

    @SuppressWarnings("resource")
    public static @NotNull Stream<Locale> fromDirectory(@NotNull Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            return Stream.empty();
        } else {
            return Files.list(directory)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(filename -> filename.endsWith(".properties") ? filename.replace(".properties", "") : "")
                    .filter(Predicate.not(String::isEmpty))
                    .map(Translator::parseLocale)
                    .filter(Objects::nonNull);
        }
    }

    public static @NotNull Loader<Locale, StringMessageMap> createLoader(@NotNull Path directory, @NotNull Loader<Locale, Optional<Map<String, String>>> builtinMessageMap) {
        return locale -> {
            var filepath = directory.resolve(locale + ".properties");
            var loaded = PropertiesFile.load(filepath);

            var builtin = builtinMessageMap.load(locale);

            Map<String, String> missing = null;

            if (builtin.isPresent()) {
                for (var entry : builtin.get().entrySet()) {
                    if (!loaded.containsKey(entry.getKey())) {
                        loaded.put(entry.getKey(), entry.getValue());

                        if (missing == null) {
                            missing = new LinkedHashMap<>();
                        }

                        missing.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            if (missing != null || !Files.isRegularFile(filepath)) {
                PropertiesFile.append(filepath, Objects.requireNonNullElse(missing, loaded));
            }

            return StringMessageMap.create(loaded);
        };
    }

    private MessageLoader() {
        throw new UnsupportedOperationException();
    }
}
