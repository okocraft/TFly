package net.okocraft.tfly;

import dev.siroshun.mcmsgdef.directory.DirectorySource;
import dev.siroshun.mcmsgdef.directory.MessageProcessors;
import dev.siroshun.mcmsgdef.file.PropertiesFile;
import net.kyori.adventure.key.Key;
import net.okocraft.tfly.checker.LocationChecker;
import net.okocraft.tfly.command.TFlyCommand;
import net.okocraft.tfly.command.subcommand.AddCommand;
import net.okocraft.tfly.command.subcommand.CheckCommand;
import net.okocraft.tfly.command.subcommand.ReloadCommand;
import net.okocraft.tfly.command.subcommand.RemainingCommand;
import net.okocraft.tfly.command.subcommand.SetCommand;
import net.okocraft.tfly.command.subcommand.ToggleCommand;
import net.okocraft.tfly.config.TFlyConfig;
import net.okocraft.tfly.data.TFlyDataProvider;
import net.okocraft.tfly.data.TFlyDataStorage;
import net.okocraft.tfly.hook.PlaceholderAPIHook;
import net.okocraft.tfly.listener.PlayerMonitor;
import net.okocraft.tfly.listener.TFlyEventListener;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.player.TFlyController;
import net.okocraft.tfly.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        getServer().getPluginManager().registerEvents(new TFlyEventListener(config), this);
        getServer().getPluginManager().registerEvents(new PlayerMonitor(scheduler, dataProvider, controller, locationChecker), this);

        command =
                new TFlyCommand()
                        .addSubCommand("remaining", new RemainingCommand(dataProvider))
                        .addSubCommand("add", new AddCommand(dataProvider))
                        .addSubCommand("set", new SetCommand(dataProvider))
                        .addSubCommand("check", new CheckCommand(dataProvider))
                        .addSubCommand("reload", new ReloadCommand(this::reload));
        ToggleCommand.registerAll(command, dataProvider, controller, locationChecker);

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (this.command != null) {
            scheduler.runAsyncTask(() -> this.command.run(sender, args));
        }
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        return this.command != null ? this.command.tabComplete(sender, args) : Collections.emptyList();
    }

    private void hookPlaceholderAPI() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHook.register(getPluginMeta(), dataProvider);
        }
    }

    private void unhookPlaceholderAPI() {
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIHook.unregister();
        }
    }

    private boolean reload() {
        locationChecker.removeChecker(config.getWorldChecker());
        reloadConfig();
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
        DirectorySource.propertiesFiles(this.getDataFolder().toPath().resolve("languages"))
                .defaultLocale(Locale.ENGLISH, Locale.JAPANESE)
                .messageProcessor(MessageProcessors.appendMissingMessagesToPropertiesFile(this::loadDefaultMessageMap))
                .loadAndRegister(Key.key("tfly", "languages"));
    }

    private @Nullable Map<String, String> loadDefaultMessageMap(@NotNull Locale locale) throws IOException {
        if (locale.equals(Locale.ENGLISH)) {
            return MessageKeys.defaultMessages();
        } else {
            try (var input = this.getResource(locale + ".properties")) {
                return input != null ? PropertiesFile.load(input) : null;
            }
        }
    }
}
