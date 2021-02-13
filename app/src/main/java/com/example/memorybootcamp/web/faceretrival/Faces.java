package com.example.memorybootcamp.web.faceretrival;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class Faces implements Serializable, Parcelable
{

    @Json(name = "results")
    private List<Result> results = null;
    @Json(name = "info")
    private Info info;
    public final static Parcelable.Creator<Faces> CREATOR = new Creator<Faces>() {

        public Faces createFromParcel(Parcel in) {
            return new Faces(in);
        }

        public Faces[] newArray(int size) {
            return (new Faces[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7230819105524389279L;

    protected Faces(Parcel in) {
        in.readList(this.results, (com.example.memorybootcamp.web.faceretrival.Result.class.getClassLoader()));
        this.info = ((Info) in.readValue((Info.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Faces() {
    }

    /**
     *
     * @param results Query results with URLs to faces and names.
     * @param info Info about the query.
     */
    public Faces(List<Result> results, Info info) {
        super();
        this.results = results;
        this.info = info;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Faces withResults(List<Result> results) {
        this.results = results;
        return this;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Faces withInfo(Info info) {
        this.info = info;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(results);
        dest.writeValue(info);
    }

    public int describeContents() {
        return 0;
    }

}