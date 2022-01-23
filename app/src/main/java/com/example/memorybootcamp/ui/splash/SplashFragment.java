package com.example.memorybootcamp.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.databinding.FragmentSplashBinding;

/** Splash screen fragment for the app. */
public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private SplashViewModel viewModel;
    private AnimatorSet animSet;

    /** "onCreateView" adding image to viewModel, fixing orientation and hiding action bar. */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ((AppCompatActivity)requireActivity()).getSupportActionBar().hide(); // disable going back and settings

        Drawable icon = ContextCompat
                .getDrawable(requireActivity(), R.drawable.ic_splashscreen_nonstop);
        viewModel.setSplashIcon(icon);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    /** "onViewCreated" preparing animation. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView splashImage = binding.imageViewSplash;
        animSet = new AnimatorSet();
        Animator fadeIn = AnimatorInflater.loadAnimator(getContext(), R.animator.splash_fade_in);
        fadeIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                NavDirections action = SplashFragmentDirections.actionSplashToHome();
                if (isAdded()) Navigation.findNavController(requireView()).navigate(action);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        fadeIn.setTarget(splashImage);
        animSet.play(fadeIn);
    }

    /** "onStart" starting the animation. */
    @Override
    public void onStart() {
        super.onStart();
        animSet.start();
    }

    /** "onDestroyView" unfixing orientation and returning action bar. */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity)requireActivity()).getSupportActionBar().show();
        binding = null;
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}