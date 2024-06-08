package com.github.codelomer.holyworldrtp.rtp.specific.impl;

import com.github.codelomer.holyworldrtp.api.impl.PsPluginAPI;
import com.github.codelomer.holyworldrtp.api.impl.WGPluginAPI;
import com.github.codelomer.holyworldrtp.config.rtp.impl.BaseRtpConfig;
import com.github.codelomer.holyworldrtp.config.other.impl.EffectsConfig;
import com.github.codelomer.holyworldrtp.factory.APIFactory;
import com.github.codelomer.holyworldrtp.factory.ConfigFactory;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.rtp.specific.SpecificRtp;
import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BaseRtp extends SpecificRtp {
    private final BaseRtpConfig config;
    private final WGPluginAPI guardAPI;
    private final PsPluginAPI stonesAPI;
    private final EffectsConfig effectsConfig;

    public BaseRtp(@NonNull ConfigFactory configFactory, @NonNull UUID playerUUID, @NonNull APIFactory apiFactory, @NonNull JavaPlugin plugin) {
        super(configFactory.getMessagesConfig(), playerUUID, plugin);
        this.config = configFactory.getBaseRtpConfig();
        this.guardAPI = apiFactory.getWgPluginAPI();
        this.stonesAPI = apiFactory.getPsPluginAPI();
        this.effectsConfig = configFactory.getEffectsConfig();
    }

    @Override
    protected String getCooldownKey() {
        return "BASE_RTP_" + playerUUID.toString();
    }

    @Override
    protected String getPermissionFormat() {
        return "holy.base.rtp";
    }

    @Override
    public void findLocationAndTeleport() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            RtpParams rtpParams = config.getRtpParams();

            List<Location> locations = getRegionCenterLocations(rtpParams);
            Location center = getRandomElement(locations);
            if (center == null) {
                BukkitLogger.logColorText(messagesConfig.getSomethingWrong(), player);
                return;
            }
            World world = center.getWorld();
            if (world == null) {
                BukkitLogger.logColorText(messagesConfig.getSomethingWrong(), player);
                return;
            }
            Location location = findRandomLocation(rtpParams, world, center.getBlockX(), center.getBlockZ());
            if (location == null) {
                BukkitLogger.logColorText(messagesConfig.getNotFoundLocationText(), player);
                return;
            }
            Bukkit.getScheduler().runTask(plugin, () -> teleport(rtpParams.invulnerability(), location, effectsConfig.getBaseRtpEffectList(), player));
        });
    }

    @Override
    protected long getCooldown() {
        return config.getCooldown();
    }

    public boolean dependenciesEnabled() {
        boolean isEnabled = true;
        if (guardAPI.isDisabled()) {
            BukkitLogger.logError(WGPluginAPI.NOT_FOUND);
            isEnabled = false;
        }
        if (config.isProtectionStonesEnabled() && stonesAPI.isDisabled()) {
            BukkitLogger.logError(PsPluginAPI.NOT_FOUND);
            isEnabled = false;
        }
        return isEnabled;
    }

    private List<Location> getRegionCenterLocations(@NonNull RtpParams rtpParams) {
        List<Location> regionCenterLocations = new ArrayList<>();
        for (UUID worldUUID : rtpParams.worldList()) {
            World world = Bukkit.getWorld(worldUUID);
            if (world == null) continue;

            List<ProtectedRegion> regions = new ArrayList<>(guardAPI.getAllRegionsFromWorld(world));
            if (config.isProtectionStonesEnabled()) {
                regionCenterLocations.addAll(stonesAPI.filterRegionsAndGetBlockPlaces(regions, world, config.getProtectionStoneBlockMaterials()));
                continue;
            }
            for (ProtectedRegion region : regions) {
                Location center = guardAPI.getRegionCenter(region, world);
                regionCenterLocations.add(center);
            }
        }
        return regionCenterLocations;
    }


    @Override
    protected String getFirstCooldownKey() {
        return BaseRtpConfig.FIRST_COOLDOWN_KEY;
    }
}
