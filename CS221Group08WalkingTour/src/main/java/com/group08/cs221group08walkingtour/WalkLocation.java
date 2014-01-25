package com.group08.cs221group08walkingtour;


import java.io.Serializable;
import java.util.Vector;


/**
 * Created by Angharad on 18/11/13.
 */
public class WalkLocation implements Serializable{


    public static final int MAX_NUM_IMAGES = 10;
    private String[] gps;
    private String name;
    private String desc;
    private Vector<String> images;

    public WalkLocation(){
        gps = new String[2];
        images = new Vector<String>();
    }

    public WalkLocation(String x, String y, String n){
        gps = new String[2];
        gps[0] = x;
        gps[1] = y;
        name = n;
        images = new Vector<String>();
    }

    public void addPhoto(String s){
        images.add(s);
    }

    public void deletePhoto(String s){
        images.remove(s);
    }

    public String[] getGps() {
        return gps;
    }

    public void setGps(String[] gps) {
        this.gps = gps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Vector<String> getImages() {
        return images;
    }

    public void setImages(Vector<String> images) {
        this.images = images;
    }
}
