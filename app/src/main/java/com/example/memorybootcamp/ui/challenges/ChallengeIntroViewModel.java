package com.example.memorybootcamp.ui.challenges;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChallengeIntroViewModel extends ViewModel {

    private final MutableLiveData<String> mHeader;
    private final MutableLiveData<String> mDescription;
    private final MutableLiveData<float[][]> mScores;

    public ChallengeIntroViewModel() {
        mHeader = new MutableLiveData<>();
        mHeader.setValue("Placeholder");
        mDescription = new MutableLiveData<>();
        mDescription.setValue("Placeholder");
        mScores = new MutableLiveData<>();
    }

    public LiveData<String> getHeader() {
        return mHeader;
    }
    public LiveData<String> getDescription() {
        return mDescription;
    }
    public LiveData<float[][]> getScores() {
        return mScores;
    }

    public void setHeader(String headerText) { this.mHeader.setValue(headerText); }
    public void setDescription(String descriptionText) { this.mDescription.setValue(descriptionText); }
    public void setScores(float[][] scores) {
        this.mScores.setValue(scores);
    }

}