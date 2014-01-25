package com.group08.cs221group08walkingtour;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends ActionBarActivity{



    public static final int MAKE_NEW_WALK = 1;
    public static final int USE_EXISTING_WALK = 2;
    private Walk existingWalk = null;
    private Button makeWalkButton;





    //-------------------------------------
    // Make a call to the InputOutputHandler
    // to check for any existing Walk data.
    //
    // Either restore or create a new Walk.
    //
    // Returns an int indicating whether
    // a Walk is in progress or not.
    //-------------------------------------
    private int initializeWalk(){

        // TODO - add functionality and call the InputOutputHandler
        // It will need to check whether there is a Walk saved (using
        // Serializable) and if so, retrieve it.




        // Make a call to the GPSTracker, in order to begin
        // getting a GPS fix.
        GPSTracker tracker = new GPSTracker(MainActivity.this);
        tracker.getLatitude();
        tracker.getLongitude();



        if (existingWalk == null){
            return MAKE_NEW_WALK;
        }
        else {
            return USE_EXISTING_WALK;
        }
    }



    private void initiateGetNewWalkDetailsActivity(){

    }



    //----------------------------------------------------------------------
    // This method is the first thing called when the app is created
    //----------------------------------------------------------------------
    private void runApp(){


        // Create or retrieve a walk object
        //----------------------------------------------------------------------
        int walkStatus = this.initializeWalk();


        // Make a new Walk, or skip this activity and go to the 'Walk Activity'
        //----------------------------------------------------------------------
        if (walkStatus == MAKE_NEW_WALK){
            setContentView(R.layout.main_layout);

            // The new Walk details such as Name and Description, with defaults.
            Bundle newWalkData = new Bundle();
            newWalkData.putString("walkTitle", "@string/default_walk_title");
            newWalkData.putString("walkDescShort", "@string/default_walk_desc_short");
            newWalkData.putString("walkDescLong", "@string/default_walk_desc_long");




            // Go to the 'Walk Activity' when the 'Make Walk' button is pressed
            //----------------------------------------------------------------------
            makeWalkButton = (Button)this.findViewById(R.id.makeWalkButton);
            makeWalkButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent walkIntent = new Intent(MainActivity.this, WalkActivity.class);

                    Walk w = new Walk();
                    walkIntent.putExtra("walk", w);

                    startActivity(walkIntent);
                }


            });


            // Go to the 'Walk Details Activity' when the 'Set Details' button is pressed
            //----------------------------------------------------------------------
            makeWalkButton = (Button)this.findViewById(R.id.makeWalkButton);
            makeWalkButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent walkIntent = new Intent(MainActivity.this, WalkActivity.class);

                    Walk w = new Walk();
                    walkIntent.putExtra("walk", w);

                    startActivity(walkIntent);
                }


            });

        } else {

            // Navigate to the Walk Activity, retrieving the Walk object
            // from InputOutputHandler's getSavedData() method.

        }
    }





















    //----------------------------------------------------------------------------------------
    // Everything below this comment is Android-Studio-generated boilerplate.
    //
    // Any change must be clearly marked with well-spaced comments please,
    // so we can all follow what's going on.
    //----------------------------------------------------------------------------------------



    //---------------------------------------------
    // onCreate - gets called the first time the app
    // is opened or when it is reopened after being
    // completely closed.
    //---------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        this.runApp();
    }













    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.main_fragment, container, false);
            return rootView;
        }
    }

}
