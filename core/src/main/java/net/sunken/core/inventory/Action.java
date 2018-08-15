package net.sunken.core.inventory;

import lombok.Getter;

public class Action {

    private final PageContainer container;
    @Getter
    private final ActionType actionType;

    public Action(PageContainer container, ActionType actionType) {
        this.container = container;
        this.actionType = actionType;
    }
}
