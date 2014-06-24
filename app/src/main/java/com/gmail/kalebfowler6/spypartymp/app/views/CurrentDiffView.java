package com.gmail.kalebfowler6.spypartymp.app.views;

import android.content.Context;
import android.util.AttributeSet;

import com.gmail.kalebfowler6.spypartymp.app.R;

import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role;

/**
 * Created by stuart on 6/21/14.
 */
public class CurrentDiffView extends BaseScoreView {

    private int mCurrentScoreSigned = 0;

    public CurrentDiffView(Context context) {
        super(context);
    }

    public CurrentDiffView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrentDiffView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        setTextColor(getResources().getColor(R.color.score_white));
    }

    @Override
    public void setScore(int n, Role playerRole) {
        mCurrentScoreSigned = n;

        if (n < 0) {
            setBackgroundResource(R.drawable.red_circle);
            setText(Integer.toString(n));
        } else if (n > 0) {
            setBackgroundResource(R.drawable.green_circle);
            setText("+"+Integer.toString(n));
        } else {
            setBackgroundResource(R.drawable.gray_circle);
            setText("0");
        }
    }

    public int getCurrentScoreSigned() {
        return mCurrentScoreSigned;
    }
}
