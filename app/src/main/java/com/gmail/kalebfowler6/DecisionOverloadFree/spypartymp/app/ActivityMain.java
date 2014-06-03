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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ActivityMain extends ActionBarActivity {

    private Boolean currentSpyIsMe=false;//Who is the first spy?  (gets changed to true in onCreate() )
    private Boolean switchThisRound=true;//whether spy/snipers switch in this pair
    private Boolean editMode=true;//if we are not in a game, we are in editMode
    private List<String> scoreHistory;
    private ArrayAdapter adapterList;//want this globally, so it never disappears
    private int totalScore=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillSpinner();
        changeFirstSpy(this.getWindow().getDecorView());//set the text of first spy button
        prepareScoreHistory();
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
            spinnerMyPoints.setSelection(0);
            spinnerYourPoints.setVisibility(View.INVISIBLE);
        }
        else{
            spinnerMyPoints.setVisibility(View.INVISIBLE);
            spinnerYourPoints.setVisibility(View.VISIBLE);
            spinnerYourPoints.setSelection(0);
        }
    }

    public void prepareScoreHistory(){//set up the listView with variable scoreHistory, and the ability to remove elements by tapping them
        scoreHistory=new ArrayList<String>();
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
                        updateScoreFromItem(scoreHistory.remove(positionToRemove));
                        adapterList.notifyDataSetChanged();
                    }
                });
                adb.show();
            }
        });
    }

    public void scoreRound(View v){
        Spinner spinnerPoints;
        String history;
        String myName;
        String yourName;
        int score;
        String temp;

        if(editMode)//if we are starting a brand new game right now
        {
            //disable a bunch of stuff that should not change through the course of a game
            ((EditText) findViewById(R.id.editPlayer1)).setEnabled(false);
            ((EditText) findViewById(R.id.editPlayer2)).setEnabled(false);
            ((Button)findViewById(R.id.buttonSpy)).setEnabled(false);
            editMode=false;
        }

        //get myName and yourName
        temp=((EditText) findViewById(R.id.editPlayer1)).getText().toString();
        if(temp.length()>0)
            myName=temp;
        else
            myName="You";
        temp=((EditText) findViewById(R.id.editPlayer2)).getText().toString();
        if(temp.length()>0)
            yourName=temp;
        else
            yourName="Opponent";

        //get score
        if(currentSpyIsMe)
            spinnerPoints = (Spinner) findViewById(R.id.spinnerMyPoints);
        else
            spinnerPoints = (Spinner) findViewById(R.id.spinnerYourPoints);
        score=spinnerPoints.getSelectedItemPosition();

        //create text to add to history
        if(currentSpyIsMe)
        {
            history=""+myName+" scored "+score+" points.";
            totalScore+=score;
        }
        else
        {
            history=""+yourName+" scored "+score+" points.";
            totalScore-=score;
        }

        history+="\nYou are "+Math.abs(totalScore)+" point";
        if(totalScore!=1 && totalScore!=-1)
            history+="s";
        if (totalScore<0)
            history+=" behind.";
        else
            history+=" ahead.";



        if(switchThisRound)//are we supposed to switch this round
            switchSpy();
        else
            switchThisRound=true;//if not, then we'll switch next round

        updateScore();//totalScore has changed, display it
        scoreHistory.add(0,history);//add history from the top
        adapterList.notifyDataSetChanged();//update the listview
    }

    public void switchSpy(){
        if(currentSpyIsMe)
        {
            currentSpyIsMe=false;
        }
        else
        {
            currentSpyIsMe=true;
        }
        switchThisRound=false;
        notifySpinner();//change layout so the spinner (to add score) is under the current spy
    }

    public void updateScoreFromItem(String item){//takes the removed item, and parses it to determine how to update the score
        int start=item.indexOf(" scored ")+8;//add 8 because of 8 shown chars
        int end= item.indexOf(" points.");//end point is exclusive, and indexOf returns the start of the string, so no change
        int scoreDiff=Integer.parseInt(item.substring(start,end));

        //now, to add or subtract?

        updateScore();
    }

    public void updateScore(){
        String score="You are "+Math.abs(totalScore)+" point";
        if(totalScore!=1 && totalScore!=-1)
            score+="s";
        if (totalScore<0)
            score+=" behind.";
        else
            score+=" ahead.";
        ((TextView)findViewById(R.id.textScore)).setText(score);
    }
}
