package com.gmail.kalebfowler6.spypartymp.app.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stuart on 6/21/14.
 */
public class Match {

    public enum Role {
        SPY("Spy"),
        SNIPER("Sniper");

        private String displayString;

        Role(String displayString) {
            this.displayString = displayString;
        }

        @Override
        public String toString() {
            return displayString;
        }
    }

    // setup variables
    private String mPlayerName;
    private String mOpponentName;
    private int mWinDifference;
    private Role mFirstRole;

    // match variables
    private int mRoundNumber;
    private Role mCurrentRole;
    private int mCurrentDifference = 0;
    private List<Round> mRounds = new ArrayList<Round>();

    public Match(String playerName, String opponentName, int winDifference, Role firstRole) {
        mPlayerName = playerName;
        mOpponentName = opponentName;
        mWinDifference = winDifference;
        mFirstRole = firstRole;
        mRoundNumber = 1;
        mCurrentRole = firstRole;
    }

    public int getCurrentRoundNumber() {
        return mRoundNumber;
    }

    public Role getCurrentRole() {
        return mCurrentRole;
    }

    public String getPlayerName() {
        return mPlayerName;
    }

    public String getOpponentName() {
        return mOpponentName;
    }

    public int getWinDifference() {
        return mWinDifference;
    }

    public int getCurrentDifference() {
        return mCurrentDifference;
    }

    public List<Round> getRounds() {
        return mRounds;
    }

    public Match postRoundResult(Round round) {
        mRounds.add(round);
        calculateDifferentialFromRounds();
        mRoundNumber += 1;
        updateCurrentRole();
        return this;
    }

    public Match deleteLastRoundResult() {
        if (mRounds.size() > 0) {
            mRounds.remove(mRounds.size() - 1);
            calculateDifferentialFromRounds();
            mRoundNumber -= 1;
            updateCurrentRole();
        }
        return this;
    }

    private void calculateDifferentialFromRounds() {
        int diff = 0;

        for (Round round : mRounds) {
            if (round.getSpyPlayer().equalsIgnoreCase(mPlayerName)) {
                diff += round.getRoundScore();
            } else {
                diff -= round.getRoundScore();
            }
        }

        mCurrentDifference = diff;
    }

    private void updateCurrentRole() {
        if (mRoundNumber % 4 == 0 || mRoundNumber % 4 == 1) {
            mCurrentRole = mFirstRole;
        } else {
            mCurrentRole = Role.values()[(mFirstRole.ordinal() + 1) % 2];
        }
    }

}
