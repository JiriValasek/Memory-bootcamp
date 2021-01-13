package com.example.memorybootcamp.ui.waiting;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.databinding.FragmentWaitingBinding;

import java.util.Locale;

public class WaitingFragment extends Fragment {

    private FragmentWaitingBinding binding;
    private WaitingViewModel viewModel;
    private CountDownTimer timer;
    private long timeMs;
    private NavDirections actionReturn, actionTimeout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                showAlertDialog(getContext(), "Training in progress",
                        "Do you really wish to stop your Training?");
            }};
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(WaitingViewModel.class);
        binding = FragmentWaitingBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide(); // disable going back and settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String type = WaitingFragmentArgs.fromBundle(getArguments()).getChallengeType();
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int foregroundColor = typedValue.data;
        Drawable icon;
        switch (type) {
            case "binary":
                icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_binary_numbers);
                icon.setTint(foregroundColor);
                viewModel.setWaitingIcon(icon);
                String time = sharedPreferences.getString(getString(R.string.binary_wait_key),
                        getString(R.string.binary_wait_default));
                timeMs = Math.round(Float.parseFloat(time)*60*1000);
                actionReturn = WaitingFragmentDirections.actionWaitingToBinary();
                actionTimeout = WaitingFragmentDirections.actionWaitingToBinaryTraining();
                ((WaitingFragmentDirections.ActionWaitingToBinaryTraining) actionTimeout)
                        .setMode("recollection");
                ((WaitingFragmentDirections.ActionWaitingToBinaryTraining) actionTimeout)
                        .setTaskContent(WaitingFragmentArgs.fromBundle(getArguments())
                                .getTaskContent());
                break;
            case "cards":
                icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cards);
                icon.setTint(foregroundColor);
                viewModel.setWaitingIcon(icon);
                break;
            case "faces":
                icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_faces);
                icon.setTint(foregroundColor);
                viewModel.setWaitingIcon(icon);
                break;
            case "numbers":
                icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_numbers);
                icon.setTint(foregroundColor);
                viewModel.setWaitingIcon(icon);
                break;
            case "words":
                icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_words);
                icon.setTint(foregroundColor);
                viewModel.setWaitingIcon(icon);
                break;
            default:
                break;
        }

        setupTimer();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Use the ViewModel
    }

    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title); // Setting Dialog Title
        alertDialog.setMessage(message); // Setting Dialog Message
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> Navigation.findNavController(getView()).navigate(actionReturn));
        // Setting Cancel button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> alertDialog.dismiss());
        alertDialog.show(); // Showing Alert Message
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        timer.cancel();
        binding = null;
    }

    private void setupTimer() {
        timer = new CountDownTimer( timeMs, 100) {

            public void onTick(long millisUntilFinished) {
                SpannableString firstPart = new SpannableString("Time to recollection is\n");
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (millisUntilFinished / (60*1000)) % 60;
                long hours = (millisUntilFinished / (60*60*1000));
                String time = String.format(Locale.US,"%02d:%02d:%02d", hours, minutes, seconds);
                SpannableString lastPart = new SpannableString(time);
                TypedValue typedValue = new TypedValue();
                getContext().getTheme().resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
                @ColorInt int foregroundColor = typedValue.data;
                firstPart.setSpan(new ForegroundColorSpan(foregroundColor), 0, firstPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastPart.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.red)), 0, lastPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.waitingText.setText(TextUtils.concat(firstPart, lastPart));
                binding.waitingProgress.setProgress(Math.round(100*((float)timeMs - millisUntilFinished)/timeMs));
            }

            public void onFinish() {
                binding.waitingProgress.setProgress(100);
                Navigation.findNavController(getView()).navigate(actionTimeout);
            }
        };
        timer.start();
    }

}