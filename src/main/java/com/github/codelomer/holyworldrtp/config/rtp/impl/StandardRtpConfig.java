package com.github.codelomer.holyworldrtp.config.rtp.impl;

import com.github.codelomer.configprotection.api.ConfigChecker;
import com.github.codelomer.configprotection.model.params.impl.ConfigNumberParams;
import com.github.codelomer.configprotection.model.params.impl.ConfigParams;
import com.github.codelomer.configprotection.model.params.impl.ConfigStringParams;
import com.github.codelomer.holyworldrtp.config.rtp.RtpConfig;
import com.github.codelomer.holyworldrtp.model.RtpGroup;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import com.github.codelomer.holyworldrtp.util.DefaultConfig;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StandardRtpConfig extends RtpConfig {
    private long cooldown;
    private RtpParams rtpParams;
    private UUID worldUUID;
    private final List<RtpGroup> delayRtpGroups = new ArrayList<>();
    private UUID firstWorld;
    private boolean checkMove;

    public StandardRtpConfig(@NonNull ConfigChecker configChecker, @NonNull JavaPlugin plugin, @NonNull DefaultConfig config) {
        super(configChecker, plugin, config);
        loadConfig();
    }


    @Override
    public final void loadConfig() {
        delayRtpGroups.clear();
        ConfigurationSection section = BukkitUtilities.getOrCreateSection(config.getConfig(), "Rtp", true);

        cooldown = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "cooldown").setMinLimit(0).setDef(10));
        delayRtpGroups.addAll(loadRtpGroups(BukkitUtilities.getOrCreateSection(section, "delay", true)));
        rtpParams = loadRtpParams(section);
        worldUUID = configChecker.checkWorldByName(new ConfigParams<World>(section, "world").setDef(Bukkit.getWorlds().get(0))).getUID();
        checkMove = section.getBoolean("check-move");
        String worldName = configChecker.checkString(new ConfigStringParams(section, "first-world").canBeEmpty(true));
        if (worldName != null && !worldName.isEmpty()) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                configChecker.getConfigLogger().log(String.format("такого мира как %s не существует на сервере", worldName), BukkitUtilities.getFullPath(section, "First-world"));
                world = Bukkit.getWorlds().get(0);
            }
            firstWorld = world.getUID();
        }
    }

    public long getCooldown() {
        return cooldown;
    }

    public List<RtpGroup> getDelayRtpGroups() {
        return delayRtpGroups;
    }

    public RtpParams getRtpParams() {
        return rtpParams;
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public UUID getFirstWorld() {
        return firstWorld;
    }

    public boolean isCheckMove() {
        return checkMove;
    }
}
