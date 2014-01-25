package com.group08.cs221group08walkingtour;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class WalkActivity extends ActionBarActivity implements Serializable{


    private Walk currentWalk;
    private Button addLocationButton;
    private Button completeWalkButton;
    private Vector<String[]> coordinatesList = new Vector<String[]>();
    private boolean recordingCoordinates = true;
    private int COORDINATE_LOGGING_INTERVAL = 20000; // Milliseconds
    private GPSTracker gps;

    //-----------------------------------------------------------------
    // Activity Transitions are tied to button clicks here
    //-----------------------------------------------------------------
    private void configureButtons(){


        // Transition to the LocationActivity when the user clicks the
        // 'Add Location' button.
        //-----------------------------------------------------------------
        addLocationButton = (Button)this.findViewById(R.id.newLocationButton);
        addLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent addLocationIntent = new Intent(WalkActivity.this, LocationActivity.class);

                // Information on startActivityForResult can be found here - http://bit.ly/1bInSu3
                startActivityForResult(addLocationIntent, 1);
            }


        });


        // Transition to the FinalActivity when the user clicks the
        // 'Complete Walk' button.
        //-----------------------------------------------------------------
        completeWalkButton = (Button)this.findViewById(R.id.completeWalkButton);
        completeWalkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Stop logging the coordinates and save them to the Walk.
                recordingCoordinates = false;
                currentWalk.setCoordinates(coordinatesList);

                Intent completeWalkIntent = new Intent(WalkActivity.this, FinalActivity.class);
                completeWalkIntent.putExtra("walk", currentWalk);
                startActivity(completeWalkIntent);
            }


        });

    }




    //-----------------------------------------------------------------
    // This method gets called by Android when it returns from the
    // LocationActivity (when a user clicks 'Save Location')
    //-----------------------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1){

            if (resultCode == Activity.RESULT_OK){

                // Display a confirmation to the user
                //-------------------------------------------------
                String locationName = data.getStringExtra("locationName");
                Toast.makeText(getApplicationContext(), "Great, the location " + locationName +
                                                        " has been saved.", Toast.LENGTH_LONG).show();

                // Make the new location from the data returned
                //-------------------------------------------------
                WalkLocation newLocation = new WalkLocation();
                newLocation.setName(data.getStringExtra("locationName"));
                newLocation.setDesc(data.getStringExtra("locationDesc"));
                newLocation.setGps(data.getStringArrayExtra("locationGPS"));
                newLocation.setImages(new Vector(Arrays.asList(data.getStringArrayExtra("locationImages"))));

                currentWalk.addLocation(newLocation);
            }


            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Location cancelled.", Toast.LENGTH_LONG).show();
            }

        }

    }





    // Continual logging of coordinate data
    private void logCoordinates(){
        //----------------------------------------------------------------------------------------
        // Get the latest GPS coordinates (using the GPSTracker class) and save them continuously
        // to the coordinates ArrayList.
        //----------------------------------------------------------------------------------------

        Thread continualLogging = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    double latitude = 0.0;
                    double longitude = 0.0;

                    while(recordingCoordinates){

                        if(gps.canGetLocation()){
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();

                            String gpsPositions[] = new String[2];
                            gpsPositions[0] = Double.toString(latitude);
                            gpsPositions[1] = Double.toString(longitude);

                            // Don't save completely blank data (wait for rough GPS lock)
                            if (latitude != 0.0){
                                coordinatesList.add(gpsPositions);
                            }
                        }
                        else{
                            gps.showSettingsAlert();
                        }

                        Thread.sleep(COORDINATE_LOGGING_INTERVAL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        continualLogging.start();
    }



















    //----------------------------------------------------------------------------------------
    // Everything below this comment is Android-Studio-generated boilerplate.
    //
    // Any change must be clearly marked with well-spaced comments please,
    // so we can all follow what's going on.
    //----------------------------------------------------------------------------------------




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walk_layout);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // Retrieve the Walk from inside the Intent
        //-----------------------------------------------------------------
        currentWalk = (Walk) getIntent().getExtras().getSerializable("walk");


        // Tie activities to the button presses, housed in a separate method
        // for neatness and ease of maintenance
        //-----------------------------------------------------------------
        this.configureButtons();


        // Initiate the continuous logging of coordinate data
        //-----------------------------------------------------------------
        gps = new GPSTracker(WalkActivity.this);
        logCoordinates();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.walk, menu);
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
            View rootView = inflater.inflate(R.layout.walk_fragment, container, false);
            return rootView;
        }
    }



    //----------------------------------------------------------------------------------------
    // This override has been added in order to prevent transition back to the MainActivity
    //----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed(){
        // Save Walk data to disk and exit the app.
    }

}
