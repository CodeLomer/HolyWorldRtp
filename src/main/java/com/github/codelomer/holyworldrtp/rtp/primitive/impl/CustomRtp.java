package com.github.codelomer.holyworldrtp.rtp.primitive.impl;

import com.github.codelomer.holyworldrtp.config.rtp.impl.CustomRtpConfig;
import com.github.codelomer.holyworldrtp.config.other.impl.EffectsConfig;
import com.github.codelomer.holyworldrtp.factory.ConfigFactory;
import com.github.codelomer.holyworldrtp.model.CustomRtpParams;
import com.github.codelomer.holyworldrtp.model.RtpGroup;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.rtp.primitive.PrimitiveRtp;
import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CustomRtp extends PrimitiveRtp {
    private final CustomRtpConfig config;
    private CustomRtpParams customRtpParams;
    private final EffectsConfig effectsConfig;

    public CustomRtp(@NonNull ConfigFactory configFactory, @NonNull UUID playerUUID, @NonNull JavaPlugin plugin) {
        super(configFactory.getMessagesConfig(), playerUUID, plugin);
        this.config = configFactory.getCustomRtpConfig();
        this.effectsConfig = configFactory.getEffectsConfig();
    }

    @Override
    protected List<RtpGroup> getRtpGroups() {
        return customRtpParams.cooldownRtpGroups();
    }

    @Override
    protected String getPermissionFormat() {
        return "holyrtp.custom." + customRtpParams.name();
    }

    @Override
    protected List<UUID> getWorldList() {
        return customRtpParams.rtpParams().worldList();
    }

    @Override
    protected long getCooldown() {
        return value;
    }

    @Override
    public void findLocationAndTeleport() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            World world = Bukkit.getWorld(customRtpParams.worldUUID());
            if (world == null) return;
            RtpParams rtpParams = customRtpParams.rtpParams();
            Location location = findRandomLocation(rtpParams, world, customRtpParams.centerX(), customRtpParams.centerZ());
            if (location == null) {
                BukkitLogger.logColorText(messagesConfig.getNotFoundLocationText(), player);
                return;
            }
            Bukkit.getScheduler().runTask(plugin, () -> teleport(rtpParams.invulnerability(), location, effectsConfig.getCustomRtpEffectList(customRtpParams.name()), player));
        });
    }

    @Override
    protected String getCooldownKey() {
        return "CUSTOM_RTP_" + customRtpParams.name() + "_" + playerUUID.toString();
    }

    public boolean isTypeExists(String rtpType) {
        customRtpParams = config.getRtpParamsFromName(rtpType.toLowerCase(Locale.ENGLISH));
        return customRtpParams != null;
    }
}
