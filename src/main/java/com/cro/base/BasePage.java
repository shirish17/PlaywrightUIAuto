package com.cro.base;

import com.cro.utils.UIActions;

public abstract class BasePage {

    protected final UIActions uiActions;

    public BasePage(UIActions uiActions) {
        this.uiActions = uiActions;
    }
}
