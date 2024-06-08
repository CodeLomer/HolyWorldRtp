package com.github.codelomer.holyworldrtp.model;

import lombok.NonNull;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

public record RtpParams(int minRadius, int maxRadius, int yMax,int tryFindCount, @NonNull List<Material> blockList, @NonNull List<UUID> worldList, int invulnerability) {
}
