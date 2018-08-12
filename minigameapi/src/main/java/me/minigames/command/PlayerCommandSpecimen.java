package me.minigames.command;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommandSpecimen extends CommandSpecimen {
    @Getter
    private Player player;

    public PlayerCommandSpecimen(CommandSender sender, String[] args) {
        super(sender, args);
        Preconditions.checkState(getSender() instanceof Player, "CommandSender must be an instance of Player");
        this.player = (Player) sender;
    }
}
