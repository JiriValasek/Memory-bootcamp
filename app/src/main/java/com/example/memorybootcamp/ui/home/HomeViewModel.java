package com.example.memorybootcamp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memorybootcamp.charts.ProgressRadarChart;

/** View model for home fragment. */
public class HomeViewModel extends ViewModel {

    /** Header text. */
    private final MutableLiveData<String> mHeader;
    /** Scores to be shown in a graph. */
    private final MutableLiveData<float[][]> mScores;

    /** Constructor */
    public HomeViewModel() {
        mHeader = new MutableLiveData<>();
        mHeader.setValue("This is home fragment");
        mScores = new MutableLiveData<>();
    }

    /** Getter for the header. */
    public LiveData<String> getHeader() { return mHeader; }
    /** Getter for the scores. */
    public LiveData<float[][]> getScores() { return mScores; }
    /** Setter for the header. */
    public void setHeader(String headerText) { this.mHeader.setValue(headerText); }
    /** Setter for the scores. */
    public void setScores(float[][] scores) { this.mScores.setValue(scores); }
}