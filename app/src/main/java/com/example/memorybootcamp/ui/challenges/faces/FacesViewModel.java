package com.example.memorybootcamp.ui.challenges.faces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.memorybootcamp.ui.challenges.ChallengeViewModel;
import com.example.memorybootcamp.web.faceretrival.Faces;

public class FacesViewModel extends ChallengeViewModel {

    private final MutableLiveData<Faces> mFaces;
    private final MutableLiveData<String> mHeader;
    private final MutableLiveData<String> mDescription;
    private final MutableLiveData<float[][]> mScores;
    private final MutableLiveData<Boolean> mStartChallengeAllowed;

    public FacesViewModel() {
        super();
        mHeader = new MutableLiveData<>();
        mHeader.setValue("");
        mDescription = new MutableLiveData<>();
        mDescription.setValue("Loading faces...");
        mScores = new MutableLiveData<>();
        mStartChallengeAllowed = new MutableLiveData<>();
        mStartChallengeAllowed.setValue(false);
        mFaces = new MutableLiveData<>();
        mFaces.setValue(null);
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
    public LiveData<Faces> getFaces() {return mFaces;}

    public void setHeader(String header){mHeader.setValue(header);}
    public void setDescription(String description){mDescription.setValue(description);}
    public void setScores(float[][] scores){mScores.setValue(scores);}
    public void setStartChallengeAllowed(boolean allowed){mStartChallengeAllowed.setValue(allowed);}
    public void setFaces(Faces faces){mFaces.setValue(faces);}
}
