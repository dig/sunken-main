package net.sunken.core.inventory;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class RunnableAction extends Action {

    @Getter
    @Setter
    private Player player;

    public RunnableAction(PageContainer container, ActionType actionType) {
        super(container, actionType);
    }
}
