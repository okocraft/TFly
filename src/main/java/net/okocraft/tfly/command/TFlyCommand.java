package net.okocraft.tfly.command;

import net.okocraft.tfly.command.subcommand.HelpCommand;
import net.okocraft.tfly.command.subcommand.SubCommand;
import net.okocraft.tfly.message.MessageKeys;
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
    private final HelpCommand helpCommand;

    public TFlyCommand() {
        this.helpCommand = new HelpCommand(() -> this.subCommandMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue));
        this.subCommandMap.put("help", this.helpCommand);
    }

    public void run(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("tfly.command")) {
            sendNoPermission(sender, "tfly.command");
            return;
        }

        SubCommand subCommand;

        if (args.length == 0) {
            subCommand = this.helpCommand;
        } else {
            subCommand = this.subCommandMap.getOrDefault(args[0].toLowerCase(Locale.ENGLISH), this.helpCommand);
        }

        if (sender.hasPermission(subCommand.permissionNode())) {
            subCommand.run(sender, args);
        } else {
            sendNoPermission(sender, subCommand.permissionNode());
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
                    .filter(cmd -> sender.hasPermission(this.subCommandMap.get(cmd).permissionNode()))
                    .toList();
        }

        var subCommand = getSubCommand(args[0]);

        if (subCommand != null && sender.hasPermission(subCommand.permissionNode())) {
            return subCommand.tabComplete(sender, args);
        } else {
            return Collections.emptyList();
        }
    }

    private void sendNoPermission(@NotNull CommandSender target, @NotNull String permissionNode) {
        target.sendMessage(MessageKeys.NO_PERMISSION.apply(permissionNode));
    }

    public @Nullable SubCommand getSubCommand(@NotNull String name) {
        return this.subCommandMap.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Contract("_, _ -> this")
    public @NotNull TFlyCommand addSubCommand(@NotNull String name, @NotNull SubCommand subCommand) {
        this.subCommandMap.put(name.toLowerCase(Locale.ENGLISH), subCommand);
        return this;
    }
}
