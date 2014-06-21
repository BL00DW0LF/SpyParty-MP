package com.gmail.kalebfowler6.spypartymp.app.models;

/**
 * Created by stuart on 6/21/14.
 */
public class Match {

    public enum Role {
        SPY,
        SNIPER
    }

    // setup variables
    private String playerName;
    private String opponentName;
    private int winDifference;
    private Role firstRole;

    // match variables
    private int roundNumber;
    private Role currentRole;

    // single Match instance
    private static Match match;

    private Match() {
    }

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
        match.firstRole = null;
        match.roundNumber = 0;
        match.currentRole = null;
    }

    public Role getCurrentRole() {
        return currentRole;
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

    public Match setFirstRole(Role role) {
        currentRole = role;
        return this;
    }

    private Role getRoleForRoundNumber(int roundNumber) {
        if (roundNumber % 4 == 0 || roundNumber % 4 == 3) {
            return firstRole;
        } else {
            return Role.values()[(firstRole.ordinal() + 1) % 2];
        }
    }

}
