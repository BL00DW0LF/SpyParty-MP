package com.gmail.kalebfowler6.DecisionOverloadFree.spypartymp.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;


public class ActivityMain extends ActionBarActivity {

    private Boolean currentSpyIsMe=false;//Who is the first spy?  (gets changed to true in onCreate() )
    private Boolean switchThisRound=true;//whether spy/snipers switch in this pair
    private List<String> scoreHistory;
    private ArrayAdapter adapterList;//want this globally, so it never disappears

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillSpinner();
        changeFirstSpy(this.getWindow().getDecorView());//set the text of first spy button
        //prepareScoreHistory();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFirstSpy(View v){
        if(currentSpyIsMe){
            ((Button)findViewById(R.id.buttonSpy)).setText("First Spy\n----->");
            currentSpyIsMe=false;

        }
        else
        {
            ((Button)findViewById(R.id.buttonSpy)).setText("First Spy\n<-----");
            currentSpyIsMe=true;
        }
        notifySpinner();//change layout so the spinner (to add score) is under the current spy
    }

    public void fillSpinner(){
        //points spinner
        Spinner spinnerMyPoints = (Spinner) findViewById(R.id.spinnerMyPoints);
        Spinner spinnerYourPoints = (Spinner) findViewById(R.id.spinnerYourPoints);

        String[] listPoints=new String[SettingsHelper.getMaxMissions()+1];
        for (int i=0;i<=SettingsHelper.getMaxMissions();i++){
            listPoints[i]="+"+i;}

        // Create an ArrayAdapter using the string array and a layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_dropdown_item, listPoints);

        // Apply the adapter to the spinner
        spinnerMyPoints.setAdapter(adapter);
        spinnerYourPoints.setAdapter(adapter);
    }

    public void notifySpinner(){//change layout so the spinner (to add score) is under the current spy
        Spinner spinnerMyPoints = (Spinner) findViewById(R.id.spinnerMyPoints);
        Spinner spinnerYourPoints = (Spinner) findViewById(R.id.spinnerYourPoints);
        if(currentSpyIsMe){
            spinnerMyPoints.setVisibility(View.VISIBLE);
            spinnerYourPoints.setVisibility(View.INVISIBLE);
        }
        else{
            spinnerMyPoints.setVisibility(View.INVISIBLE);
            spinnerYourPoints.setVisibility(View.VISIBLE);
        }
    }

    public void prepareScoreHistory(){//set up the listView with variable scoreHistory, and the ability to remove elements by tapping them
        final ListView listView = (ListView) findViewById(R.id.listView);
        adapterList = new ArrayAdapter(this,android.R.layout.simple_list_item_1, scoreHistory);
        listView.setAdapter(adapterList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //when we receive a click
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(ActivityMain.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete \"" + scoreHistory.get(position)+"\"?");
                final int positionToRemove = position;
                adb.setNegativeButton("No", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        scoreHistory.remove(positionToRemove);
                        adapterList.notifyDataSetChanged();
                        updateScore();
                    }
                });
                adb.show();
            }
        });
    }

    public void scoreRound(View v){

    }

    public void updateScore(){//takes the list, and converts it to score

    }
}
