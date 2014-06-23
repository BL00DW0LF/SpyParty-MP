package com.gmail.kalebfowler6.spypartymp.app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmail.kalebfowler6.spypartymp.app.R;
import com.gmail.kalebfowler6.spypartymp.app.utils.SettingsHelper;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by stuart on 6/21/14.
 */
public class BaseActivity extends ActionBarActivity {

    public static final String PLAYER_NAME_KEY = "Player Name";
    public static final String OPPONENT_NAMES_KEY = "Opponent Names";
    public static final String WIN_POINTS_KEY = "Win Points";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String itemTitle = item.getTitle().toString();

        if (itemTitle.equalsIgnoreCase(getString(R.string.menu_item_help))) {
            Intent launchHelpIntent = new Intent(this, ActivityHelp.class);
            startActivity(launchHelpIntent);
            return true;
        } else if (itemTitle.equalsIgnoreCase(getString(R.string.menu_item_reset))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete all saved player names?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getSharedPreferences(SettingsHelper.SHARED_PREFS_FILE_KEY, MODE_PRIVATE)
                                    .edit()
                                    .remove(PLAYER_NAME_KEY)
                                    .remove(OPPONENT_NAMES_KEY)
                                    .remove(WIN_POINTS_KEY)
                                    .commit();

                            Toast.makeText(BaseActivity.this, "Saved player names deleted", LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    })
                    .show();
            return true;
        }

        return false;
    }
}
