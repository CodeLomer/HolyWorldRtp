package com.github.codelomer.holyworldrtp.config.other.impl;

import com.github.codelomer.holyworldrtp.config.other.PluginConfig;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MessagesConfig extends PluginConfig {

    private List<String> cooldownText;
    private List<String> worldListText;
    private List<String> noPermissionText;
    private List<String> errorArgumentsText;
    private List<String> notFoundLocationText;
    private List<String> teleportedText;
    private List<String> somethingWrong;
    private List<String> firstCooldown;

    private List<String> rtpDelayText;
    private List<String> rtpMoveText;
    private List<String> notEnoughPlayerInServerText;
    private List<String> helpText;

    public MessagesConfig(@NonNull JavaPlugin plugin) {
        super("messages.yml", plugin);
        loadConfig();
    }


    @Override
    public final void loadConfig() {
        ConfigurationSection section = configFile.getConfig();
        loadSubsection(section, "General", this::loadGeneralSection);
        loadSubsection(section, "Rtp", this::loadRtpSection);
        loadSubsection(section, "Player-rtp", this::loadPlayerRtpSection);
    }


    private void loadGeneralSection(@NonNull ConfigurationSection section) {
        cooldownText = section.getStringList("cooldown");
        worldListText = section.getStringList("world-list");
        noPermissionText = section.getStringList("no-permission");
        errorArgumentsText = section.getStringList("error-arguments");
        notFoundLocationText = section.getStringList("not-found-location");
        teleportedText = section.getStringList("teleported");
        helpText = section.getStringList("help");
        somethingWrong = section.getStringList("something-wrong");
        firstCooldown = section.getStringList("first-cooldown");
    }

    private void loadRtpSection(@NonNull ConfigurationSection section) {
        rtpDelayText = section.getStringList("delay");
        rtpMoveText = section.getStringList("moving");
    }

    private void loadPlayerRtpSection(@NonNull ConfigurationSection section) {
        notEnoughPlayerInServerText = section.getStringList("not-enough-players-in-server");
    }

    public List<String> getCooldownText() {
        return cooldownText;
    }

    public List<String> getWorldListText() {
        return worldListText;
    }

    public List<String> getNoPermissionText() {
        return noPermissionText;
    }

    public List<String> getErrorArgumentsText() {
        return errorArgumentsText;
    }

    public List<String> getNotFoundLocationText() {
        return notFoundLocationText;
    }

    public List<String> getTeleportedText() {
        return teleportedText;
    }

    public List<String> getRtpDelayText() {
        return rtpDelayText;
    }

    public List<String> getNotEnoughPlayerInServerText() {
        return notEnoughPlayerInServerText;
    }

    public List<String> getHelpText() {
        return helpText;
    }

    public List<String> getSomethingWrong() {
        return somethingWrong;
    }

    public List<String> getRtpMoveText() {
        return rtpMoveText;
    }

    public List<String> getFirstCooldown() {
        return firstCooldown;
    }
}

