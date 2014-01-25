package com.group08.cs221group08walkingtour;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LocationActivity extends ActionBarActivity {

    private EditText locationNameTextField;
    private EditText locationDescTextField;
    private Button saveLocationButton;
    private Button addPhotoButton;
    private GPSTracker gps;
    private ArrayList<String> imagePathsList = new ArrayList<String>();

    public static final int ACTION_CODE = 2;

    private double latitude;
    private double longitude;

    //----------------------------------------------------------------------
    // These save any text the user entered into the Intent, which
    // will get passed back to the WalkActivity
    //----------------------------------------------------------------------
    private Intent getNewLocationName(Intent intent){
        intent.putExtra("locationName", locationNameTextField.getText().toString());
        return intent;
    }
    private Intent getNewLocationDesc(Intent intent){
        intent.putExtra("locationDesc", locationDescTextField.getText().toString());
        return intent;
    }
    private Intent getNewLocationImages(Intent intent){
        String imagePaths[] = new String[imagePathsList.size()];
        imagePathsList.toArray(imagePaths);
        intent.putExtra("locationImages", imagePaths);
        return intent;
    }

    //----------------------------------------------------------------------
    // This saves the current gps coordinates (as they are when this activity started)
    // into the Intent ready for return to the WalkActivity
    //----------------------------------------------------------------------
    private Intent getGPSCoordinates(Intent intent){

        String gpsPositions[] = new String[2];
        gpsPositions[0] = Double.toString(latitude);
        gpsPositions[1] = Double.toString(longitude);

        intent.putExtra("locationGPS", gpsPositions);
        return intent;
    }


    //----------------------------------------------------------------------
    // When the user taps 'Save Location', retrieve all entered text and
    // save it inside an Intent.
    //
    // Calling finish() ends this Activity and we can return the Intent to
    // WalkActivity.
    //----------------------------------------------------------------------
    private void configureSaveLocationAction(){

        saveLocationButton = (Button)this.findViewById(R.id.saveLocationButton);
        saveLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // This get's the Intent passed into the Activity when it was created
                Intent completeWalkIntent = getIntent();

                // Save the data entered into the Intent
                getNewLocationName(completeWalkIntent);
                getNewLocationDesc(completeWalkIntent);
                getGPSCoordinates(completeWalkIntent);
                getNewLocationImages(completeWalkIntent);

                // Finish. The intent will be passed back to the 'WalkActivity'
                // where the data can be extracted.
                setResult(Activity.RESULT_OK, completeWalkIntent);
                finish();
                return;
            }


        });
    }



    //----------------------------------------------------------------------
    // When the user taps 'Add Photo', take a picture with an existing camera
    // app on the phone. (Add in a 'choose-from-album' option later?)
    //
    // Add the image's path to the images array.
    //----------------------------------------------------------------------
    private void configureAddPhotoAction(){

        addPhotoButton = (Button)this.findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent getPhotoIntent = new Intent(LocationActivity.this, CameraActivity.class);

                // When the CameraActivity returns, it sends the Intent back to
                // the onActivityResult method
                startActivityForResult(getPhotoIntent, LocationActivity.ACTION_CODE);
            }
        });
    }


    //-----------------------------------------------------------------
    // This method gets called by Android when it returns from the CameraActivity
    //-----------------------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == this.ACTION_CODE){

            if (resultCode == Activity.RESULT_OK){

                // Save the image path
                //-------------------------------------------------
                imagePathsList.add(data.getStringExtra("imagePath"));
               }

            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Take Photo cancelled.", Toast.LENGTH_LONG).show();
            }
        }
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
        setContentView(R.layout.location_layout);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        //----------------------------------------------------------------------------------------
        // Get the latest GPS coordinates, using the GPSTracker class.
        // To view the latitude and longitude for debugging purposes, uncomment the Toast message.
        //----------------------------------------------------------------------------------------
        gps = new GPSTracker(LocationActivity.this);

        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // Display a message for debugging
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // Can't get location because the GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        // Get the text field references
        //----------------------------------------------------------------------
        locationNameTextField = (EditText)this.findViewById(R.id.locationNameTextField);
        locationDescTextField = (EditText) this.findViewById(R.id.locationDescTextField);

        // Configure action when the user clicks the 'Save Location' button
        //-----------------------------------------------------------------
        this.configureSaveLocationAction();
        this.configureAddPhotoAction();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location, menu);
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
            View rootView = inflater.inflate(R.layout.location_fragment, container, false);
            return rootView;
        }
    }


    //----------------------------------------------------------------------------------------
    // This override has been added in order to transition smoothly back to the WalkActivity
    // in case the user presses back (instead of Save Location).
    //----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed(){

        // Finish. We will transition back to the 'WalkActivity'
        Intent exitActivity = getIntent();
        setResult(Activity.RESULT_CANCELED, exitActivity);
        finish();
        return;
    }
}
