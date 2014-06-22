package com.gmail.kalebfowler6.spypartymp.app.views;

/**
 * Created by stuart on 6/21/14.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role;

/**
 * Created by stuart on 6/21/14.
 */
public abstract class BaseScoreView extends TextView {

    public abstract void setScore(int n, Role playerRole);

    public BaseScoreView(Context context) {
        super(context);
        init();
    }

    public BaseScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseScoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        setScore(0, null);
    }

}
