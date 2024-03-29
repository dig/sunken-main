package net.sunken.core.util;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class CooldownUtil {

    private static final HashMap<String, Long> cooldownRegistry = new HashMap<>();

    public static void addCooldown(Player p, String id, Integer howManySeconds) {
        cooldownRegistry.put(p.getUniqueId().toString() + id, System.currentTimeMillis() + (howManySeconds * 1000l));
    }

    public static boolean isInCooldown(Player p, String id) {
        String key = p.getUniqueId().toString() + id;

        if (cooldownRegistry.containsKey(key)) {
            long expire = cooldownRegistry.get(key);
            return (System.currentTimeMillis() <= expire);
        } else {
            return false;
        }
    }

}
