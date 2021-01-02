package com.example.memorybootcamp.ui.challenges;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChallengeIntroViewModel extends ViewModel {

    private final MutableLiveData<String> mHeader;
    private final MutableLiveData<float[][]> scores;

    public ChallengeIntroViewModel() {
        mHeader = new MutableLiveData<>();
        mHeader.setValue("Placeholder");
        scores = new MutableLiveData<>();
    }

    public LiveData<String> getHeader() {
        return mHeader;
    }
    public LiveData<float[][]> getScores() {
        return scores;
    }

    public void setHeader(String headerText) {
        this.mHeader.setValue(headerText);
    }
    public void setScores(float[][] scores) {
        this.scores.setValue(scores);
    }

}