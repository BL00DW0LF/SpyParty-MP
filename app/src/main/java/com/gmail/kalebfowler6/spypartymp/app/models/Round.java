package com.gmail.kalebfowler6.spypartymp.app.models;

import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role;
import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role.SPY;

/**
 * Created by stuart on 6/21/14.
 */
public class Round {

    private String mSpyPlayer;
    private Role mPlayerRole;
    private int mRoundScore;
    private int mRoundNumber;

    public Round(Match match, Role playerRole, int roundScore, int roundNumber) {
        mPlayerRole = playerRole;
        mRoundScore = roundScore;
        mRoundNumber = roundNumber;

        mSpyPlayer = mPlayerRole == SPY ? match.getPlayerName() : match.getOpponentName();
    }

    public String getSpyPlayer() {
        return mSpyPlayer;
    }

    public Role getPlayerRole() {
        return mPlayerRole;
    }

    public int getRoundScore() {
        return mRoundScore;
    }

    public int getRoundNumber() {
        return mRoundNumber;
    }
}
