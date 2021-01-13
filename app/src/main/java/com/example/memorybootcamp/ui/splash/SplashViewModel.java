package com.example.memorybootcamp.ui.splash;

import android.graphics.drawable.Drawable;

import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

@BindingMethods({
        @BindingMethod(type = android.widget.ImageView.class,
                attribute = "srcCompat",
                method = "setImageDrawable") })
public class SplashViewModel extends ViewModel {

    private final MutableLiveData<Drawable> mIcon;

    public SplashViewModel() {
        mIcon = new MutableLiveData<>();
        mIcon.setValue(null);
    }

    public LiveData<Drawable> getSplashIcon() { return mIcon; }

    public void setSplashIcon(Drawable splashIcon){ mIcon.setValue(splashIcon);}

}
