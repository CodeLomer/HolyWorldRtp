package com.github.codelomer.holyworldrtp.config;

import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Consumer;

public interface ConfigLoader {
    void loadConfig();

    default void loadSubsection(@NonNull ConfigurationSection section, @NonNull String subsectionName, @NonNull Consumer<ConfigurationSection> loader) {
        ConfigurationSection subsection = BukkitUtilities.getOrCreateSection(section, subsectionName, false);
        loader.accept(subsection);
    }
}
