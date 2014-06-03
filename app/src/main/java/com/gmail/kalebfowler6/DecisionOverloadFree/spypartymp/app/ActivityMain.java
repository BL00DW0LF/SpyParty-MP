package com.gmail.kalebfowler6.DecisionOverloadFree.spypartymp.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class ActivityMain extends ActionBarActivity {

    private Boolean firstSpyIsMe=false;//Who is the first spy?  (gets changed to true in onCreate() )
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillSpinner();
        changeFirstSpy(this.getWindow().getDecorView());//set the text of first spy button
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
        if(firstSpyIsMe){
            ((Button)findViewById(R.id.buttonSpy)).setText("First Spy\n----->");
            firstSpyIsMe=false;

        }
        else
        {
            ((Button)findViewById(R.id.buttonSpy)).setText("First Spy\n<-----");
            firstSpyIsMe=true;
        }
        notifySpinner();//change layout so the spinner (to add score) is under the current spy
    }

    public void fillSpinner(){
        //points spinner
        Spinner spinnerPoints = (Spinner) findViewById(R.id.spinnerPoints);
        String[] listPoints=new String[SettingsHelper.getMaxMissions()+1];
        for (int i=0;i<=SettingsHelper.getMaxMissions();i++){
            listPoints[i]="+"+i;}
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_dropdown_item, listPoints);
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//this line fucked it up
        // Apply the adapter to the spinner
        spinnerPoints.setAdapter(adapter);
        //spinnerPoints.setSelection(0);
    }

    public void notifySpinner(){//change layout so the spinner (to add score) is under the current spy

    }
}
