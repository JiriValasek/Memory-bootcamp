package com.example.memorybootcamp.web.faceretrival;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class Info implements Serializable, Parcelable
{

    @Json(name = "seed")
    private String seed;
    @Json(name = "results")
    private Integer results;
    @Json(name = "page")
    private Integer page;
    @Json(name = "version")
    private String version;
    public final static Parcelable.Creator<Info> CREATOR = new Creator<Info>() {

        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        public Info[] newArray(int size) {
            return (new Info[size]);
        }

    }
            ;
    private final static long serialVersionUID = -8164292511462597225L;

    protected Info(Parcel in) {
        this.seed = ((String) in.readValue((String.class.getClassLoader())));
        this.results = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.version = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Info() {
    }

    /**
     *
     * @param seed seed used for random generation of the faces
     * @param page page used for random generation fo the faces
     * @param results results with retrieved data
     * @param version version of the API
     */
    public Info(String seed, Integer results, Integer page, String version) {
        super();
        this.seed = seed;
        this.results = results;
        this.page = page;
        this.version = version;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public Info withSeed(String seed) {
        this.seed = seed;
        return this;
    }

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public Info withResults(Integer results) {
        this.results = results;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Info withPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Info withVersion(String version) {
        this.version = version;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(seed);
        dest.writeValue(results);
        dest.writeValue(page);
        dest.writeValue(version);
    }

    public int describeContents() {
        return 0;
    }

}