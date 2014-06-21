package com.gmail.kalebfowler6.spypartymp.app.models;

import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Spy;

/**
 * Created by stuart on 6/21/14.
 */
public class RoundScore {

    private Spy mSpy;
    private int mPointsScored;

    public RoundScore(Spy spy, int pointsScored) {
        mSpy = spy;
        mPointsScored = pointsScored;
    }

    public String getRoundScoreString() {
        String s;
        if (mSpy == Spy.PLAYER) {
            s = "You ";
        } else {
            s = "Opponent ";
        }

        return s + "scored " + mPointsScored + "points.";
    }

}
