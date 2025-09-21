package net.okocraft.tfly.data.storage;

import net.okocraft.tfly.TFlyPlugin;
import net.okocraft.tfly.data.TFlyData;
import net.okocraft.tfly.data.TFlyDataFlatFileStorage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class DatFileStorage implements TFlyDataFlatFileStorage {

    private static final String SPLITTER = ",";
    private static final Pattern SPLITTER_PATTERN = Pattern.compile(SPLITTER, Pattern.LITERAL);

    private final Path filepath;
    private boolean loadErrorOccurred = false;

    public DatFileStorage(@NotNull Path filepath) {
        this.filepath = filepath;
    }

    @Override
    public void loadAll(@NotNull Map<UUID, TFlyData> dataMap) {
        if (!Files.isRegularFile(filepath)) {
            return;
        }

        try (var reader = Files.newBufferedReader(filepath, StandardCharsets.UTF_8); var lines = reader.lines()) {
            lines.forEach(line -> processLine(line, dataMap));
        } catch (IOException | UncheckedIOException e) {
            TFlyPlugin.logger().error("Could not load data from {}", filepath.getFileName(), e);
            loadErrorOccurred = true;
        }
    }

    @Override
    public void saveAll(@NotNull Map<UUID, TFlyDataRecord> dataMap) {
        try {
            saveAll0(dataMap);
        } catch (IOException e) {
            TFlyPlugin.logger().error("Could not save data to {}", filepath.getFileName(), e);
        }
    }

    private void processLine(@NotNull String line, @NotNull Map<UUID, TFlyData> dataMap) {
        var elements = SPLITTER_PATTERN.split(line);

        UUID uuid = null;
        long remainingTime = 0;
        boolean stoppedOnQuit = true;

        if (0 < elements.length) {
            try {
                uuid = UUID.fromString(elements[0]);
            } catch (IllegalArgumentException e) {
                return;
            }
        }

        if (1 < elements.length) {
            try {
                remainingTime = Math.max(0, Long.parseLong(elements[1]));
            } catch (NumberFormatException ignored) {
            }
        }

        if (2 < elements.length) {
            stoppedOnQuit = Boolean.parseBoolean(elements[2]);
        }

        dataMap.put(uuid, new TFlyData(remainingTime, stoppedOnQuit));
    }

    private void saveAll0(@NotNull Map<UUID, TFlyDataRecord> dataMap) throws IOException {
        if (loadErrorOccurred && Files.isRegularFile(filepath)) {
            Files.move(filepath, filepath.getParent().resolve(filepath.getFileName() + "-" + System.currentTimeMillis()));
            loadErrorOccurred = false;
        }

        if (!Files.isDirectory(filepath.getParent())) {
            Files.createDirectories(filepath.getParent());
        }

        try (var writer = Files.newBufferedWriter(filepath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (var uuid : dataMap.keySet()) {
                var data = dataMap.get(uuid);

                if (data.remainingTime() < 1) {
                    continue;
                }

                writer.write(uuid.toString());
                writer.write(SPLITTER);
                writer.write(Long.toString(data.remainingTime()));
                writer.write(SPLITTER);
                writer.write(Boolean.toString(data.stoppedOnQuit()));
                writer.newLine();
            }
        }
    }
}
