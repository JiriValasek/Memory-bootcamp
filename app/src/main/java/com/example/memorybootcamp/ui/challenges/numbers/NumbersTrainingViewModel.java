package com.example.memorybootcamp.ui.challenges.numbers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/** View model for number training. */
public class NumbersTrainingViewModel extends ViewModel {

    /** Challenge text to be shown. */
    private final MutableLiveData<CharSequence> mChallengeText;
    /** Description text to be shown. */
    private final MutableLiveData<CharSequence> mDescriptionText;
    /** Text to be shown on a button. */
    private final MutableLiveData<CharSequence> mButtonText;
    /** Remaining time on the timer. */
    private final MutableLiveData<Long> mRemainingTime;

    /** Constructor */
    public NumbersTrainingViewModel() {
        mChallengeText = new MutableLiveData<>();
        mChallengeText.setValue("");
        mDescriptionText = new MutableLiveData<>();
        mDescriptionText.setValue("");
        mButtonText = new MutableLiveData<>();
        mButtonText.setValue("");
        mRemainingTime = new MutableLiveData<>();
        mRemainingTime.setValue(null);
    }

    /** Getter for challenge text. */
    public LiveData<CharSequence> getChallengeText() { return mChallengeText; }
    /** Getter for description text. */
    public LiveData<CharSequence> getDescriptionText() { return mDescriptionText; }
    /** Getter for button text. */
    public LiveData<CharSequence> getButtonText() { return mButtonText; }
    /** Getter for remaining time in the timer. */
    public LiveData<Long> getRemainingTime() { return mRemainingTime; }

    /** Setter for challenge text. */
    public void setChallengeText(CharSequence challengeText){
        mChallengeText.setValue(challengeText);
    }
    /** Setter for description text. */
    public void setDescriptionText(CharSequence descriptionText){
        mDescriptionText.setValue(descriptionText);
    }
    /** Setter for button text. */
    public void setButtonText(CharSequence buttonText){
        mButtonText.setValue(buttonText);
    }
    /** Setter for remaining time in the timer. */
    public void setRemainingTime(long time) { mRemainingTime.setValue(time); }

}
