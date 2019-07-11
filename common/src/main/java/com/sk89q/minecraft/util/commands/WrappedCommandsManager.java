package com.sk89q.minecraft.util.commands;

public abstract class WrappedCommandsManager extends CommandsManager<WrappedCommandSender> {

    @Override
    public boolean hasPermission(WrappedCommandSender player, String perm) {
        return player.hasPermission(perm);
    }
}
