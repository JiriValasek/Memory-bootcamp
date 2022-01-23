package com.example.memorybootcamp.ui.challenges.cards;

import android.content.res.Configuration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/** View model for card training. */
public class CardsTrainingViewModel extends ViewModel {

    /** Challenge text to be shown. */
    private final MutableLiveData<CharSequence> mChallengeText;
    /** Description text to be shown. */
    private final MutableLiveData<CharSequence> mDescriptionText;
    /** Text to be shown on a button. */
    private final MutableLiveData<CharSequence> mButtonText;
    /** Remaining time on the timer. */
    private final MutableLiveData<Long> mRemainingTime;
    /** Orientation of the screen. */
    private final MutableLiveData<Integer> mScreenOrientation;
    /** Items in a recycler view. */
    private final MutableLiveData<List<CardContent.CardItem>> mRecyclerViewItems;

    /** Constructor */
    public CardsTrainingViewModel() {
        mChallengeText = new MutableLiveData<>();
        mChallengeText.setValue("");
        mDescriptionText = new MutableLiveData<>();
        mDescriptionText.setValue("");
        mButtonText = new MutableLiveData<>();
        mButtonText.setValue("");
        mRemainingTime = new MutableLiveData<>();
        mRemainingTime.setValue(null);
        mScreenOrientation = new MutableLiveData<>();
        mScreenOrientation.setValue(-1);
        mRecyclerViewItems = new MutableLiveData<>();
        mRecyclerViewItems.setValue(null);
    }

    /** Getter for challenge text. */
    public LiveData<CharSequence> getChallengeText() { return mChallengeText; }
    /** Getter for description text. */
    public LiveData<CharSequence> getDescriptionText() { return mDescriptionText; }
    /** Getter for button text. */
    public LiveData<CharSequence> getButtonText() { return mButtonText; }
    /** Getter for remaining time in the timer. */
    public LiveData<Long> getRemainingTime() { return mRemainingTime; }
    /** Getter for recorded screen orientation. */
    public LiveData<Integer> getScreenOrientation() { return mScreenOrientation; }
    /** Getter for current items in recycler view. */
    public LiveData<List<CardContent.CardItem>> getRecyclerViewItems() {
        return mRecyclerViewItems;
    }

    /** Setter for challenge text. */
    public void setChallengeText(CharSequence challengeText){
        mChallengeText.setValue(challengeText);
    }
    /** Setter for description text. */
    public void setDescriptionText(CharSequence descriptionText){
        mDescriptionText.setValue(descriptionText);
    }
    /** Setter for button text. */
    public void setButtonText(CharSequence buttonText){ mButtonText.setValue(buttonText); }
    /** Setter for remaining time in the timer. */
    public void setRemainingTime(long time) { mRemainingTime.setValue(time); }
    /** Setter for recording screen orientation. */
    public void setScreenOrientation(int orientation) {
        mScreenOrientation.setValue(orientation);
    }
    /** Setter for recycler view items. */
    public void setRecyclerViewItems(List<CardContent.CardItem> cards) {
        mRecyclerViewItems.setValue(cards);
    }

}
