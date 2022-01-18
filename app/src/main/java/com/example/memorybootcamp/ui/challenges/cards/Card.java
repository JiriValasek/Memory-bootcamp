package com.example.memorybootcamp.ui.challenges.cards;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Card implements Serializable, Parcelable {
    private String name;
    private int picture;

    protected Card(Parcel in) {
        name = in.readString();
        picture = in.readInt();
    }

    public Card(String name, int picture) {
        super();
        this.name = name;
        this.picture = picture;
    }

    /** No args constructor for use in serialization. */
    public Card() {}

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(picture);
    }

    public String getName(){
        return name;
    }

    public int getPicture(){
        return picture;
    }
}
