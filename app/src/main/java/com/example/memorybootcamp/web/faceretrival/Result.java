package com.example.memorybootcamp.web.faceretrival;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class Result implements Serializable, Parcelable
{

    @Json(name = "gender")
    private String gender;
    @Json(name = "name")
    private Name name;
    @Json(name = "picture")
    private Picture picture;
    public final static Parcelable.Creator<Result> CREATOR = new Creator<Result>() {

        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        public Result[] newArray(int size) {
            return (new Result[size]);
        }

    }
            ;
    private final static long serialVersionUID = 8692568980806118581L;

    protected Result(Parcel in) {
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((Name) in.readValue((Name.class.getClassLoader())));
        this.picture = ((Picture) in.readValue((Picture.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Result() {
    }

    /**
     *
     * @param gender gender of a person returned as a query result
     * @param name name of a person returned as a query result
     * @param picture picture of a person returned as a query result
     */
    public Result(String gender, Name name, Picture picture) {
        super();
        this.gender = gender;
        this.name = name;
        this.picture = picture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Result withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Result withName(Name name) {
        this.name = name;
        return this;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Result withPicture(Picture picture) {
        this.picture = picture;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(gender);
        dest.writeValue(name);
        dest.writeValue(picture);
    }

    public int describeContents() {
        return 0;
    }

}