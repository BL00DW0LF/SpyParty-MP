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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gmail.kalebfowler6.spypartymp.app.R;
import com.gmail.kalebfowler6.spypartymp.app.models.Match;
import com.gmail.kalebfowler6.spypartymp.app.utils.SettingsHelper;

import java.util.ArrayList;
import java.util.HashSet;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import static android.widget.Toast.LENGTH_SHORT;
import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role.SNIPER;
import static com.gmail.kalebfowler6.spypartymp.app.models.Match.Role.SPY;

/**
 * Created by stuart on 6/20/14.
 */
public class ActivitySetup extends BaseActivity implements OnSharedPreferenceChangeListener {

    public static final String TAG = "ActivitySetup";

    private View mRootViewGroup;
    private EditText mPlayerName;
    private AutoCompleteTextView mOpponentName;
    private EditText mWinPoints;
    private ToggleButton mRoleButton;

    private ArrayList<String> mSavedOpponentNames = new ArrayList<String>();
    private ArrayAdapter<String> mOpponentNamesAdapter;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // get view references
        mRootViewGroup = findViewById(R.id.rootViewGroup);
        mPlayerName = (EditText) findViewById(R.id.playerName);
        mOpponentName = (AutoCompleteTextView) findViewById(R.id.opponentName);
        mWinPoints = (EditText) findViewById(R.id.winPoints);
        mRoleButton = (ToggleButton) findViewById(R.id.role);
        Button startMatch = (Button) findViewById(R.id.startMatch);

        // set view listeners, adapters etc.
        mOpponentNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        mOpponentName.setAdapter(mOpponentNamesAdapter);

        startMatch.setOnClickListener(startMatchClickListener);

        // initialize shared prefs
        mPrefs = getSharedPreferences(SettingsHelper.SHARED_PREFS_FILE_KEY, MODE_PRIVATE);
        mPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restorePlayerName();
        restoreOpponentNames();
        restoreWinPoints();
        mRootViewGroup.requestFocus();
    }

    private View.OnClickListener startMatchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isAllDataValid()) {

                savePlayerName();
                saveOpponentName();
                saveWinPoints();

                Match.reInitializeMatch(
                        mPlayerName.getText().toString(),
                        mOpponentName.getText().toString(),
                        Integer.valueOf(mWinPoints.getText().toString()),
                        mRoleButton.isChecked() ? SNIPER : SPY);

                Intent intent = new Intent(ActivitySetup.this, ActivityMatch.class);
                startActivity(intent);

            } else {
                Toast.makeText(ActivitySetup.this, "All fields are required", LENGTH_SHORT).show();
            }
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

    private void saveWinPoints() {
        int enteredPoints = Integer.valueOf(mWinPoints.getText().toString().trim());

        if (enteredPoints > 0) {
            mPrefs.edit().putInt(WIN_POINTS_KEY, enteredPoints).commit();
            Log.d(TAG, "Saving win points: " + enteredPoints);
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

    private void restoreWinPoints() {
        int storedWinPoints = mPrefs.getInt(WIN_POINTS_KEY, 7);
        mWinPoints.setText(Integer.toString(storedWinPoints));

        if (0 != storedWinPoints) {
            Log.d(TAG, "Loaded win points: " + storedWinPoints);
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

    private boolean isAllDataValid() {
        return isViewDataValid(mPlayerName) && isViewDataValid(mOpponentName) && isViewDataValid(mWinPoints);
    }

    private boolean isViewDataValid(TextView v) {
        boolean b = !"".equals(v.getText().toString().trim());

        if (!b) {
            v.setText("");
            v.requestFocus();
        }

        return !"".equals(v.getText().toString().trim());
    }
}
