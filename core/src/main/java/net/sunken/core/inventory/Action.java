package net.sunken.core.inventory;

import lombok.Getter;

public class Action {

    @Getter
    private final PageContainer pageContainer;
    @Getter
    private final ActionType actionType;

    public Action(PageContainer pageContainer, ActionType actionType) {
        this.pageContainer = pageContainer;
        this.actionType = actionType;
    }
}
