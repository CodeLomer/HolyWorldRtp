package com.github.codelomer.holyworldrtp.api.impl;

import com.github.codelomer.holyworldrtp.api.SoftDependPlugin;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class WGPluginAPI implements SoftDependPlugin {

    public static final String NOT_FOUND = "Для работы команды '/rtp base' на сервере должны быть установленны worldGuard и worldEdit";
    private boolean enabled;
    private WorldGuard api;


    @Override
    public void loadPlugin() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !plugin.isEnabled() || !(plugin instanceof WorldGuardPlugin)) return;
        enabled = true;
        api = WorldGuard.getInstance();
    }

    @Override
    public boolean isDisabled() {
        return !enabled;
    }

    public List<ProtectedRegion> getAllRegionsFromWorld(@NonNull World world) {
        RegionContainer regionContainer = api.getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(world));
        List<ProtectedRegion> protectedRegions = new ArrayList<>();

        if (regionManager == null) {
            return protectedRegions;
        }
        return new ArrayList<>(regionManager.getRegions().values());
    }

    public Location getRegionCenter(@NonNull ProtectedRegion region, @NonNull World world) {
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        double centerX = (min.getX() + max.getX()) / 2.0;
        double centerY = (min.getY() + max.getY()) / 2.0;
        double centerZ = (min.getZ() + max.getZ()) / 2.0;

        return new Location(world, centerX, centerY, centerZ);
    }
}
