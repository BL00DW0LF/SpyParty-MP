package com.gmail.kalebfowler6.spypartymp.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stuart on 6/21/14.
 */
public class Match implements Parcelable {

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

    // static match instance:
    public static Match mMatch;

    // setup variables
    private String mPlayerName;
    private String mOpponentName;
    private int mWinDifference;
    private Role mFirstRole;

    // mMatch variables
    private int mRoundNumber;
    private Role mCurrentRole;
    private int mCurrentDifference = 0;
    private List<Round> mRounds = new ArrayList<Round>();

    private Match(String playerName, String opponentName, int winDifference, Role firstRole) {
        mPlayerName = playerName;
        mOpponentName = opponentName;
        mWinDifference = winDifference;
        mFirstRole = firstRole;
        mRoundNumber = 1;
        mCurrentRole = firstRole;
    }

    public static void reInitializeMatch(String playerName, String opponentName, int winDifference, Role firstRole) {
        mMatch = new Match(playerName, opponentName, winDifference, firstRole);
    }

    public static Match getMatch() {
        return mMatch;
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

    public Match postRoundScore(Round round) {
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
        if (mRoundNumber % 4 <= 1) {
            mCurrentRole = mFirstRole;
        } else {
            mCurrentRole = Role.values()[(mFirstRole.ordinal() + 1) % 2];
        }
    }

    // parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPlayerName);
        dest.writeString(this.mOpponentName);
        dest.writeInt(this.mWinDifference);
        dest.writeInt(this.mFirstRole == null ? -1 : this.mFirstRole.ordinal());
        dest.writeInt(this.mRoundNumber);
        dest.writeInt(this.mCurrentRole == null ? -1 : this.mCurrentRole.ordinal());
        dest.writeInt(this.mCurrentDifference);
        dest.writeList(this.mRounds);
    }

    private Match(Parcel in) {
        this.mPlayerName = in.readString();
        this.mOpponentName = in.readString();
        this.mWinDifference = in.readInt();
        int tmpMFirstRole = in.readInt();
        this.mFirstRole = tmpMFirstRole == -1 ? null : Role.values()[tmpMFirstRole];
        this.mRoundNumber = in.readInt();
        int tmpMCurrentRole = in.readInt();
        this.mCurrentRole = tmpMCurrentRole == -1 ? null : Role.values()[tmpMCurrentRole];
        this.mCurrentDifference = in.readInt();

        this.mRounds = new ArrayList<Round>();
        in.readList(this.mRounds, Round.class.getClassLoader());
    }

    public static Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        public Match createFromParcel(Parcel source) {
            return new Match(source);
        }

        public Match[] newArray(int size) {
            return new Match[size];
        }
    };
}
