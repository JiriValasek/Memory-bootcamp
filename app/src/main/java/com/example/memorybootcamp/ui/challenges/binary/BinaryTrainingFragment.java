package com.example.memorybootcamp.ui.challenges.binary;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.memorybootcamp.R;

public class BinaryTrainingFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true // default to enabled
        ) {
            @Override
            public void handleOnBackPressed() {
                showAlertDialog(getContext(), "Training in progress", "Do you really wish to stop your Training?");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, // LifecycleOwner
                callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().findViewById(R.id.action_settings).setVisibility(View.GONE);

        //TODO unregister when leaving
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                SpannableString firstPart = new SpannableString("Memorize in: ");
                SpannableString lastPart = new SpannableString(" " + millisUntilFinished / 1000);

                firstPart.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.white)), 0, firstPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                lastPart.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.red)), 0, lastPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                toolbar.setTitle(TextUtils.concat(firstPart, lastPart));
            }

            public void onFinish() {
                toolbar.setTitle("done!");
            }
        }.start();
        return inflater.inflate(R.layout.fragment_binary_training, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.action_settings).setVisibility(View.VISIBLE);
    }

    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            NavDirections action = BinaryTrainingFragmentDirections.actionBinaryTrainingFragmentToNavBinary();
            Navigation.findNavController(getView()).navigate(action);
            // TODO destroy countdown timer
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> alertDialog.dismiss());

        // Showing Alert Message
        alertDialog.show();
    }
}