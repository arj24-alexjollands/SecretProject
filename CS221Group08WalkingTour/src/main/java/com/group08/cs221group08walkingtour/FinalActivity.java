package com.group08.cs221group08walkingtour;

import android.os.AsyncTask;
import android.os.Handler;
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
import android.widget.Toast;


public class FinalActivity extends ActionBarActivity {


    public static final int UPLOAD_NOT_YET_INITIATED = 0;
    public static final int UPLOAD_IN_PROGRESS = 1;
    public static final int UPLOAD_SUCCESSFUL = 2;
    public static final int NO_INTERNET_ACCESS = 3;
    public static final int UPLOAD_FAILED = 4;

    Walk completeWalk;
    int uploadStatus = UPLOAD_NOT_YET_INITIATED;



    //----------------------------------------------------------------------------------------
    // Attempts to upload the data to the server
    //----------------------------------------------------------------------------------------
    private void completeWalk(){

        // Get the walk from inside the Intent, passed from WalkActivity
        //----------------------------------------------------------------------------------------
        completeWalk = (Walk) getIntent().getSerializableExtra("walk");

        // AsyncTask - runs in it's own thread.
        //----------------------------------------------------------------------------------------
        new UploadWalkTask().execute(completeWalk);
    }



    //----------------------------------------------------------------------------------------
    // Using an Android AsyncTask to upload the Walk.
    //
    // This works just like Threads. It does the 'doInBackground()' method,
    // and calls 'onPostExecute()' method when the thread is finished.
    //----------------------------------------------------------------------------------------
    private class UploadWalkTask extends AsyncTask<Walk, String, String> {

        protected String doInBackground(Walk... walks){

            String returnMessage = "Attempted Upload";

            try {

                Walk w = walks[0];

                UploadHandler up = new UploadHandler();

                uploadStatus = UPLOAD_IN_PROGRESS;
                returnMessage = up.uploadWalk(w);

                if (returnMessage.equals("Sent Successfully via HTTP Post")){
                    uploadStatus = UPLOAD_SUCCESSFUL;
                }
                else {
                    uploadStatus = UPLOAD_FAILED;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnMessage;
        }

        protected void onProgressUpdate(String s){
            // Unused. Could send messages back as we upload.
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.finalpage_layout);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        // Attempt to upload the walk data to the server
        //-------------------------------------------------------------------
        this.completeWalk();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finalpage, menu);
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
            View rootView = inflater.inflate(R.layout.finalpage_fragment, container, false);
            return rootView;
        }
    }

}
