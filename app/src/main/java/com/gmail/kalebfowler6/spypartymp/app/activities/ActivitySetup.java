package com.gmail.kalebfowler6.spypartymp.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.kalebfowler6.spypartymp.app.R;
import com.gmail.kalebfowler6.spypartymp.app.models.Match;
import com.gmail.kalebfowler6.spypartymp.app.utils.SettingsHelper;

import java.util.ArrayList;
import java.util.HashSet;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;

/**
 * Created by stuart on 6/20/14.
 */
public class ActivitySetup extends BaseActivity implements OnSharedPreferenceChangeListener {

    public static final String TAG = "ActivitySetup";
    public static final String INITIALIZED_MATCH = "Initialized Match";

    private TextView mPlayerName;
    private AutoCompleteTextView mOpponentName;

    private ArrayList<String> mSavedOpponentNames = new ArrayList<String>();
    private ArrayAdapter<String> mOpponentNamesAdapter;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // get view references
        mPlayerName = (TextView) findViewById(R.id.playerName);
        mOpponentName = (AutoCompleteTextView) findViewById(R.id.opponentName);
        Button mStartMatch = (Button) findViewById(R.id.startMatch);

        // set view listeners, adapters etc.
        mOpponentNamesAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        mOpponentName.setAdapter(mOpponentNamesAdapter);

        mStartMatch.setOnClickListener(startMatchClickListener);

        // initialize shared prefs
        mPrefs = getSharedPreferences(SettingsHelper.SHARED_PREFS_FILE_KEY, MODE_PRIVATE);
        mPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume hit");
        restorePlayerName();
        restoreOpponentNames();
    }

    private View.OnClickListener startMatchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savePlayerName();
            saveOpponentName();

            Match match = Match.getMatch()
                    .setPlayerName(mPlayerName.getText().toString())
                    .setOpponentName(mOpponentName.getText().toString())
                    .setWinDifference(0)
                    .setFirstSpy(Match.Spy.PLAYER);

            Intent intent = new Intent(ActivitySetup.this, ActivityMain.class);
//            intent.putExtra(INITIALIZED_MATCH, match);
            startActivity(intent);
        }
    };

    private void savePlayerName() {
        String enteredName = mPlayerName.getText().toString().trim();
        mPrefs.edit().putString(PLAYER_NAME_KEY, enteredName).commit();

        if (!"".equals(enteredName)) {
            Log.d(TAG, "Saving player name: " + enteredName);
        }
    }

    private void saveOpponentName() {
        if (Build.VERSION.SDK_INT >= 11) {
            String enteredName = mOpponentName.getText().toString().trim();

            if (!"".equals(enteredName)) {
                HashSet<String> savedOpponentNames = new HashSet<String>(mPrefs.getStringSet(OPPONENT_NAMES_KEY, new HashSet<String>()));

                if (!savedOpponentNames.contains(enteredName)) {
                    savedOpponentNames.add(enteredName);
                }

                Log.d(TAG, "Saving opponent names: " + savedOpponentNames);

                mPrefs.edit().putStringSet(OPPONENT_NAMES_KEY, savedOpponentNames).commit();
            }
        }
    }

    private void restorePlayerName() {
        String storedPlayerName = mPrefs.getString(PLAYER_NAME_KEY, "");
        mPlayerName.setText(storedPlayerName);

        if (!"".equals(storedPlayerName)) {
            Log.d(TAG, "Loaded player name: " + storedPlayerName);
        }
    }

    private void restoreOpponentNames() {
        if (Build.VERSION.SDK_INT >= 11) {

            mSavedOpponentNames.clear();
            HashSet<String> savedOpponentNames = (HashSet<String>) mPrefs.getStringSet(OPPONENT_NAMES_KEY, new HashSet<String>());
            mSavedOpponentNames = new ArrayList<String>(savedOpponentNames);

            mOpponentNamesAdapter.clear();
            mOpponentNamesAdapter.addAll(mSavedOpponentNames);

            Log.d(TAG, "Loaded opponent names: " + mSavedOpponentNames);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase(PLAYER_NAME_KEY) && !sharedPreferences.contains(key)) {
            mPlayerName.setText("");
        } else if (key.equalsIgnoreCase(OPPONENT_NAMES_KEY) && !sharedPreferences.contains(key)) {
            mOpponentName.setText("");
            mOpponentNamesAdapter.clear();
        }
    }
}
