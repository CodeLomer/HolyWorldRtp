package com.github.codelomer.holyworldrtp.config.other.impl;

import com.github.codelomer.configprotection.logger.ConfigLogger;
import com.github.codelomer.configprotection.model.params.impl.ConfigNumberParams;
import com.github.codelomer.holyworldrtp.config.other.PluginConfig;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;


public class EffectsConfig extends PluginConfig {
    private final ConfigLogger configLogger;

    private final List<PotionEffect> rtpEffectList = new ArrayList<>();
    private final List<PotionEffect> playerRtpEffectList = new ArrayList<>();
    private final List<PotionEffect> baseRtpEffectList = new ArrayList<>();
    private final Multimap<String, PotionEffect> customRtpEffectMap = ArrayListMultimap.create();

    public EffectsConfig(@NonNull JavaPlugin plugin) {
        super("effects.yml", plugin);
        this.configLogger = configChecker.getConfigLogger();
        loadConfig();
    }


    @Override
    public final void loadConfig() {
        rtpEffectList.clear();
        customRtpEffectMap.clear();
        playerRtpEffectList.clear();
        baseRtpEffectList.clear();

        ConfigurationSection section = configFile.getConfig();

        loadSubsection(section, "Rtp", this::loadRtpSection);
        loadSubsection(section, "Custom-rtp", this::loadCustomRtpSection);
        loadSubsection(section, "Player-rtp", this::loadPlayerRtpSection);
        loadSubsection(section, "Base-rtp", this::loadBaseRtpSection);
    }

    private void loadRtpSection(@NonNull ConfigurationSection section) {
        rtpEffectList.addAll(loadPotionEffects(section));

    }

    private void loadCustomRtpSection(@NonNull ConfigurationSection section) {
        for (String rtpTypeKey : section.getKeys(false)) {
            ConfigurationSection rtpTypeSection = BukkitUtilities.getOrCreateSection(section, rtpTypeKey, true);
            customRtpEffectMap.putAll(rtpTypeKey, loadPotionEffects(rtpTypeSection));
        }
    }

    private void loadPlayerRtpSection(@NonNull ConfigurationSection section) {
        playerRtpEffectList.addAll(loadPotionEffects(section));
    }

    private void loadBaseRtpSection(@NonNull ConfigurationSection section) {
        baseRtpEffectList.addAll(loadPotionEffects(section));
    }

    private PotionEffect loadPotionEffect(@NonNull ConfigurationSection section, @NonNull String effectTypeName) {
        PotionEffectType potionEffectType = PotionEffectType.getByName(effectTypeName);
        if (potionEffectType == null) {
            configLogger.log("this type of effect like " + effectTypeName + " does not exist in Minecraft", BukkitUtilities.getFullPath(section, effectTypeName));
            return null;
        }
        Integer level = configChecker.checkInt(new ConfigNumberParams<Integer>(section, effectTypeName + ".level").setMinLimit(1));
        Integer time = configChecker.checkInt(new ConfigNumberParams<Integer>(section, effectTypeName + ".time").setMinLimit(0));
        if (level == null || time == null) return null;
        return new PotionEffect(potionEffectType, time * 20, level - 1);
    }

    private List<PotionEffect> loadPotionEffects(@NonNull ConfigurationSection section) {
        List<PotionEffect> potionEffects = new ArrayList<>();
        for (String effectTypeName : section.getKeys(false)) {
            PotionEffect potionEffect = loadPotionEffect(section, effectTypeName);
            if (potionEffect != null) {
                potionEffects.add(potionEffect);
            }

        }
        return potionEffects;
    }

    public List<PotionEffect> getRtpEffectList() {
        return rtpEffectList;
    }

    public List<PotionEffect> getBaseRtpEffectList() {
        return baseRtpEffectList;
    }

    public List<PotionEffect> getPlayerRtpEffectList() {
        return playerRtpEffectList;
    }

    public List<PotionEffect> getCustomRtpEffectList(@NonNull String rtpType) {
        return new ArrayList<>(customRtpEffectMap.get(rtpType));
    }
}
