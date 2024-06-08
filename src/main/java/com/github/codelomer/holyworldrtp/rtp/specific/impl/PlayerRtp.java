package com.github.codelomer.holyworldrtp.rtp.specific.impl;

import com.github.codelomer.holyworldrtp.config.other.impl.EffectsConfig;
import com.github.codelomer.holyworldrtp.config.rtp.impl.PlayerRtpConfig;
import com.github.codelomer.holyworldrtp.config.other.impl.TitlesConfig;
import com.github.codelomer.holyworldrtp.factory.ConfigFactory;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.model.TextTitle;
import com.github.codelomer.holyworldrtp.rtp.specific.SpecificRtp;
import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerRtp extends SpecificRtp {
    private final PlayerRtpConfig config;
    private final EffectsConfig effectsConfig;
    private final TitlesConfig titlesConfig;

    public PlayerRtp(@NonNull ConfigFactory configFactory, @NonNull UUID playerUUID, @NonNull JavaPlugin plugin) {
        super(configFactory.getMessagesConfig(), playerUUID, plugin);
        this.config = configFactory.getPlayerRtpConfig();
        this.effectsConfig = configFactory.getEffectsConfig();
        this.titlesConfig = configFactory.getTitlesConfig();
    }

    @Override
    protected String getCooldownKey() {
        return "PLAYER_RTP_" + playerUUID.toString();
    }

    @Override
    protected String getPermissionFormat() {
        return "holy.player.rtp";
    }

    @Override
    public void findLocationAndTeleport() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) return;
            List<Player> players = getAllRequiredPlayers();
            players.remove(player);
            Player target = getRandomElement(players);
            if (target == null) {
                BukkitLogger.logColorText(messagesConfig.getSomethingWrong(), player);
                return;
            }
            RtpParams rtpParams = config.getRtpParams();
            Location location = findRandomLocation(rtpParams, target.getWorld(), target.getLocation().getBlockX(), target.getLocation().getBlockZ());
            if (location == null) {
                BukkitLogger.logColorText(messagesConfig.getNotFoundLocationText(), player);
                return;
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                teleport(rtpParams.invulnerability(), location, effectsConfig.getPlayerRtpEffectList(), player);
                TextTitle warning = titlesConfig.getPlayerRtpWarningTextTitle();
                if (warning.enabled())
                    player.sendTitle(warning.text(), warning.subtext(), warning.fadeIn(), warning.state(), warning.fadeOut());
            });

        });
    }

    @Override
    protected long getCooldown() {
        return config.getCooldown();
    }

    public boolean playerExistsInServer() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return false;
        if (Bukkit.getOnlinePlayers().size() < config.getMinPlayersInServer()) {
            BukkitLogger.logColorText(messagesConfig.getNotEnoughPlayerInServerText(), player);
            return false;
        }
        return true;
    }

    private List<Player> getAllRequiredPlayers() {
        List<Player> players = new ArrayList<>();
        for (UUID worldUUID : config.getRtpParams().worldList()) {
            World world = Bukkit.getWorld(worldUUID);
            if (world == null) continue;
            players.addAll(world.getPlayers());
        }
        return players;
    }

    @Override
    protected String getFirstCooldownKey() {
        return PlayerRtpConfig.FIRST_COOLDOWN_KEY;
    }
}
