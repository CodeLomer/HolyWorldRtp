package com.github.codelomer.holyworldrtp.config.other.impl;

import com.github.codelomer.configprotection.api.ConfigChecker;
import com.github.codelomer.holyworldrtp.config.other.PluginConfig;
import com.github.codelomer.holyworldrtp.util.DefaultConfig;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class GeneralConfig extends PluginConfig {
    private boolean checkUpdates;

    public GeneralConfig(@NonNull ConfigChecker configChecker, @NonNull JavaPlugin plugin) {
        super("config.yml", configChecker, plugin);
    }


    @Override
    public final void loadConfig() {
        ConfigurationSection section = configFile.getConfig();
        checkUpdates = section.getBoolean("Check-update");

    }

    public boolean isCheckUpdates() {
        return checkUpdates;
    }

    public DefaultConfig getConfigFile() {
        return configFile;
    }
}
