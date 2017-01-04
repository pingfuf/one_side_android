package com.oneside.base.mock;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by pingfu on 16-9-19.
 */
public class MockDialogView extends LinearLayout {
    public MockDialogView(Context context) {
        super(context);
    }

    private void init() {
        setOrientation(VERTICAL);
    }
}
