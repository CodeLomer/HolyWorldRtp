package com.github.codelomer.holyworldrtp.rtp;

import com.github.codelomer.holyworldrtp.config.other.impl.MessagesConfig;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import com.github.codelomer.holyworldrtp.util.Cooldown;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Rtp {
    protected final MessagesConfig messagesConfig;
    protected final UUID playerUUID;
    protected final JavaPlugin plugin;

    protected Rtp(@NonNull MessagesConfig messagesConfig, @NonNull UUID playerUUID, @NonNull JavaPlugin plugin) {
        this.messagesConfig = messagesConfig;
        this.playerUUID = playerUUID;
        this.plugin = plugin;
    }

    protected Location findRandomLocation(@NonNull RtpParams rtpParams, @NonNull World world, int xCenter, int zCenter) {
        int innerRadius = rtpParams.minRadius();
        int outerRadius = rtpParams.maxRadius();
        int tryFindLocationCount = rtpParams.tryFindCount();
        List<String> trushCoordinates = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Location location = null;

        for (int tryFind = 0; tryFind < tryFindLocationCount; tryFind++) {

            double r = innerRadius + (outerRadius - innerRadius) * random.nextDouble();
            double theta = random.nextDouble() * 2 * Math.PI;
            int x = (int) (xCenter + r * Math.cos(theta));
            int z = (int) (zCenter + r * Math.sin(theta));
            String coordinateId = x + "," + z;
            if (trushCoordinates.contains(coordinateId)) {
                System.out.println(coordinateId);
                continue;
            }
            int y = Objects.requireNonNull(world).getHighestBlockYAt(x, z);
            Location randomLocation = new Location(world, x, y, z);
            Chunk chunk = randomLocation.getChunk();
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!chunk.isLoaded()) chunk.load();
            });
            if (rtpParams.blockList().contains(randomLocation.getBlock().getType())) {
                trushCoordinates.add(coordinateId);
                continue;
            }
            location = randomLocation;
            break;
        }
        return location;
    }

    protected boolean hasCooldown(long cooldown, @NonNull Player player) {
        long seconds = Cooldown.isHasCooldown(getCooldownKey());
        if (seconds > 0) {
            BukkitLogger.logColorText(messagesConfig.getCooldownText(), player, Map.of("{SEC}", String.valueOf(seconds)));
            return true;
        }
        Cooldown.setCooldown(getCooldownKey(), cooldown);
        return false;
    }

    protected void teleport(int invulnerability, @NonNull Location location, @NonNull List<PotionEffect> potionEffects, @NonNull Player player) {
        location.setY(location.getBlockY() + 1);
        player.teleport(location);
        player.setNoDamageTicks(invulnerability * 20);
        player.addPotionEffects(potionEffects);
        BukkitLogger.logColorText(messagesConfig.getTeleportedText(), player, Map.of(
                "{X}", String.valueOf(location.getBlockX()),
                "{Y}", String.valueOf(location.getBlockY()),
                "{Z}", String.valueOf(location.getBlockZ())
        ));

    }

    protected abstract String getCooldownKey();

    protected abstract String getPermissionFormat();

    public abstract boolean hasPermission();

    public abstract boolean canTeleport();

    public abstract void findLocationAndTeleport();

    protected abstract long getCooldown();
}
