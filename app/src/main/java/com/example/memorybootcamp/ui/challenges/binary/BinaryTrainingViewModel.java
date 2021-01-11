package com.example.memorybootcamp.ui.challenges.binary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BinaryTrainingViewModel extends ViewModel {

    private final MutableLiveData<CharSequence> mChallengeText;
    private final MutableLiveData<CharSequence> mDescriptionText;
    private final MutableLiveData<CharSequence> mButtonText;

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

    public void setChallengeText(CharSequence challengeText){
        mChallengeText.setValue(challengeText);
    }

    public void setDescriptionText(CharSequence descriptionText){
        mDescriptionText.setValue(descriptionText);
    }

    public void setButtonText(CharSequence buttonText){
        mButtonText.setValue(buttonText);
    }
}
