package com.gmail.kalebfowler6.spypartymp.app.models;

import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Spy.OPPONENT;
import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Spy.PLAYER;

/**
 * Created by stuart on 6/21/14.
 */
public class Match {

    public enum Spy {
        PLAYER,
        OPPONENT
    }

    // setup variables
    private String playerName;
    private String opponentName;
    private int winDifference;
    private Spy[] spyOrder;

    // match variables
    private int roundNumber;
    private Spy currentSpy;

    // single Match instance
    private static Match match;

    private Match() { }

    public static Match getMatch() {
        if (match != null) {
            return match;
        }

        return match = new Match();
    }

    public void resetMatch() {
        match = new Match();

        match.playerName = "";
        match.opponentName = "";
        match.winDifference = 0;
        match.spyOrder = new Spy[]{};
        match.roundNumber = 0;
        match.currentSpy = null;
    }

    public Spy getCurrentSpy() {
        return currentSpy;
    }

    public Match setWinDifference(int winDifference) {
        this.winDifference = winDifference;
        return this;
    }

    public Match setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public Match setOpponentName(String opponentName) {
        this.opponentName = opponentName;
        return this;
    }

    public Match setFirstSpy(Spy spy) {
        switch (spy) {
            case PLAYER:
                this.spyOrder = new Spy[]{PLAYER, OPPONENT};
                return this;
            case OPPONENT:
                this.spyOrder = new Spy[]{OPPONENT, PLAYER};
                return this;
        }
        return null;
    }

    private Spy getSpyForRoundNumber(int roundNumber) {
        if (roundNumber % 4 == 0 || roundNumber % 4 == 3) {
            return spyOrder[0];
        } else {
            return spyOrder[1];
        }
    }

}
