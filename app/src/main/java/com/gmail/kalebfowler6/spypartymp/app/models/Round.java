package com.gmail.kalebfowler6.spypartymp.app.models;

import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role;

/**
 * Created by stuart on 6/21/14.
 */
public class Round {

    private Role playerRole;
    private int roundScore;

    public Round(Role playerRole, int roundScore) {
        this.playerRole = playerRole;
        this.roundScore = roundScore;
    }

    public Role getPlayerRole() {
        return playerRole;
    }

    public int getRoundScore() {
        return roundScore;
    }
}
