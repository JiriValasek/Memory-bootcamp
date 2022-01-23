package com.example.memorybootcamp.ui.challenges.binary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/** View model for binary number training. */
public class BinaryTrainingViewModel extends ViewModel {

    /** Text that user should remember */
    private final MutableLiveData<CharSequence> mChallengeText;
    /** View description (related to the view mode). */
    private final MutableLiveData<CharSequence> mDescriptionText;
    /** Text on a button for skipping timer/going back */
    private final MutableLiveData<CharSequence> mButtonText;
    /** Remaining time on the timer. */
    private final MutableLiveData<Long> mRemainingTime;

    /** Constructor setting all variables empty */
    public BinaryTrainingViewModel() {
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
    public void setChallengeText(CharSequence challengeText) {
        mChallengeText.setValue(challengeText);
    }
    /** Setter for description text. */
    public void setDescriptionText(CharSequence descriptionText) {
        mDescriptionText.setValue(descriptionText);
    }
    /** Setter for button text. */
    public void setButtonText(CharSequence buttonText){ mButtonText.setValue(buttonText); }
    /** Setter for remaining time in the timer. */
    public void setRemainingTime(long time) { mRemainingTime.setValue(time); }
}
