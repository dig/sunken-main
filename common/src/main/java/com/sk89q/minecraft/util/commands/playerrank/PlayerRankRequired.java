package com.sk89q.minecraft.util.commands.playerrank;

import net.sunken.common.player.PlayerRank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerRankRequired {

    /**
     * The PlayerRank the player must have in order to execute the command.
     *
     * @return the PlayerRank
     */
    PlayerRank value();
}
