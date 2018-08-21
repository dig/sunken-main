package com.sk89q.minecraft.util.commands.cooldowns;

import lombok.Getter;

public class ActiveCooldown {

    @Getter
    private final String[] commands;
    @Getter
    private final long cooldownStartAt;

    public ActiveCooldown(String[] commands, long cooldownStartAt) {
        this.commands = commands;
        this.cooldownStartAt = cooldownStartAt;
    }
}
