package com.example.memorybootcamp.ui.challenges;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChallengeViewModel extends ViewModel {

    private final MutableLiveData<String> mHeader;
    private final MutableLiveData<String> mDescription;
    private final MutableLiveData<float[][]> mScores;
    private final MutableLiveData<Boolean> mStartChallengeAllowed;


    public ChallengeViewModel() {
        mHeader = new MutableLiveData<>();
        mHeader.setValue("");
        mDescription = new MutableLiveData<>();
        mDescription.setValue("");
        mScores = new MutableLiveData<>();
        mStartChallengeAllowed = new MutableLiveData<>();
        mStartChallengeAllowed.setValue(false);
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
    public LiveData<Boolean> getStartChallengeAllowed(){ return mStartChallengeAllowed; }

    public void setHeader(String header){mHeader.setValue(header);}
    public void setDescription(String description){mDescription.setValue(description);}
    public void setScores(float[][] scores){mScores.setValue(scores);}
    public void setStartChallengeAllowed(boolean allowed){mStartChallengeAllowed.setValue(allowed);}
}