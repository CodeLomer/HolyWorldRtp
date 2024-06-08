package com.github.codelomer.holyworldrtp.rtp.specific;

import com.github.codelomer.holyworldrtp.config.other.impl.MessagesConfig;
import com.github.codelomer.holyworldrtp.rtp.Rtp;
import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import com.github.codelomer.holyworldrtp.util.Cooldown;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public abstract class SpecificRtp extends Rtp {

    protected SpecificRtp(@NonNull MessagesConfig messagesConfig, @NonNull UUID playerUUID, @NonNull JavaPlugin plugin) {
        super(messagesConfig, playerUUID, plugin);
    }

    @Override
    public boolean hasPermission() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return false;
        if (BukkitUtilities.hasPermission(player, getPermissionFormat())) {
            return true;
        }
        BukkitLogger.logColorText(messagesConfig.getNoPermissionText(), player);
        return false;
    }

    @Override
    public boolean canTeleport() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return false;
        long second = Cooldown.isHasCooldown(getFirstCooldownKey());
        if (second > 0) {
            BukkitLogger.logColorText(messagesConfig.getFirstCooldown(), player);
            return false;
        }
        return !hasCooldown(getCooldown(), player);
    }

    protected <E> E getRandomElement(@NonNull List<E> list) {
        if (list.isEmpty()) return null;
        return list.get(BukkitUtilities.getRandomInt(0, list.size() - 1));
    }

    protected abstract String getFirstCooldownKey();
}

