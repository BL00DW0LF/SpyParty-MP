package com.gmail.kalebfowler6.spypartymp.app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.kalebfowler6.spypartymp.app.R;
import com.gmail.kalebfowler6.spypartymp.app.models.Match;
import com.gmail.kalebfowler6.spypartymp.app.models.Round;
import com.gmail.kalebfowler6.spypartymp.app.views.CurrentDiffView;

import static android.widget.Toast.LENGTH_SHORT;
import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role.SPY;

/**
 * Created by stuart on 6/21/14.
 */
public class ActivityMatch extends BaseActivity {

    private Match match = new Match("r7stuart", "virifaux", 10, SPY);
    private TextView mRoundNumber;
    private TextView mCurrentSpy;
    private CurrentDiffView mCurrentDiffView;
    private ListView mMatchHistoryListView;

    private Button mUndoButton;
    private EditText mRoundScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

//        Match match = (Match) getIntent().getParcelableExtra(ActivitySetup.INITIALIZED_MATCH);

        // get view references
        mRoundNumber = (TextView) findViewById(R.id.roundNumber);
        mCurrentSpy = (TextView) findViewById(R.id.currentSpy);
        mCurrentDiffView = (CurrentDiffView) findViewById(R.id.differential);
        mMatchHistoryListView = (ListView) findViewById(R.id.matchHistoryListView);
        mUndoButton = (Button) findViewById(R.id.undoButton);
        mRoundScore = (EditText) findViewById(R.id.roundScore);
        Button scoreButton = (Button) findViewById(R.id.scoreButton);

        // set view listeners, adapters etc.
        mUndoButton.setOnClickListener(undoButtonListener);
        scoreButton.setOnClickListener(scoreButtonListener);

        fillDataFromMatch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Reset Match")
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(ActivityMatch.this, "Reset match here", LENGTH_SHORT).show();
                        return true;
                    }
                });

        return super.onCreateOptionsMenu(menu);
    }

    private void fillDataFromMatch() {
        mRoundNumber.setText("Round Number: " + match.getCurrentRoundNumber());
        mCurrentSpy.setText("Current Spy: " + (match.getCurrentRole() == SPY ? match.getPlayerName() : match.getOpponentName()));
        mCurrentDiffView.setScore(match.getCurrentDifference(), null);

        mUndoButton.setEnabled(match.getCurrentRoundNumber() != 0);

        if (Math.abs(match.getCurrentDifference()) >= match.getWinDifference()) {
            String title = (match.getCurrentDifference() < 0 ? match.getOpponentName() : match.getPlayerName()) + " wins!";

            new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setPositiveButton("New Match", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Undo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            match.deleteLastRoundResult();
                            fillDataFromMatch();
                        }
                    })
                    .show();
        } else {
            mRoundScore.setText("");
            mRoundScore.requestFocus();
        }
    }

    private View.OnClickListener scoreButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String s = mRoundScore.getText().toString();

            if (s != null && s.trim().length() > 0) {
                match.postRoundResult(new Round(match.getCurrentRole(), Integer.valueOf(mRoundScore.getText().toString())));
            } else {
                Toast.makeText(ActivityMatch.this, "No score entered", LENGTH_SHORT).show();
            }

            fillDataFromMatch();
        }
    };

    private View.OnClickListener undoButtonListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(View v) {
            match.deleteLastRoundResult();
            fillDataFromMatch();
        }
    };

}
