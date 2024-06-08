package com.github.codelomer.holyworldrtp.rtp.primitive.impl;

import com.github.codelomer.holyworldrtp.config.rtp.impl.StandardRtpConfig;
import com.github.codelomer.holyworldrtp.config.other.impl.TitlesConfig;
import com.github.codelomer.holyworldrtp.factory.ConfigFactory;
import com.github.codelomer.holyworldrtp.model.RtpGroup;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.model.TextTitle;
import com.github.codelomer.holyworldrtp.rtp.primitive.PrimitiveRtp;
import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StandardRtp extends PrimitiveRtp {
    private final StandardRtpConfig config;
    private final List<PotionEffect> potionEffects;
    private final TitlesConfig titlesConfig;
    private BukkitTask teleportTask;
    private BukkitTask moveCheckTask;

    public StandardRtp(@NonNull ConfigFactory configFactory, @NonNull UUID playerUUID, @NonNull JavaPlugin plugin) {
        super(configFactory.getMessagesConfig(), playerUUID, plugin);
        this.config = configFactory.getStandardRtpConfig();
        this.potionEffects = configFactory.getEffectsConfig().getRtpEffectList();
        this.titlesConfig = configFactory.getTitlesConfig();
    }

    @Override
    protected List<RtpGroup> getRtpGroups() {
        return config.getDelayRtpGroups();
    }

    @Override
    protected String getPermissionFormat() {
        return "holyrtp.standard";
    }

    @Override
    protected List<UUID> getWorldList() {
        return config.getRtpParams().worldList();
    }

    @Override
    protected long getCooldown() {
        return config.getCooldown();
    }

    @Override
    public void findLocationAndTeleport() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            World world = Bukkit.getWorld(config.getWorldUUID());
            if (world == null) return;
            RtpParams rtpParams = config.getRtpParams();
            Location location = findRandomLocation(rtpParams, world, 0, 0);
            if (location == null) {
                BukkitLogger.logColorText(messagesConfig.getNotFoundLocationText(), player);
                return;
            }
            if (value < 1 || (config.getFirstWorld() != null && config.getFirstWorld().equals(player.getWorld().getUID()))) {
                Bukkit.getScheduler().runTask(plugin, () -> teleport(rtpParams.invulnerability(), location, potionEffects, player));
                return;
            }
            BukkitLogger.logColorText(messagesConfig.getRtpDelayText(), player, Map.of("{SEC}", String.valueOf(value)));
            TextTitle textTitle = titlesConfig.getRtpDelayTextTitle();
            if (textTitle.enabled())
                player.sendTitle(textTitle.text(), textTitle.subtext().replace("{SEC}", String.valueOf(value)), textTitle.fadeIn(), textTitle.state(), textTitle.fadeOut());
            teleportTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!moveCheckTask.isCancelled()) moveCheckTask.cancel();
                teleport(rtpParams.invulnerability(), location, potionEffects, player);
            }, value * 20L);
            if (config.isCheckMove()) checkMove(player);
        });

    }

    @Override
    protected String getCooldownKey() {
        return "STANDARD_RTP_" + playerUUID.toString();
    }

    private void checkMove(@NonNull Player player) {
        Location firstLocation = player.getLocation();
        moveCheckTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Location nextLocation = player.getLocation();
            if (!locationEquals(firstLocation, nextLocation)) {
                teleportTask.cancel();
                BukkitLogger.logColorText(messagesConfig.getRtpMoveText(), player);
                moveCheckTask.cancel();
            }
        }, 0, 20);
    }

    private boolean locationEquals(@NonNull Location l1, @NonNull Location l2) {
        String l1Id = l1.getBlockX() + "-" + l1.getBlockY() + "-" + l1.getBlockZ();
        String l2Id = l2.getBlockX() + "-" + l2.getBlockY() + "-" + l2.getBlockZ();
        return l1Id.equalsIgnoreCase(l2Id);
    }
}
