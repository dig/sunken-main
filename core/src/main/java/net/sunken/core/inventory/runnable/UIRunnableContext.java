package net.sunken.core.inventory.runnable;

import lombok.Getter;
import org.bukkit.entity.Player;

public class UIRunnableContext {

    @Getter
    private final Player observer;

    public UIRunnableContext(Player observer) {
        this.observer = observer;
    }
}
