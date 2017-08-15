package com.deco2800.potatoes.gui;

public class HideableGui {
    protected boolean hidden;

    /**
     * Hide's this Gui element. Fadeout effects can be implemented on a case-by-case basis.
     */
    public void hide() {
        hidden = true;
    }

    /**
     * Show's this Gui element. Fadein effects can be implemented on a case-by-case basis.
     */
    public void show() {
        hidden = false;
    }

    /**
     * @return if this element is hidden
     */
    public boolean isHidden() {
        return hidden;
    }
}
