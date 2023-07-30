package net.okocraft.tfly.command;

import com.github.siroshun09.messages.api.localize.MiniMessageLocalization;
import net.okocraft.tfly.command.subcommand.HelpCommand;
import net.okocraft.tfly.command.subcommand.SubCommand;
import net.okocraft.tfly.message.MessageKeys;
import net.okocraft.tfly.message.Placeholders;
import net.okocraft.tfly.util.LocaleUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TFlyCommand {

    private final Map<String, SubCommand> subCommandMap = new ConcurrentHashMap<>();
    private final MiniMessageLocalization localization;
    private final HelpCommand helpCommand;

    public TFlyCommand(@NotNull MiniMessageLocalization localization) {
        this.localization = localization;
        this.helpCommand = new HelpCommand(localization, () -> subCommandMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue));

        subCommandMap.put("help", helpCommand);
    }

    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        var locale = LocaleUtils.getFrom(sender);

        if (!sender.hasPermission("tfly.command")) {
            sendNoPermission(sender, locale, "tfly.command");
            return;
        }

        SubCommand subCommand;

        if (args.length == 0) {
            subCommand = helpCommand;
        } else {
            subCommand = subCommandMap.getOrDefault(args[0].toLowerCase(Locale.ENGLISH), helpCommand);
        }

        if (sender.hasPermission(subCommand.permissionNode())) {
            subCommand.run(sender, args);
        } else {
            sendNoPermission(sender, locale, subCommand.permissionNode());
        }
    }

    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (args.length == 0 || !sender.hasPermission("tfly.command")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return subCommandMap.keySet()
                    .stream()
                    .filter(cmd -> cmd.startsWith(args[0].toLowerCase(Locale.ENGLISH)))
                    .filter(cmd -> sender.hasPermission(subCommandMap.get(cmd).permissionNode()))
                    .toList();
        }

        var subCommand = getSubCommand(args[0]);

        if (subCommand != null && sender.hasPermission(subCommand.permissionNode())) {
            return subCommand.tabComplete(sender, args);
        } else {
            return Collections.emptyList();
        }
    }

    private void sendNoPermission(@NotNull CommandSender target, @NotNull Locale locale, @NotNull String permissionNode) {
        localization.findSource(locale).builder().key(MessageKeys.NO_PERMISSION).tagResolver(Placeholders.permission(permissionNode)).send(target);
    }

    public @Nullable SubCommand getSubCommand(@NotNull String name) {
        return subCommandMap.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Contract("_, _ -> this")
    public @NotNull TFlyCommand addSubCommand(@NotNull String name, @NotNull SubCommand subCommand) {
        subCommandMap.put(name.toLowerCase(Locale.ENGLISH), subCommand);
        return this;
    }
}
