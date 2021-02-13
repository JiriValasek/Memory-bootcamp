package com.example.memorybootcamp.web.faceretrival;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class Picture implements Serializable, Parcelable
{

    @Json(name = "large")
    private String large;
    @Json(name = "medium")
    private String medium;
    @Json(name = "thumbnail")
    private String thumbnail;
    public final static Parcelable.Creator<Picture> CREATOR = new Creator<Picture>() {

        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        public Picture[] newArray(int size) {
            return (new Picture[size]);
        }

    }
            ;
    private final static long serialVersionUID = -2307921449649933871L;

    protected Picture(Parcel in) {
        this.large = ((String) in.readValue((String.class.getClassLoader())));
        this.medium = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnail = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Picture() {
    }

    /**
     *
     * @param thumbnail URL to a face thumbnail/small resolution face picture
     * @param large URL to a large resolution face picture
     * @param medium  URL to a medium resolution face picture
     */
    public Picture(String large, String medium, String thumbnail) {
        super();
        this.large = large;
        this.medium = medium;
        this.thumbnail = thumbnail;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public Picture withLarge(String large) {
        this.large = large;
        return this;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public Picture withMedium(String medium) {
        this.medium = medium;
        return this;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Picture withThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(large);
        dest.writeValue(medium);
        dest.writeValue(thumbnail);
    }

    public int describeContents() {
        return 0;
    }

}