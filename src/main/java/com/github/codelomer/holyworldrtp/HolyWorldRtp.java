package com.github.codelomer.holyworldrtp;

import com.github.codelomer.holyworldrtp.command.RtpCMD;
import com.github.codelomer.holyworldrtp.factory.APIFactory;
import com.github.codelomer.holyworldrtp.factory.ConfigFactory;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class HolyWorldRtp extends JavaPlugin {
    private ConfigFactory configFactory;
    private static HolyWorldRtp instance;

    @Override
    public void onEnable() {
        instance = this;
        configFactory = new ConfigFactory(this);
        APIFactory apiFactory = new APIFactory();
        new RtpCMD("rtp", this, configFactory, apiFactory);

        Metrics metrics = new Metrics(this, 22168);
        registerUsePluginMetrics(metrics);
        registerMinecraftVersionMetrics(metrics);
        if(configFactory.getGeneralConfig().isCheckUpdates()) new UpdateChecker(this,"CodeLomer","HolyWorldRtp").checkUpdate();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void reloadPlugin() {
        configFactory.getMessagesConfig().reloadConfig();
        configFactory.getTitlesConfig().reloadConfig();
        configFactory.getEffectsConfig().reloadConfig();
        configFactory.getGeneralConfig().reloadConfig();

        configFactory.getStandardRtpConfig().loadConfig();
        configFactory.getPlayerRtpConfig().loadConfig();
        configFactory.getCustomRtpConfig().loadConfig();
        configFactory.getBaseRtpConfig().loadConfig();
    }

    public static HolyWorldRtp getInstance() {
        return instance;
    }

    private void registerUsePluginMetrics(@NonNull Metrics metrics) {
        metrics.addCustomChart(new Metrics.SingleLineChart("servers", () -> 1));
    }

    private void registerMinecraftVersionMetrics(@NonNull Metrics metrics) {
        metrics.addCustomChart(new Metrics.DrilldownPie("minecraftVersion", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String minecraftVersion = BukkitUtilities.getMinecraftVersion();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(minecraftVersion, 1);
            map.put("Minecraft server version " + minecraftVersion, entry);
            return map;
        }));

    }
}
