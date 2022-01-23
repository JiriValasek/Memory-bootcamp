package com.example.memorybootcamp.ui.challenges.cards;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/** Cards as a class that can be handed between fragments. */
public class Cards implements Serializable, Parcelable
{

    /** Necessary ID for serialization. */
    private final static long serialVersionUID = -7230819105524389279L;
    /** List of cards to be stored inside an object handed between fragments. */
    private List<Card> cards = null;

    /** Creator for parcelable classes. */
    public final static Parcelable.Creator<Cards> CREATOR = new Creator<Cards>() {

        public Cards createFromParcel(Parcel in) { return new Cards(in); }

        public Cards[] newArray(int size) { return (new Cards[size]); }

    };

    /** Constructor for Parcelable classes. */
    protected Cards(Parcel in) { in.readList(this.cards, (Card.class.getClassLoader())); }

    /** Constructor with no args for use in serialization. */
    public Cards() { }

    /**
     * Constructor for regular code.
     * @param cards Query cards with references to cards and names.
     */
    public Cards(List<Card> cards) {
        super();
        this.cards = cards;
    }

    /** Getter for list of cards. */
    public List<Card> getCards() { return cards; }
    /** Setter for list of cards. */
    public void setCards(List<Card> cards) { this.cards = cards; }

    /** Method for serialization. */
    public Cards withCards(List<Card> cards) {
        this.cards = cards;
        return this;
    }

    /** Necessary method for Parcelable classes. */
    public void writeToParcel(Parcel dest, int flags) { dest.writeList(cards); }
    /** Necessary method for Parcelable classes. */
    public int describeContents() { return 0; }
}