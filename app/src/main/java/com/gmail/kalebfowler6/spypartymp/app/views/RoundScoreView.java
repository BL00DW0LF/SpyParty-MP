package com.gmail.kalebfowler6.spypartymp.app.views;

import android.content.Context;
import android.util.AttributeSet;

import com.gmail.kalebfowler6.spypartymp.app.R;

import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role;
import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role.SPY;

/**
 * Created by stuart on 6/21/14.
 */
public class RoundScoreView extends BaseScoreView {

    public RoundScoreView(Context context) {
        super(context);
    }

    public RoundScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundScoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void setScore(int n, Role playerRole) {
        if (n > 0) {
            if (SPY == playerRole) {
                setTextColor(getResources().getColor(R.color.score_green));
            } else {
                setTextColor(getResources().getColor(R.color.score_red));
            }
            setText(n);
        } else if (n == 0) {
            if (SPY == playerRole) {
                setTextColor(getResources().getColor(R.color.score_red));
            } else {
                setTextColor(getResources().getColor(R.color.score_green));
            }
            setText(n);
        }
    }

}
