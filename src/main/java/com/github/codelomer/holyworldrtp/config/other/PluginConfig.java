package com.github.codelomer.holyworldrtp.config.other;

import com.github.codelomer.configprotection.api.ConfigChecker;
import com.github.codelomer.holyworldrtp.config.ConfigLoader;
import com.github.codelomer.holyworldrtp.config.ConfigReloader;
import com.github.codelomer.holyworldrtp.util.DefaultConfig;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginConfig implements ConfigLoader, ConfigReloader {

    protected final ConfigChecker configChecker;
    protected final DefaultConfig configFile;
    protected final JavaPlugin plugin;

    protected PluginConfig(@NonNull String configName, @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        configChecker = new ConfigChecker(plugin, configName);
        configFile = new DefaultConfig(plugin, configName);
        configFile.saveDefaultConfig();
    }

    protected PluginConfig(@NonNull String configName, @NonNull ConfigChecker configChecker, @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.configChecker = configChecker;
        this.configFile = new DefaultConfig(plugin, configName);
        this.configFile.saveDefaultConfig();

    }

    @Override
    public void reloadConfig() {
        configFile.reloadConfig();
        loadConfig();
    }
}
