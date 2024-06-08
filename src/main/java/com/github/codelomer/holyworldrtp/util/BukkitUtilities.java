package com.github.codelomer.holyworldrtp.util;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permissible;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BukkitUtilities {

    private static final Pattern VERSION_PATTERN_FILTER = Pattern.compile("\\(MC: (?<version>\\d+\\.\\d+(\\.\\d+)?)\\)");
    private static String minecraftVersion;

    private BukkitUtilities() {
    }

    public static String getColorText(String text) {
        if (text != null) text = ChatColor.translateAlternateColorCodes('&', text);
        return text;
    }

    public static ConfigurationSection getOrCreateSection(@NonNull ConfigurationSection main, @NonNull String subsectionName, boolean overwrite) {
        if (main.contains(subsectionName)) {
            if (!main.isConfigurationSection(subsectionName)) {
                if (overwrite) return main.createSection(subsectionName);
                throw new IllegalArgumentException("Subsection is not a Configuration Section and overwrite is not allowed.");
            }
            return main.getConfigurationSection(subsectionName);
        }
        return main.createSection(subsectionName);
    }

    public static String getFullPath(@NonNull ConfigurationSection section, @NonNull String path) {
        return Objects.requireNonNull(section.getCurrentPath()).replace(".", "\\") + "\\" + path.replace(".", "\\");
    }

    public static boolean hasPermission(@NonNull Permissible permissible, @NonNull String permission) {
        return permissible.getEffectivePermissions().stream().anyMatch(info -> info.getPermission().equalsIgnoreCase(permission));
    }

    public static int getRandomInt(int minimum, int maximum) {
        Random rn = new Random();
        return rn.nextInt(maximum - minimum + 1) + minimum;
    }

    public static String getMinecraftVersion() {
        if (minecraftVersion != null) {
            return minecraftVersion;
        } else {
            String bukkitGetVersionOutput = Bukkit.getVersion();
            Matcher matcher = VERSION_PATTERN_FILTER.matcher(bukkitGetVersionOutput);
            if (matcher.find()) {
                minecraftVersion = matcher.group("version");
                return minecraftVersion;
            } else {
                throw new IllegalArgumentException("Не удалось определить версию Minecraft по Bukkit.getVersion(): " + bukkitGetVersionOutput);
            }
        }
    }
}

