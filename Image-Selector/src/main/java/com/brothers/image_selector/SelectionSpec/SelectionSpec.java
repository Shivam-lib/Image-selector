package com.brothers.image_selector.SelectionSpec;

import com.brothers.image_selector.R;

public final class SelectionSpec {

    private int themeId;

    public static SelectionSpec getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private SelectionSpec() {
        // Set the default theme here if needed
        themeId = R.style.image_selector;
    }

    public void setTheme(int themeId) {
        this.themeId = themeId;
    }

    public int getTheme() {
        return themeId;
    }
    private static final class InstanceHolder {
        private static final SelectionSpec INSTANCE = new SelectionSpec();
    }
}
