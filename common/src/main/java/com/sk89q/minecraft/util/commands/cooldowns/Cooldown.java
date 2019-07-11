package com.sk89q.minecraft.util.commands.cooldowns;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

/** A command can have a cooldown between executions. */
@Retention(RetentionPolicy.RUNTIME)
public @interface Cooldown {

    /**
     * The commands a cooldown must match at execution for it to incur
     * this specific cooldown.
     *
     * @return an array of commands
     */
    String[] commands();

    /**
     * The value of the cooldown.
     *
     * @return the period of time a cooldown is active.
     */
    int value();

    /**
     * The time unit of the cooldown value.
     *
     * @return the {@link TimeUnit} of the cooldown
     */
    TimeUnit unit();
}
