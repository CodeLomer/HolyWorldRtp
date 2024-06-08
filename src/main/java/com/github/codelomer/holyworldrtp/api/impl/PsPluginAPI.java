package com.github.codelomer.holyworldrtp.api.impl;

import com.github.codelomer.holyworldrtp.api.SoftDependPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.espi.protectionstones.PSRegion;
import dev.espi.protectionstones.ProtectionStones;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PsPluginAPI implements SoftDependPlugin {
    public static final String NOT_FOUND = "Режим поиска по приватным блокам включен. Для работы команды '/rtp base' нужен protectionStones";
    private boolean enabled;

    @Override
    public void loadPlugin() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ProtectionStones");
        if (plugin == null || !plugin.isEnabled() || !(plugin instanceof ProtectionStones)) return;
        enabled = true;
    }

    @Override
    public boolean isDisabled() {
        return !enabled;
    }


    public List<Location> filterRegionsAndGetBlockPlaces(@NonNull List<ProtectedRegion> regions, @NonNull World world, @NonNull List<Material> regionMaterialBlocks) {
        List<Location> locations = new ArrayList<>();
        for (ProtectedRegion region : regions) {
            PSRegion psRegion = PSRegion.fromWGRegion(world, region);
            Block block = null;
            if (psRegion != null) block = psRegion.getProtectBlock();
            if (block == null || !regionMaterialBlocks.contains(block.getType())) continue;
            locations.add(block.getLocation());
        }
        return locations;
    }
}
