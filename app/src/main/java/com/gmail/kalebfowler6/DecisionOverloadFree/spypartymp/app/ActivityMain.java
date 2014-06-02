package com.gmail.kalebfowler6.DecisionOverloadFree.spypartymp.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ActivityMain extends ActionBarActivity {

    private Boolean firstSpyIsMe=false;//gets changed to true onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            ((Button)findViewById(R.id.buttonSpy)).setText("First Spy\n---->");
            firstSpyIsMe=false;

        }
        else
        {
            ((Button)findViewById(R.id.buttonSpy)).setText("First Spy\n<----");
            firstSpyIsMe=true;
        }
    }
}
