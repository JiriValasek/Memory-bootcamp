package com.example.memorybootcamp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mHeader;
    private final MutableLiveData<float[][]> scores;

    public HomeViewModel() {
        mHeader = new MutableLiveData<>();
        mHeader.setValue("This is home fragment");
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