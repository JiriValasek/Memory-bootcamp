package com.example.memorybootcamp.ui.challenges.binary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BinaryTrainingViewModel extends ViewModel {

    /** Text that user should remember */
    private final MutableLiveData<CharSequence> mChallengeText;
    /** View description (related to the view mode). */
    private final MutableLiveData<CharSequence> mDescriptionText;
    /** Text on a button for skipping timer/going back */
    private final MutableLiveData<CharSequence> mButtonText;

    /** Constructor setting all variables empty */
    public BinaryTrainingViewModel() {
        mChallengeText = new MutableLiveData<>();
        mChallengeText.setValue("");
        mDescriptionText = new MutableLiveData<>();
        mDescriptionText.setValue("");
        mButtonText = new MutableLiveData<>();
        mButtonText.setValue("");
    }

    public LiveData<CharSequence> getChallengeText() {
        return mChallengeText;
    }
    public LiveData<CharSequence> getDescriptionText() {
        return mDescriptionText;
    }
    public LiveData<CharSequence> getButtonText() {
        return mButtonText;
    }

    public void setChallengeText(CharSequence challengeText) {
        mChallengeText.setValue(challengeText);
    }

    public void setDescriptionText(CharSequence descriptionText) {
        mDescriptionText.setValue(descriptionText);
    }

    public void setButtonText(CharSequence buttonText){
        mButtonText.setValue(buttonText);
    }
}
