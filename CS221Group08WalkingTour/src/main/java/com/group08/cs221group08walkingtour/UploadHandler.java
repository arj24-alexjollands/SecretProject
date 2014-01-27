package com.group08.cs221group08walkingtour;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Alex on 10/12/13.
 */
public class UploadHandler {


    private String alexURL = "http://users.aber.ac.uk/arj24/group08/test2.php/";
    private String jamesURL = "http://users.aber.ac.uk/jmh25/group_project/test2.php/";



    public UploadHandler(){

    }


    public String uploadWalk(Walk w){

        String exitMessage = "";

        StringBuilder b = new StringBuilder();

        //----- Walk ------
        b.append(getWalkStart());
        b.append(getWalkDataStart());
        b.append(getWalkTitle(w));
        b.append(getWalkDescriptionShort(w));
        b.append(getWalkDescriptionLong(w));
        b.append(getWalkDataEnd());

        //---- Locations -----
        b.append(getLocationsStart());

        // For every WalkLocation in Walk
        int numLocations = w.getWalkLocations().size();
        for(int i = 0; i < numLocations; i++){

            WalkLocation l = w.getWalkLocations().get(i);

            b.append(getLocationDataStart());

            b.append(getLocationName(l));
            b.append(getLocationDescription(l));
            b.append(getLocationGPS(l));

            // For every Image in WalkLocation
            for(int j = 0; j < l.getImages().size(); j++){
                b.append(getLocationImageName(l,j));

                this.uploadFile(l.getImages().get(0));
            }

            b.append(getLocationDataEnd());
        }

        //---- Coordinates -----
        b.append(getCoordinateDataStart());
        b.append(getAllCoordinateElements(w));
        b.append(getCoordinateDataEnd());


        //----- End XML -----
        b.append(getLocationsEnd());
        b.append(getWalkEnd());


        //----- Upload -----
        String walkTitle = w.getTitle();
        String walkData = b.toString();

        exitMessage = this.uploadData(walkTitle, walkData);



        return exitMessage;
    }


    //-------------------------------------------------------------------------
    private String getWalkStart(){
        return "<walk>\n";
    }
    private String getWalkEnd(){
        return "</walk>\n";
    }
    //-------------------------------------------------------------------------
    private String getWalkDataStart(){
        return "<walk_data>\n";
    }
    private String getWalkDataEnd(){
        return "</walk_data>\n";
    }
    //-------------------------------------------------------------------------
    private String getWalkTitle(Walk w){
        return "<title>" + w.getTitle() + "</title>\n";
    }
    //-------------------------------------------------------------------------
    private String getWalkDescriptionShort(Walk w){
        return "<desc_short>" + w.getDescShort() + "</desc_short>\n";
    }
    //-------------------------------------------------------------------------
    private String getWalkDescriptionLong(Walk w){
        return "<desc_long>" + w.getDescLong() + "</desc_long>\n";
    }
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    private String getLocationsStart(){
        return "<locations>\n";
    }
    private String getLocationsEnd(){
        return "</locations>\n";
    }
    //-------------------------------------------------------------------------
    private String getLocationDataStart(){
        return "<location_data>\n";
    }
    private String getLocationDataEnd(){
        return "</location_data>\n";
    }
    //-------------------------------------------------------------------------
    private String getLocationName(WalkLocation l){
        return "<name>" + l.getName() + "</name>\n";
    }
    //-------------------------------------------------------------------------
    private String getLocationDescription(WalkLocation l){
        return "<description>" + l.getDesc() + "</description>\n";
    }
    //-------------------------------------------------------------------------
    private String getLocationGPS(WalkLocation l){
        return "<gps>" + l.getGps()[0] + "," + l.getGps()[1] + "</gps>\n";
    }
    //-------------------------------------------------------------------------
    private String getLocationImageName(WalkLocation l, int index){
        return "<image>" + l.getImages().get(index) + "</image>\n";
    }
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    private String getCoordinateDataStart(){
        return "<coordinates>\n";
    }
    private String getCoordinateDataEnd(){
        return "</coordinates>\n";
    }
    //-------------------------------------------------------------------------
    private String getAllCoordinateElements(Walk w){

        StringBuilder b = new StringBuilder();

        for (int i = 0; i < w.getCoordinates().size(); i++){
            b.append(w.getCoordinates().get(i)[0] + "," + w.getCoordinates().get(i)[1] + ",0.000000\n" );
        }

        return b.toString();
    }
    //-------------------------------------------------------------------------






    /*
    * Upload the walk to the server using HTTP Post protocol.
    *
    * This method returns a message String indicating whether
    * the post was a success or not.
    *
    * This method only uploads textual data - images are uploaded
    * separately.
    *
     */
    private String uploadData(String walkTitle, String walkData){

        String returnMessage = "Attempting upload";

        System.out.println("Creating post classes");

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(alexURL);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("android_device_group08", "walk_received"));
        pairs.add(new BasicNameValuePair("walk_name", walkTitle));
        pairs.add(new BasicNameValuePair("walk_data", walkData));

        try{
            post.setEntity(new UrlEncodedFormEntity(pairs));
        }
        catch(UnsupportedEncodingException e){
            returnMessage = "UEE Exception! Some fields have unsupported characters in them.";
        }

        try{
            HttpResponse response = client.execute(post);
            InputStream inputStream = response.getEntity().getContent();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();

            String bufferedStrChunk = null;

            while((bufferedStrChunk = bufferedReader.readLine()) != null){
                stringBuilder.append(bufferedStrChunk);
            }

            System.out.println(">>>>> " + stringBuilder.toString() + " <<<<<<<<");
            returnMessage = "Walk Sent Successfully via HTTP Post";
        }
        catch(ClientProtocolException e){
            returnMessage = "ClientProtocol Exception!";
        }
        catch(IOException e){
            returnMessage = "Walk upload failed - please check you have a 3G or Wifi internet connection.";
        }

        return returnMessage;
    }



    /*
    *
    * Upload an image to the server.
    *
    * Majority credit for this method belongs
    * to the androidexample.com team:
    *
    * http://bit.ly/1iCoElu
    *
    */
    public int uploadFile(String sourceFileUri) {

        TextView messageText;
        Button uploadButton;
        int serverResponseCode = 0;
        ProgressDialog dialog = null;

        final String uploadFilePath = "/mnt/sdcard/";
        final String uploadFileName = "";

        String upLoadServerUri = "http://users.aber.ac.uk/arj24/group08/uploadtoserver.php";
        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            //dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);


            System.out.println("=========??????============");
            System.out.println("Source File does not exist :"+ uploadFilePath + " " + uploadFileName);
            System.out.println("=========??????============");

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){
                    String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                            +"http://users.aber.ac.uk/arj24/group08/"
                            +uploadFileName;
                    System.out.println("=========??????============");
                    System.out.println(msg);
                    System.out.println("=========??????============");
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                //dialog.dismiss();
                ex.printStackTrace();

                System.out.println("=========??????============");
                System.out.println("MalformedURLException Exception : check script url.");
                System.out.println("=========??????============");


                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                //dialog.dismiss();
                e.printStackTrace();

                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            //dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }



}
