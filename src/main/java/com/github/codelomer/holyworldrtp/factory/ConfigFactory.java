package com.github.codelomer.holyworldrtp.factory;

import com.github.codelomer.configprotection.api.ConfigChecker;
import com.github.codelomer.holyworldrtp.config.other.impl.EffectsConfig;
import com.github.codelomer.holyworldrtp.config.other.impl.GeneralConfig;
import com.github.codelomer.holyworldrtp.config.other.impl.MessagesConfig;
import com.github.codelomer.holyworldrtp.config.other.impl.TitlesConfig;
import com.github.codelomer.holyworldrtp.config.rtp.impl.BaseRtpConfig;
import com.github.codelomer.holyworldrtp.config.rtp.impl.CustomRtpConfig;
import com.github.codelomer.holyworldrtp.config.rtp.impl.PlayerRtpConfig;
import com.github.codelomer.holyworldrtp.config.rtp.impl.StandardRtpConfig;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigFactory {

    private final MessagesConfig messagesConfig;
    private final TitlesConfig titlesConfig;
    private final EffectsConfig effectsConfig;
    private final StandardRtpConfig standardRtpConfig;
    private final CustomRtpConfig customRtpConfig;
    private final PlayerRtpConfig playerRtpConfig;
    private final BaseRtpConfig baseRtpConfig;
    private final GeneralConfig generalConfig;

    public ConfigFactory(@NonNull JavaPlugin plugin) {
        ConfigChecker configChecker = new ConfigChecker(plugin, "config.yml");

        messagesConfig = new MessagesConfig(plugin);
        titlesConfig = new TitlesConfig(plugin);
        effectsConfig = new EffectsConfig(plugin);

        generalConfig = new GeneralConfig(configChecker, plugin);
        standardRtpConfig = new StandardRtpConfig(configChecker, plugin, generalConfig.getConfigFile());
        customRtpConfig = new CustomRtpConfig(configChecker, plugin, generalConfig.getConfigFile());
        playerRtpConfig = new PlayerRtpConfig(configChecker, plugin, generalConfig.getConfigFile());
        baseRtpConfig = new BaseRtpConfig(configChecker, plugin, generalConfig.getConfigFile());
    }


    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public TitlesConfig getTitlesConfig() {
        return titlesConfig;
    }

    public EffectsConfig getEffectsConfig() {
        return effectsConfig;
    }

    public StandardRtpConfig getStandardRtpConfig() {
        return standardRtpConfig;
    }

    public CustomRtpConfig getCustomRtpConfig() {
        return customRtpConfig;
    }

    public PlayerRtpConfig getPlayerRtpConfig() {
        return playerRtpConfig;
    }

    public BaseRtpConfig getBaseRtpConfig() {
        return baseRtpConfig;
    }

    public GeneralConfig getGeneralConfig() {
        return generalConfig;
    }
}
