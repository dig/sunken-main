package com.sk89q.minecraft.util.commands.playerrank;

import com.sk89q.minecraft.util.commands.CommandException;
import lombok.Getter;
import net.sunken.common.player.PlayerRank;

/**
 * Thrown when a player does not have a PlayerRank that is required.
 */
public class PlayerNotHasRankException extends CommandException {

    @Getter
    private final PlayerRank required;

    public PlayerNotHasRankException (PlayerRank required) {
        this.required = required;
    }
}
