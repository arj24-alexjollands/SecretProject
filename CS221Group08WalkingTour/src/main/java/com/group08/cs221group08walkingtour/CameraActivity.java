package com.group08.cs221group08walkingtour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CameraActivity extends ActionBarActivity {


    //----------------------------------------------------------------------------------------
    // This photo-taking code is heavily based on a tutorial found here:
    //
    // http://developer.android.com/training/camera/photobasics.html
    //----------------------------------------------------------------------------------------

    String currentPhotoPath;
    public static final String JPEG_FILE_PREFIX = "wtc_loc_pic_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
    private final int ACTION_CODE = 3;

    //----------------------------------------------------------------------------------------
    // This initiates an external application to take a photo.
    //
    // The 3rd party app will put the photo inside the Intent, returned to the method
    // onActivityResult() for processing.
    //----------------------------------------------------------------------------------------
    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = createImageFile();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        startActivityForResult(takePictureIntent, actionCode);
    }



    //-----------------------------------------------------------------
    // This method gets called by Android when it returns from the 3rd Party App
    //-----------------------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTION_CODE){

            if (resultCode == Activity.RESULT_OK){

                // Display a confirmation to the user
                //-------------------------------------------------
                Toast.makeText(getApplicationContext(), "Great, a photo has been taken", Toast.LENGTH_LONG).show();

                // Return to LocationActivity
                //-------------------------------------------------
                Intent dataIntent = getIntent();
                dataIntent.putExtra("imagePath", currentPhotoPath);

                setResult(Activity.RESULT_OK, dataIntent);
                finish();
                return;
            }

            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Take Photo cancelled.", Toast.LENGTH_LONG).show();
            }
        }
    }


    //----------------------------------------------------------------------------------------
    // This returns true if there is an app on the device capable of taking a photo for us
    //----------------------------------------------------------------------------------------
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }



    //----------------------------------------------------------------------------------------
    // This creates a File location on disk for the new photo to be saved into.
    //----------------------------------------------------------------------------------------
    private File createImageFile() {
        // Create an image file name
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmms").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File image = null;

        try {
            image = File.createTempFile(
                    imageFileName,
                    JPEG_FILE_SUFFIX,
                    getAlbumDir()
            );
        }
        catch (IOException e){ System.out.println(">>>>>>>>>>>>>>>> IOException when making space for an image. <<<<<<<<<<<");}

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //----------------------------------------------------------------------------------------
    // Returns a handle on the storage location for pictures on this device
    //----------------------------------------------------------------------------------------
    private File getAlbumDir(){
        //Create Folder
        File folder = new File(Environment.getExternalStorageDirectory().toString()+"/WTC/Images");
        folder.mkdirs();

        //Save the path as a string value
        String extStorageDirectory = folder.toString();

        return new File(extStorageDirectory);
    }


    //----------------------------------------------------------------------------------------
    // This simply adds the picture to the Android Gallery by calling the media scanner
    //----------------------------------------------------------------------------------------
 /*   private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
*/






    //----------------------------------------------------------------------------------------
    // Everything below this comment is Android-Studio-generated boilerplate.
    //
    // Any change must be clearly marked with well-spaced comments please,
    // so we can all follow what's going on.
    //----------------------------------------------------------------------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //----------------------------------------------------------------------------------------
        // I have commented out the setContentView, as we don't (currently) need one.
        //----------------------------------------------------------------------------------------
        setContentView(R.layout.camera_layout);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        //----------------------------------------------------------------------------------------
        // Take a photo, if there is a 3rd party app available.
        //----------------------------------------------------------------------------------------
        if (this.isIntentAvailable(this.getApplicationContext(), MediaStore.ACTION_IMAGE_CAPTURE)){

            dispatchTakePictureIntent(ACTION_CODE);
        }
        else {
            Toast.makeText(getApplicationContext(), "Sorry, we're unable to use the camera on this phone.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera, menu);
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
            View rootView = inflater.inflate(R.layout.camera_fragment, container, false);
            return rootView;
        }
    }



    //----------------------------------------------------------------------------------------
    // This override has been added in order to transition smoothly back to the LocationActivity
    // in case the user presses back (instead of taking a photo).
    //----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed(){

        // Finish. We will transition back to the 'LocationActivity'
        Intent exitActivity = getIntent();
        setResult(Activity.RESULT_CANCELED, exitActivity);
        finish();

        return;
    }

}
