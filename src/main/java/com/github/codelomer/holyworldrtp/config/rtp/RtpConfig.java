package com.github.codelomer.holyworldrtp.config.rtp;

import com.github.codelomer.configprotection.api.ConfigChecker;
import com.github.codelomer.configprotection.model.params.impl.ConfigListParams;
import com.github.codelomer.configprotection.model.params.impl.ConfigNumberParams;
import com.github.codelomer.holyworldrtp.config.ConfigLoader;
import com.github.codelomer.holyworldrtp.model.RtpGroup;
import com.github.codelomer.holyworldrtp.model.RtpParams;
import com.github.codelomer.holyworldrtp.util.DefaultConfig;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class RtpConfig implements ConfigLoader {

    protected final ConfigChecker configChecker;
    protected final JavaPlugin plugin;
    protected final DefaultConfig config;

    protected RtpConfig(ConfigChecker configChecker, JavaPlugin plugin, @NonNull DefaultConfig config) {
        this.configChecker = configChecker;
        this.plugin = plugin;
        this.config = config;
    }

    protected List<RtpGroup> loadRtpGroups(@NonNull ConfigurationSection section) {
        List<RtpGroup> rtpGroups = new ArrayList<>();
        for (String groupName : section.getKeys(false)) {
            Integer value = configChecker.checkInt(new ConfigNumberParams<Integer>(section, groupName).setMinLimit(0));
            if (value == null) continue;
            RtpGroup rtpGroup = new RtpGroup(groupName, value);
            if (!rtpGroups.contains(rtpGroup)) {
                rtpGroups.add(rtpGroup);
            }
        }
        return rtpGroups;
    }

    protected RtpParams loadRtpParams(@NonNull ConfigurationSection section) {
        int minRadius = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "min-radius").setMinLimit(0).setDef(500));
        int maxRadius = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "max-radius").setMinLimit(minRadius).setDef(1500));
        int yMax = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "y-max").setDef(120));
        int tryFind = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "try-find").setMinLimit(1).setDef(25));
        int invulnerability = configChecker.checkInt(new ConfigNumberParams<Integer>(section, "invulnerability").setMinLimit(0).setDef(2));

        List<Material> blockList = new ArrayList<>(configChecker.checkMaterialList(new ConfigListParams<Material>(section, "block-list").canBeEmpty(true)));

        List<UUID> worldUuidList = new ArrayList<>();
        List<World> worlds = configChecker.checkWorldListByNames(new ConfigListParams<World>(section, "world-list").canBeEmpty(true));
        if (worlds != null) worldUuidList.addAll(worlds.stream().map(World::getUID).toList());
        return new RtpParams(minRadius, maxRadius, yMax, tryFind, blockList, worldUuidList, invulnerability);
    }
}
