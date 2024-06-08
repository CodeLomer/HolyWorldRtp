package com.github.codelomer.holyworldrtp.util;

import com.github.codelomer.holyworldrtp.HolyWorldRtp;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;
import java.util.Map;

public final class BukkitLogger {
    private static final ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();

    private BukkitLogger() {
    }

    private static final String PLUGIN_NAME = "[" + HolyWorldRtp.getInstance().getName() + "] ";

    public static void logColorText(@NonNull List<String> message, @NonNull CommandSender sender, Map<String, String> placeholders) {
        if (message.isEmpty()) return;
        for (String line : message) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                line = line.replace(entry.getKey(), entry.getValue());
            }
            line = BukkitUtilities.getColorText(line);
            sender.sendMessage(line);
        }
    }


    public static void logColorText(@NonNull List<String> message, @NonNull CommandSender sender) {
        if (message.isEmpty()) return;
        message.forEach(line -> {
            line = BukkitUtilities.getColorText(line);
            sender.sendMessage(line);
        });

    }

    public static void logToSender(@NonNull String message, @NonNull CommandSender sender, @NonNull ChatColor chatColor, Object... args) {
        message = String.format(message, args);
        sender.sendMessage(chatColor + PLUGIN_NAME + message);
    }

    public static void logFine(@NonNull String message, @NonNull CommandSender sender, Object... args) {
        logToSender(message, sender, ChatColor.GREEN, args);
    }

    public static void logError(@NonNull String message) {
        logToSender(message, CONSOLE, ChatColor.RED);
    }


}
