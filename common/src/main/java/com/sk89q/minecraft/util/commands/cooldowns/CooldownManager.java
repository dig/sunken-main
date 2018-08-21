package com.sk89q.minecraft.util.commands.cooldowns;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.util.UUID;

public final class CooldownManager {

    @Getter
    private static final Multimap<UUID, ActiveCooldown> playerCooldowns = ArrayListMultimap.create();

    private CooldownManager() {
    }
}
