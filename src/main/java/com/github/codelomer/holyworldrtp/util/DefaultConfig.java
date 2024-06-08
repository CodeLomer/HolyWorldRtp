package com.github.codelomer.holyworldrtp.util;

import lombok.NonNull;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class DefaultConfig {
    private final JavaPlugin plugin;
    private final String configName;
    private FileConfiguration config;
    private File file;

    public DefaultConfig(@NonNull JavaPlugin plugin, @NonNull String configName) {

        this.plugin = plugin;
        this.configName = configName;
    }

    public void saveDefaultConfig() {
        file = new File(plugin.getDataFolder(), configName);
        if (!file.exists()) loadDefault();
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        if (file == null || !file.exists()) loadDefault();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDefault() {
        plugin.getDataFolder().mkdirs();
        plugin.saveResource(configName, true);
    }
}
