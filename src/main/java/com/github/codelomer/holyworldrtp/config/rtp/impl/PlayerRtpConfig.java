package com.github.codelomer.holyworldrtp.config.rtp.impl;

import com.github.codelomer.configprotection.api.ConfigChecker;
import com.github.codelomer.configprotection.model.params.impl.ConfigNumberParams;
import com.github.codelomer.holyworldrtp.config.rtp.RtpConfig;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import com.github.codelomer.holyworldrtp.util.Cooldown;
import com.github.codelomer.holyworldrtp.util.DefaultConfig;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerRtpConfig extends RtpConfig {

    public static final String FIRST_COOLDOWN_KEY = "BASE_RTP";
    private int minPlayersInServer;
    private RtpParams rtpParams;
    private long cooldown;

    public PlayerRtpConfig(@NonNull ConfigChecker configChecker, @NonNull JavaPlugin plugin, @NonNull DefaultConfig config) {
        super(configChecker, plugin, config);
        loadConfig();
    }

    @Override
    public final void loadConfig() {
        ConfigurationSection section = BukkitUtilities.getOrCreateSection(config.getConfig(), "Player-rtp", true);
        minPlayersInServer = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "min-players-in-server").setMinLimit(2).setDef(3));
        rtpParams = loadRtpParams(section);
        cooldown = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "cooldown").setMinLimit(0).setDef(60));
        long firstCooldown = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "first-cooldown").setDef(86400).setMinLimit(0));
        Cooldown.setCooldown(FIRST_COOLDOWN_KEY, firstCooldown);
    }


    public int getMinPlayersInServer() {
        return minPlayersInServer;
    }

    public RtpParams getRtpParams() {
        return rtpParams;
    }

    public long getCooldown() {
        return cooldown;
    }

}
