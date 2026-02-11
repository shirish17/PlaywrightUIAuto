package com.cro.utils;

/*
 * This class will hold the strategy to get the element text
 * Example: Locator successMsg = page.locator(".k-notification");

String msg = uiActions.getText(successMsg, TextStrategy.VISIBLE);

Call flows like: Page → UIActions → TextStrategy


 */
public enum TextStrategy {
    VISIBLE,        // innerText() --> based on real user visibility Best for:Toasts / flash messages, Kendo alerts, Labels, headings, UI assertions
    RAW,            // textContent()--> DOM: Includes hidden text, Hidden fields, Debug / structural validation
    INPUT_VALUE     // inputValue() --> FORM FIELDS ONLY, Only for <input>, <textarea>
}
