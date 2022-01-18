package com.example.memorybootcamp.ui.challenges.cards;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class Cards implements Serializable, Parcelable
{

    private List<Card> cards = null;
    public final static Parcelable.Creator<Cards> CREATOR = new Creator<Cards>() {

        public Cards createFromParcel(Parcel in) {
            return new Cards(in);
        }

        public Cards[] newArray(int size) {
            return (new Cards[size]);
        }

    };
    private final static long serialVersionUID = -7230819105524389279L;

    protected Cards(Parcel in) {
        in.readList(this.cards, (Card.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Cards() {
    }

    /**
     *
     * @param cards Query cards with references to cards and names.
     */
    public Cards(List<Card> cards) {
        super();
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Cards withCards(List<Card> cards) {
        this.cards = cards;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cards);
    }

    public int describeContents() {
        return 0;
    }
}