package com.navigine.naviginedemo;

import android.media.Image;
import android.net.Uri;

public class ReviewClass {
    private String locotion;
    private float rating;
    private String feedback;
    private String pic;
    public ReviewClass (){
    }

    public ReviewClass(String locotion, float rating, String feedback , String pic){
        this.locotion =locotion;
        this.rating=rating;
        this.feedback=feedback;
        this.pic = pic;
    }

    public String getLocotion() {
        return locotion;
    }

    public void setLocotion(String locotion) {
        this.locotion = locotion;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public  String getPic() {return pic;}

    public void setPic(String pic) {this.pic = pic;}

//    public  Uri getImage1() {
//        return image1;
//    }
}
