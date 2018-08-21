package com.sk89q.minecraft.util.commands.cooldowns;

import com.sk89q.minecraft.util.commands.CommandException;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Thrown when a player is on a cooldown of a command annotated with
 * {@link Cooldown}
 */
public class OnCooldownException extends CommandException {

    @Getter
    private final int cooldownLength;
    @Getter
    private final long timeLeft;
    @Getter
    private final TimeUnit unit;

    public OnCooldownException(int cooldownLength, long timeLeft, TimeUnit unit) {
        this.cooldownLength = cooldownLength;
        this.timeLeft = timeLeft;
        this.unit = unit;
    }
}
