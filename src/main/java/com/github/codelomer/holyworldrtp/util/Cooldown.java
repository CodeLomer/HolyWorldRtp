package com.github.codelomer.holyworldrtp.util;

import lombok.NonNull;

import java.util.HashMap;

public final class Cooldown {
    private static final HashMap<String, Long> cooldownMap = new HashMap<>();

    private Cooldown() {
    }


    public static void setCooldown(@NonNull String cmd, long second) {
        cooldownMap.put(cmd, System.currentTimeMillis() + second * 1000L);
    }

    public static long isHasCooldown(@NonNull String cmd) {
        if (cooldownMap.containsKey(cmd) && cooldownMap.get(cmd) > System.currentTimeMillis()) {
            return (cooldownMap.get(cmd) - System.currentTimeMillis()) / 1000L;
        }
        return -1;
    }
}
