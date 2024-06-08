package com.github.codelomer.holyworldrtp.config.rtp.impl;

import com.github.codelomer.configprotection.api.ConfigChecker;
import com.github.codelomer.configprotection.model.params.impl.ConfigNumberParams;
import com.github.codelomer.configprotection.model.params.impl.ConfigParams;
import com.github.codelomer.holyworldrtp.config.rtp.RtpConfig;
import com.github.codelomer.holyworldrtp.model.RtpGroup;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.model.CustomRtpParams;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import com.github.codelomer.holyworldrtp.util.DefaultConfig;
import lombok.NonNull;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class CustomRtpConfig extends RtpConfig {
    private final Map<String, CustomRtpParams> customRtpParamFromName = new HashMap<>();

    public CustomRtpConfig(@NonNull ConfigChecker configChecker, @NonNull JavaPlugin plugin, @NonNull DefaultConfig config) {
        super(configChecker, plugin, config);
        loadConfig();
    }

    @Override
    public final void loadConfig() {
        customRtpParamFromName.clear();

        ConfigurationSection section = BukkitUtilities.getOrCreateSection(config.getConfig(), "Custom-rtp", true);
        for (String rtpKey : section.getKeys(false)) {
            CustomRtpParams rtpParams = loadCustomRtpParams(BukkitUtilities.getOrCreateSection(section, rtpKey, true), rtpKey);
            if (rtpParams == null) continue;
            customRtpParamFromName.put(rtpParams.name(), rtpParams);
        }
    }


    private CustomRtpParams loadCustomRtpParams(@NonNull ConfigurationSection section, @NonNull String name) {
        Integer centerX = configChecker.checkInt(new ConfigNumberParams<>(section, "center-x"));
        Integer centerY = configChecker.checkInt(new ConfigNumberParams<>(section, "center-z"));
        World world = configChecker.checkWorldByName(new ConfigParams<>(section, "world"));
        if (centerX == null || centerY == null || world == null) return null;
        RtpParams rtpParams = loadRtpParams(section);
        List<RtpGroup> cooldownRtpGroups = loadRtpGroups(BukkitUtilities.getOrCreateSection(section, "cooldown", true));
        return new CustomRtpParams(name.toLowerCase(Locale.ENGLISH), centerX, centerY, rtpParams, cooldownRtpGroups, world.getUID());
    }

    public CustomRtpParams getRtpParamsFromName(@NonNull String name) {
        return customRtpParamFromName.get(name);
    }
}
