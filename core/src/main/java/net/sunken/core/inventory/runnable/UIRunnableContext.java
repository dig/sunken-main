package net.sunken.core.inventory.runnable;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class UIRunnableContext {

    @Getter
    private final Player observer;
    @Getter
    @Setter
    private boolean cancelled = true;

    public UIRunnableContext(Player observer) {
        this.observer = observer;
    }
}
