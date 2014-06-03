package com.gmail.kalebfowler6.spypartymp.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gmail.kalebfowler6.spypartymp.app.R;

import java.util.ArrayList;
import java.util.List;


public class ActivityMain extends ActionBarActivity {

    private Boolean currentSpyIsMe=false;//Who is the first spy?  (gets changed to true in onCreate() )
    private Boolean switchThisRound=true;//whether spy/snipers switch in this pair
    private Boolean editMode=true;//if we are not in a game, we are in editMode
    private List<String> scoreHistory;
    private ArrayAdapter adapterList;//want this globally, so it never disappears
    private int totalScore=0;
    private int winThreshold;//score needed to win

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView)findViewById(R.id.textScore)).setText("");//erases "score" from the score textbox
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
        if (id == R.id.action_help) {
            //open the help activity
            Intent myIntent = new Intent(ActivityMain.this, ActivityHelp.class);
            ActivityMain.this.startActivity(myIntent);
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
                if(position==0) {//only allow the removal of the first element
                    AlertDialog.Builder adb = new AlertDialog.Builder(ActivityMain.this);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to remove \"" + scoreHistory.get(position) + "\"?");
                    final int positionToRemove = position;
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateScoreFromItem(scoreHistory.remove(positionToRemove));
                            if(scoreHistory.size()==0)
                                resetAll();
                            adapterList.notifyDataSetChanged();

                            //disable everthing normally disabled to handle case when you made a mistake on the winning game, and you undo it
                            (findViewById(R.id.editPlayer1)).setEnabled(false);
                            (findViewById(R.id.editPlayer2)).setEnabled(false);
                            (findViewById(R.id.buttonSpy)).setEnabled(false);
                            editMode=false;
                        }
                    });
                    adb.show();
                }
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
            startingNewGame();

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

        updateScore();//totalScore has changed, display it
        scoreHistory.add(0,history);//add history from the top
        adapterList.notifyDataSetChanged();//update the listview

        if(switchThisRound)//are we supposed to switch this round
            switchSpy();
        else{//it's the start of a new set, check score threshold
            if (Math.abs(totalScore)>=winThreshold){
                notifyWinner();
            }
            switchThisRound=true;//if not, then we'll switch next round
        }
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
        //first, need to get names of me and opponent
        String temp=((EditText) findViewById(R.id.editPlayer1)).getText().toString();
        String myName;
        String yourName;
        if(temp.length()>0)
            myName=temp;
        else
            myName="You";
        temp=((EditText) findViewById(R.id.editPlayer2)).getText().toString();
        if(temp.length()>0)
            yourName=temp;
        else
            yourName="Opponent";

        start=0;
        end=item.indexOf(" scored ");
        temp=item.substring(start,end);
        if(myName.contentEquals(temp))
        {
            //if I scored (I was spy), score was positive
            totalScore-=scoreDiff;//so subtract the score

            //current state will tell us if we switched last round
            if(currentSpyIsMe)//then we didn't switch last round
                switchThisRound=false;
            else
                switchThisRound=true;

            currentSpyIsMe=true;
        }
        else if(yourName.contentEquals(temp))
        {
            //if opponent scored (opponent was spy), score was negative
            totalScore+=scoreDiff;//so add the score

            //current state will tell us if we switched last round
            if(!currentSpyIsMe)//then we didn't switch last round
                switchThisRound=false;
            else
                switchThisRound=true;

            currentSpyIsMe=false;
        }
        else
        {
            Log.e("SpyParty-M=P","Error: don't know my name or opponent's name when removing");
        }

        updateScore();//show score
        notifySpinner();//make sure the correct person can enter score
    }

    public void updateScore(){
        String score="You are "+Math.abs(totalScore)+" point";
        if(!(Math.abs(totalScore)==1))
            score+="s";
        if (totalScore<0)
            score+=" behind.";
        else
            score+=" ahead.";
        ((TextView)findViewById(R.id.textScore)).setText(score);
    }

    public void resetAll(){
        (findViewById(R.id.editPlayer1)).setEnabled(true);
        (findViewById(R.id.editPlayer2)).setEnabled(true);
        (findViewById(R.id.buttonSpy)).setEnabled(true);
        editMode=true;
        changeFirstSpy(this.getWindow().getDecorView());
    }
    public void resetAll(View v){
        scoreHistory.clear();//won't need to do this stuff when we remove the last element, so it can be here instead of in resetAll()
        adapterList.notifyDataSetChanged();
        resetAll();
    }//necessary for button's onClick

    public void startingNewGame(){
        //disable a bunch of stuff that should not change through the course of a game
        (findViewById(R.id.editPlayer1)).setEnabled(false);
        (findViewById(R.id.editPlayer2)).setEnabled(false);
        (findViewById(R.id.buttonSpy)).setEnabled(false);
        editMode=false;
        scoreHistory.clear();
        adapterList.notifyDataSetChanged();

        //show a popup asking for the score threshold
        winThreshold=999;//overwrite old one for safety
        AlertDialog.Builder adb = new AlertDialog.Builder(ActivityMain.this);
        adb.setTitle("Set the score difference needed to win:");
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[] {
                // Maximum 2 characters.
                new InputFilter.LengthFilter(3),
                // Digits only.
                DigitsKeyListener.getInstance(),  // Not strictly needed, IMHO.
        });

        // Digits only & use numeric soft-keyboard.
        input.setKeyListener(DigitsKeyListener.getInstance());
        adb.setView(input);
        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().length()==0)
                    winThreshold=0;
                else
                    winThreshold=Integer.parseInt(input.getText().toString());
                //need to hide the keyboard, it doesn't like me closing on it
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
        adb.show();
    }

    public void notifyWinner(){
        resetAll();//keep history
        TextView txtScore=(TextView)findViewById(R.id.textScore);
        String winnerName="";
        String msg;

        //get name of winner
        if (totalScore>0){//I won
            winnerName=((EditText) findViewById(R.id.editPlayer1)).getText().toString();
            if(winnerName.length()==0)
                winnerName="You";
        }
        else if (totalScore<0){//opponent won
            winnerName=((EditText) findViewById(R.id.editPlayer2)).getText().toString();
            if(winnerName.length()==0)
                winnerName="Opponent";
        }

        if (totalScore==0)
            msg="You're trying to break this, aren't you?";
        else
            msg=""+winnerName+" won at "+winThreshold;
        txtScore.setText(msg);
    }
}
