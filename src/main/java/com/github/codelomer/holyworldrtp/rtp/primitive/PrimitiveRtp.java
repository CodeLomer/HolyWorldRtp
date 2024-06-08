package com.github.codelomer.holyworldrtp.rtp.primitive;

import com.github.codelomer.holyworldrtp.config.other.impl.MessagesConfig;
import com.github.codelomer.holyworldrtp.model.RtpGroup;
import com.github.codelomer.holyworldrtp.rtp.Rtp;
import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public abstract class PrimitiveRtp extends Rtp {
    protected long value;

    protected PrimitiveRtp(@NonNull MessagesConfig messagesConfig, @NonNull UUID playerUUID, @NonNull JavaPlugin plugin) {
        super(messagesConfig, playerUUID, plugin);
    }


    @Override
    public boolean hasPermission() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return false;
        for (RtpGroup rtpGroup : getRtpGroups()) {
            String permission = getPermissionFormat() + "." + rtpGroup.groupName().toLowerCase(Locale.ENGLISH);
            if (BukkitUtilities.hasPermission(player, permission)) {
                value = rtpGroup.value();
                return true;
            }
        }
        BukkitLogger.logColorText(messagesConfig.getNoPermissionText(), player);
        return false;
    }

    @Override
    public boolean canTeleport() {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return false;
        if (hasCooldown(getCooldown(), player)) return false;
        if (getWorldList().contains(player.getWorld().getUID())) {
            BukkitLogger.logColorText(messagesConfig.getWorldListText(), player, Map.of("{WORLD_NAME}", player.getWorld().getName()));
            return false;
        }
        return true;
    }

    protected abstract List<RtpGroup> getRtpGroups();

    protected abstract List<UUID> getWorldList();
}
