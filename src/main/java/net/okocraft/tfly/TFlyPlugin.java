package net.okocraft.tfly;

import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import com.github.siroshun09.messages.api.source.MiniMessageSource;
import com.github.siroshun09.messages.api.source.StringMessageMap;
import com.github.siroshun09.messages.api.util.Loader;
import com.github.siroshun09.messages.api.util.PropertiesFile;
import net.okocraft.tfly.checker.LocationChecker;
import net.okocraft.tfly.command.TFlyCommand;
import net.okocraft.tfly.command.subcommand.*;
import net.okocraft.tfly.config.TFlyConfig;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.data.TFlyDataStorage;
import net.okocraft.tfly.hook.PlaceholderAPIHook;
import net.okocraft.tfly.listener.PlayerMonitor;
import net.okocraft.tfly.listener.TFlyEventListener;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.message.MessageLoader;
import net.okocraft.tfly.player.TFlyController;
import net.okocraft.tfly.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class TFlyPlugin extends JavaPlugin {

    public static Logger logger() {
        return JavaPlugin.getPlugin(TFlyPlugin.class).getSLF4JLogger();
    }

    private final TFlyConfig config;
    private final Scheduler scheduler;
    private final TFlyDataProvider dataProvider;
    private final TFlyController controller;
    private final LocationChecker locationChecker;

    private boolean canEnable = false;
    private MiniMessageLocalization localization;
    private TFlyCommand command;

    public TFlyPlugin() {
        this.config = new TFlyConfig();
        this.scheduler = Scheduler.create(this);
        this.dataProvider = new TFlyDataProvider();
        this.controller = new TFlyController(scheduler, dataProvider);
        this.locationChecker = new LocationChecker();
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        reloadConfig();

        config.loadFrom(getConfig());
        locationChecker.addChecker(config.getWorldChecker());

        try {
            loadMessages();
        } catch (IOException e) {
            getSLF4JLogger().error("Could not load messages.", e);
        }

        canEnable = true;
    }

    @Override
    public void onEnable() {
        if (!canEnable) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        canEnable = false;

        dataProvider.init(TFlyDataStorage.create(this));
        dataProvider.scheduleSaveTask(scheduler);

        getServer().getPluginManager().registerEvents(new TFlyEventListener(config, localization), this);
        getServer().getPluginManager().registerEvents(new PlayerMonitor(scheduler, dataProvider, controller, locationChecker), this);

        command =
                new TFlyCommand(localization)
                        .addSubCommand("remaining", new RemainingCommand(localization, dataProvider))
                        .addSubCommand("add", new AddCommand(localization, dataProvider))
                        .addSubCommand("set", new SetCommand(localization, dataProvider))
                        .addSubCommand("check", new CheckCommand(localization, dataProvider))
                        .addSubCommand("reload", new ReloadCommand(localization, this::reload));
        ToggleCommand.registerAll(command, localization, dataProvider, controller, locationChecker);

        scheduler.runDelayedTask(this::hookPlaceholderAPI, 1);
    }

    @Override
    public void onDisable() {
        unhookPlaceholderAPI();

        command = null;

        HandlerList.unregisterAll(this);

        scheduler.close();

        controller.stopAll(Bukkit.getOnlinePlayers());

        dataProvider.close();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (this.command != null) {
            scheduler.runAsyncTask(() -> this.command.run(sender, args));
        }
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return this.command != null ? this.command.tabComplete(sender, args) : Collections.emptyList();
    }

    @SuppressWarnings("UnstableApiUsage")
    private void hookPlaceholderAPI() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHook.register(getPluginMeta(), localization, dataProvider);
        }
    }

    private void unhookPlaceholderAPI() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHook.unregister();
        }
    }

    private boolean reload() {
        locationChecker.removeChecker(config.getWorldChecker());
        config.loadFrom(getConfig());
        locationChecker.addChecker(config.getWorldChecker());

        try {
            loadMessages();
        } catch (IOException e) {
            getSLF4JLogger().error("Could not load messages.", e);
            return false;
        }
        return true;
    }

    private void loadMessages() throws IOException {
        var languageDirectory = getDataFolder().toPath().resolve("languages");
        var loader = MessageLoader.createLoader(languageDirectory, builtinMessageLoader()).andThen(MiniMessageSource::create);

        if (!Files.isDirectory(languageDirectory)) {
            Files.createDirectories(languageDirectory);
        }

        if (localization == null) { // on startup
            localization = new MiniMessageLocalization(MiniMessageSource.create(StringMessageMap.create(MessageKeys.defaultMessages())));
        } else { // on reload
            localization.clearSources();
        }

        try (var locales = Stream.concat(Stream.of(Locale.ENGLISH, Locale.JAPANESE), MessageLoader.fromDirectory(languageDirectory))) {
            locales.distinct().forEach(locale -> {
                var loadedSource = loader.apply(locale); // can throw IOException
                localization.addSource(locale, loadedSource);
            });
        }
    }

    private @NotNull Loader<Locale, Optional<Map<String, String>>> builtinMessageLoader() {
        return locale -> {
            if (locale.equals(Locale.ENGLISH)) {
                return Optional.of(MessageKeys.defaultMessages());
            }
            try (var input = getResource(locale + ".properties")) {
                return input != null ? Optional.of(PropertiesFile.load(input)) : Optional.empty();
            }
        };
    }
}
