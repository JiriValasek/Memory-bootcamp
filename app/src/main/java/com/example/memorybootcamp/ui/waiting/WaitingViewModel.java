package com.example.memorybootcamp.ui.waiting;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WaitingViewModel extends ViewModel {

    private final MutableLiveData<CharSequence> mWaitingText;
    private final MutableLiveData<Drawable> mIcon;
    private final MutableLiveData<Integer> mProgress;

    public WaitingViewModel() {
        mWaitingText = new MutableLiveData<>();
        mWaitingText.setValue("Placeholder");
        mIcon = new MutableLiveData<>();
        mIcon.setValue(null);
        mProgress = new MutableLiveData<>();
        mProgress.setValue(0);
    }

    public LiveData<CharSequence> getWaitingText() { return mWaitingText; }
    public LiveData<Drawable> getWaitingIcon() { return mIcon; }
    public LiveData<Integer> getProgress() { return mProgress; }

    public void setWaitingText(CharSequence waitingText){ mWaitingText.setValue(waitingText);}
    public void setWaitingIcon(Drawable waitingIcon){ mIcon.setValue(waitingIcon);}
    public void setProgress(int progress){ mProgress.setValue(progress);}

}