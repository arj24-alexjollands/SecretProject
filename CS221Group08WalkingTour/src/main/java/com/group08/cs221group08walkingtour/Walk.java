package com.group08.cs221group08walkingtour;
import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Tango on 18/11/13.
 */


public class Walk implements Serializable{

    private String title;
    private String descShort;
    private String descLong;
    private Vector<WalkLocation> walkLocations;
    private Vector<String[]> coordinates;

    public Walk(){
        walkLocations = new Vector<WalkLocation>();
    }

    public Walk(String t){
        title = t;
        walkLocations = new Vector<WalkLocation>();
    }

    public void addLocation(WalkLocation l){
        walkLocations.add(0,l);
    }

    public void deleteLocation(WalkLocation l){
        walkLocations.remove(l);
    }

    public Walk saveWalk(){
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescShort() {
        return descShort;
    }

    public void setDescShort(String descShort) {
        this.descShort = descShort;
    }

    public String getDescLong() {
        return descLong;
    }

    public void setDescLong(String descLong) {
        this.descLong = descLong;
    }

    public Vector<WalkLocation> getWalkLocations() {
        return walkLocations;
    }

    public void setWalkLocations(Vector<WalkLocation> walkLocations) {
        this.walkLocations = walkLocations;
    }

    public void setCoordinates(Vector<String[]> coordinates){
        this.coordinates = coordinates;
    }

    public Vector<String[]> getCoordinates(){
        return coordinates;
    }


    public static Walk getDummyWalk(){

        Walk w = new Walk();
        w.setTitle("Example Walk");
        w.setDescShort("An exciting, example Walk");
        w.setDescLong("This Walk is a simple example, accessible statically and to be used" +
                        "\n to test whether the upload functionality is working or not.");

        //----- WalkLocation 1 --------
        WalkLocation loc1 = new WalkLocation();
        loc1.setName("The Precipice");
        loc1.setDesc("This is the last chance a walker has to change their mind");

        String gps1[] = new String[2];
        gps1[0] = "-111.1111111111111";
        gps1[1] = "11.11111111111111";
        loc1.setGps(gps1);

        Vector<String> images1 = new Vector<String>();
        images1.add("my-loc-photo-1.jpg");
        images1.add("my-loc-photo-2.jpg");
        images1.add("my-loc-photo-3.jpg");
        images1.add("my-loc-photo-4.jpg");
        loc1.setImages(images1);

        //----- WalkLocation 2 --------
        WalkLocation loc2 = new WalkLocation();
        loc2.setName("The descent");
        loc2.setDesc("There is only down. Waste not your time and energy " +
                                    "in an attempt to regain that which is lost");

        String gps2[] = new String[2];
        gps2[0] = "-222.22222222222222";
        gps2[1] = "22.2222222222222";
        loc2.setGps(gps2);

        Vector<String> images2 = new Vector<String>();
        images2.add("my-location-photo-1.jpg");
        images2.add("my-location-photo-2.jpg");
        images2.add("my-location-photo-3.jpg");
        images2.add("my-location-photo-4.jpg");
        loc2.setImages(images2);

        Vector<WalkLocation> walkLocations = new Vector<WalkLocation>();
        walkLocations.add(loc1);
        walkLocations.add(loc2);

        w.setWalkLocations(walkLocations);

        return w;
    }

}