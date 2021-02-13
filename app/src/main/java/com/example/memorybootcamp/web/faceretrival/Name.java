package com.example.memorybootcamp.web.faceretrival;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Name implements Serializable, Parcelable
{

    @Json(name = "title")
    private String title;
    @Json(name = "first")
    private String first;
    @Json(name = "last")
    private String last;
    public final static Parcelable.Creator<Name> CREATOR = new Creator<Name>() {

        public Name createFromParcel(Parcel in) {
            return new Name(in);
        }

        public Name[] newArray(int size) {
            return (new Name[size]);
        }

    }
            ;
    private final static long serialVersionUID = 6594513853407833494L;

    protected Name(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.first = ((String) in.readValue((String.class.getClassLoader())));
        this.last = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Name() {
    }

    /**
     *
     * @param last lastname
     * @param title title
     * @param first firstname
     */
    public Name(String title, String first, String last) {
        super();
        this.title = title;
        this.first = first;
        this.last = last;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Name withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public Name withFirst(String first) {
        this.first = first;
        return this;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Name withLast(String last) {
        this.last = last;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(first);
        dest.writeValue(last);
    }

    public int describeContents() {
        return 0;
    }

    @NotNull
    public String toString(){
        return this.title + " " + this.first + " " + this.last;
    }

}