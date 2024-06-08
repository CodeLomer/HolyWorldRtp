package com.github.codelomer.holyworldrtp.command;

import lombok.NonNull;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    protected AbstractCommand(@NonNull String command, @NonNull JavaPlugin plugin) {
        PluginCommand pluginCommand = plugin.getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    public abstract void execute(CommandSender sender, String label, String[] args);


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, label, args);
        return true;
    }
}
