package com.github.codelomer.holyworldrtp.config.other.impl;

import com.github.codelomer.configprotection.model.params.impl.ConfigNumberParams;
import com.github.codelomer.holyworldrtp.config.other.PluginConfig;
import com.github.codelomer.holyworldrtp.model.TextTitle;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class TitlesConfig extends PluginConfig {

    private TextTitle rtpDelayTextTitle;
    private TextTitle playerRtpWarningTextTitle;

    public TitlesConfig(@NonNull JavaPlugin plugin) {
        super("titles.yml", plugin);
        loadConfig();
    }


    @Override
    public final void loadConfig() {
        ConfigurationSection section = configFile.getConfig();
        rtpDelayTextTitle = createTextTitle(section, "Rtp.delay");
        playerRtpWarningTextTitle = createTextTitle(section, "Player-rtp.warning");
    }

    private TextTitle createTextTitle(@NonNull ConfigurationSection section, @NonNull String path) {
        String text = section.getString(path + ".text", "null");
        String subtext = section.getString(path + ".subtext", "null");
        int fadeIn = configChecker.checkInt(new ConfigNumberParams<Integer>(section, path + ".fadeIn").setDef(30));
        int state = configChecker.checkInt(new ConfigNumberParams<Integer>(section, path + ".state").setDef(60));
        int fadeOut = configChecker.checkInt(new ConfigNumberParams<Integer>(section, path + ".fadeOut").setDef(30));
        boolean enable = section.getBoolean("enable");
        return new TextTitle(text, subtext, fadeIn, state, fadeOut, enable);
    }

    public TextTitle getRtpDelayTextTitle() {
        return rtpDelayTextTitle;
    }

    public TextTitle getPlayerRtpWarningTextTitle() {
        return playerRtpWarningTextTitle;
    }
}
