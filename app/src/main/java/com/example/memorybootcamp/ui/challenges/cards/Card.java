package com.example.memorybootcamp.ui.challenges.cards;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/** Card as a class that can be handed between fragments. */
public class Card implements Serializable, Parcelable {

    /** Name of a card. */
    private String name;
    /** Picture of a card. */
    private int picture;

    /** Creator for parcelable classes. */
    protected Card(Parcel in) {
        name = in.readString();
        picture = in.readInt();
    }

    /** Creator for regular use. */
    public Card(String name, int picture) {
        super();
        this.name = name;
        this.picture = picture;
    }

    /** Constructor with no args for use in serialization. */
    public Card() {}

    /** Creator for parcelable classes. */
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
    public int describeContents() { return 0; }
    /** Necessary method for Parcelable classes. */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(picture);
    }

    /** Getter for name. */
    public String getName(){
        if (name == null) {
            return "";
        } else {
            return name;
        }
    }
    /** Getter for picture. */
    public int getPicture(){ return picture; }
}
