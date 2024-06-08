package com.github.codelomer.holyworldrtp.config.rtp.impl;

import com.github.codelomer.configprotection.api.ConfigChecker;
import com.github.codelomer.configprotection.model.params.impl.ConfigListParams;
import com.github.codelomer.configprotection.model.params.impl.ConfigNumberParams;
import com.github.codelomer.holyworldrtp.config.rtp.RtpConfig;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import com.github.codelomer.holyworldrtp.util.Cooldown;
import com.github.codelomer.holyworldrtp.util.DefaultConfig;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BaseRtpConfig extends RtpConfig {
    public static final String FIRST_COOLDOWN_KEY = "BASE_RTP";
    private long cooldown;
    private boolean protectionStonesEnabled;
    private RtpParams rtpParams;
    private List<Material> protectionStoneBlockMaterials;

    public BaseRtpConfig(@NonNull ConfigChecker configChecker, @NonNull JavaPlugin plugin, @NonNull DefaultConfig config) {
        super(configChecker, plugin, config);
        loadConfig();
    }

    @Override
    public final void loadConfig() {
        ConfigurationSection section = BukkitUtilities.getOrCreateSection(config.getConfig(), "Base-rtp", true);
        cooldown = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "cooldown").setMinLimit(0).setDef(60));
        protectionStonesEnabled = section.getBoolean("protection-stones.enable");
        protectionStoneBlockMaterials = new ArrayList<>(configChecker.checkMaterialList(new ConfigListParams<Material>(section, "protection-stones.blocks").canBeEmpty(true)));
        rtpParams = loadRtpParams(section);
        long firstCooldown = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "first-cooldown").setDef(86400).setMinLimit(0));
        Cooldown.setCooldown(FIRST_COOLDOWN_KEY, firstCooldown);
    }

    public long getCooldown() {
        return cooldown;
    }

    public boolean isProtectionStonesEnabled() {
        return protectionStonesEnabled;
    }

    public List<Material> getProtectionStoneBlockMaterials() {
        return protectionStoneBlockMaterials;
    }

    public RtpParams getRtpParams() {
        return rtpParams;
    }
}
